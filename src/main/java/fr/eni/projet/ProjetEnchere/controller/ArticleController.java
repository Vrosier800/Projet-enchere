package fr.eni.projet.ProjetEnchere.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import fr.eni.projet.ProjetEnchere.bll.AdresseService;
import fr.eni.projet.ProjetEnchere.bll.ArticleAVendreService;
import fr.eni.projet.ProjetEnchere.bll.EnchereService;
import fr.eni.projet.ProjetEnchere.bll.ImagesService;
import fr.eni.projet.ProjetEnchere.bo.Adresse;
import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;
import fr.eni.projet.ProjetEnchere.bo.Categorie;
import fr.eni.projet.ProjetEnchere.bo.Enchere;
import fr.eni.projet.ProjetEnchere.bo.Image;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;
import fr.eni.projet.ProjetEnchere.exceptions.BusinessException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/article")
@SessionAttributes({ "membreEnSession", "adresseEnSession", "categorieEnSession" })
public class ArticleController {

	private AdresseService adresseService;
	private ArticleAVendreService articleAVendreService;
	private ImagesService imagesService;
	private EnchereService enchereService;

	private static final String dossier = "imagesUtilisateurs";

	public ArticleController(AdresseService adresseService, ArticleAVendreService articleAVendreService,
			EnchereService enchereService, ImagesService imagesService) {
		this.adresseService = adresseService;
		this.articleAVendreService = articleAVendreService;
		this.enchereService = enchereService;
		this.imagesService = imagesService;
	}

	@ModelAttribute("adresseEnSession")
	public List<Adresse> chargerAdresse(@ModelAttribute("membreEnSession") Utilisateur membreEnSession) {
		System.out.println("Chargement des adresses en session.");
		List<Adresse> listeEnSession = new ArrayList<>();
		if (membreEnSession != null) {
			listeEnSession.add(membreEnSession.getAdresse());

			List<Adresse> listeEni = adresseService.consulterAdressesEni();
			for (Adresse adresse : listeEni) {
				if (adresse != null) {
					Optional<Adresse> isExist = listeEnSession.stream()
							.filter(a -> a != null && a.getRue().equals(adresse.getRue())
									&& a.getVille().equals(adresse.getVille())
									&& a.getCodePostal().equals(adresse.getCodePostal()))
							.findAny();
					if (isExist.isEmpty()) {
						listeEnSession.add(adresse);
					}
				}
			}
		}
		return listeEnSession;
	}

	@GetMapping("/detail")
	public String afficherUnArticle(@RequestParam(name = "id", required = true) long id, Model model) {

		ArticleAVendre article = articleAVendreService.consulterArticleParID(id);
		
		Enchere enchere = enchereService.consulterEnchereParArticle(id);
		
		Enchere newEnchere = new Enchere();
		newEnchere.setArticleAVendre(article);
		

		model.addAttribute("article", article);
		model.addAttribute("enchere", enchere);
		model.addAttribute("newEnchere", newEnchere);
		
		try {
			Image image = imagesService.consulterImageParArticle(id);
			
			System.out.println(image.getNom());
//			 String nom = image.getArticleAVendre().getVendeur().getPseudo()
//					 + "_"
//					 + image.getArticleAVendre().getId() 
//					 + image.getNom().substring(image.getNom().lastIndexOf("."));
			String nom = image.getNom();

			model.addAttribute("image", "/imagesUtilisateurs/"+nom);
		} catch (Exception e){
			model.addAttribute("image", null);

		}
			return "article";

		}

	@GetMapping("/creer")
	public String creerUnArticle(@ModelAttribute("membreEnSession") Utilisateur membreEnSession, Model model,
			@ModelAttribute("articleAVendre") ArticleAVendre article,
			@ModelAttribute("adresseEnSession") List<Adresse> adresses,
			@ModelAttribute("categorieEnSession") List<Categorie> categories) {

		if (membreEnSession != null) {

			model.addAttribute("articleAVendre", new ArticleAVendre());
			return "creation";
		} else {
			return "redirect:/";
		}

	}

	@PostMapping("/creer")
	public String creerArticle(@Valid @ModelAttribute("articleAVendre") ArticleAVendre article,
			BindingResult bindingResult, @ModelAttribute("membreEnSession") Utilisateur membreEnSession) {
		if (!bindingResult.hasErrors()) {
			try {
				article.setVendeur(membreEnSession);
				articleAVendreService.creerArticle(article);
				return "redirect:/";
			} catch (BusinessException e) {
				e.getClefsExternalisations().forEach(key -> {
					ObjectError error = new ObjectError("globalError", key);
					bindingResult.addError(error);
				});
			}
		}
		return "creation";
	}

	@GetMapping("/modifier")
	public String getMethodName(@ModelAttribute("membreEnSession") Utilisateur membreEnSession, Model model,
			@ModelAttribute("articleAVendre") ArticleAVendre article,
			@ModelAttribute("adresseEnSession") List<Adresse> adresses,
			@ModelAttribute("categorieEnSession") List<Categorie> categories) {
		if (membreEnSession != null) {
			article = articleAVendreService.consulterArticleParID(article.getId());
			model.addAttribute("articleAVendre", article);
			return "modification";
		} else {
			return "redirect:/";
		}
	}

	@PostMapping("/modifier")
	public String modifierArticle(@Valid @ModelAttribute("articleAVendre") ArticleAVendre article,
			BindingResult bindingResult, @ModelAttribute("membreEnSession") Utilisateur membreEnSession) {
		if (!bindingResult.hasErrors()) {
			try {
				article.setVendeur(membreEnSession);
				articleAVendreService.modifierArticle(article);
				return "redirect:/";
			} catch (BusinessException e) {
				e.getClefsExternalisations().forEach(key -> {
					ObjectError error = new ObjectError("globalError", key);
					bindingResult.addError(error);
				});
			}
		}
		return "modification";
	}

	@GetMapping("/annuler")
	public String annulerArticle(@Valid @RequestParam(name = "id") long id,
			@ModelAttribute("articleAVendre") ArticleAVendre article, BindingResult bindingResult,
			@ModelAttribute("membreEnSession") Utilisateur membreEnSession) {
		if (!bindingResult.hasErrors()) {
			try {
				article = articleAVendreService.consulterArticleParID(id);
				articleAVendreService.annulerArticle(article);
				return "redirect:/";
			} catch (BusinessException e) {
				e.getClefsExternalisations().forEach(key -> {
					ObjectError error = new ObjectError("globalError", key);
					bindingResult.addError(error);
				});
			}
		}
		return "modification";
	}

	@GetMapping("/calendrier")
	public String afficherCalendrier() {
		return "calendrier";
	}

	@GetMapping("/image")
	public String afficherTelechargementImage(@RequestParam(name = "id", required = true) long id, Model model) {
		System.out.println(id);
		model.addAttribute("id", id);

		return "ajout-image";
	}

	@PostMapping("/image")
	public String telechargerImage(

			@RequestParam(name = "fichier", required = true) MultipartFile fichier,
			@RequestParam(name = "id", required = true) String id,
			@SessionAttribute("membreEnSession") Utilisateur membreEnSession, Model model) {

		long idLong = Long.parseLong(id);
		System.out.println("Début de la méthode telechargerImage");
		System.out.println("ID de l'article : " + idLong);
		System.out.println("Fichier reçu : " + fichier.getOriginalFilename());

		if (fichier.isEmpty()) {
			System.out.println("Le fichier est vide !");

			model.addAttribute("msg", "image.vide");

			return "ajout-image";
		} else if (fichier.getSize() > 10 * 1024 * 1024) {
			model.addAttribute("msg", "image.tropLourde");
			return "ajout-image";
		}

		try {
			ArticleAVendre article = articleAVendreService.consulterArticleParID(idLong);

			String originalFilename = fichier.getOriginalFilename();

			// Extraction
			String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

			// Renommage
			String nom = membreEnSession.getPseudo() + "_" + article.getId() + extension;

			// Chemin de destination (initialisé en haut)
			Path chemin = Paths.get(dossier, nom);
			System.out.println("Chemin du fichier : " + chemin);

			// Créer le dossier s'il n'existe pas
			Files.createDirectories(chemin.getParent());

			// Enregistrement
			if (!Files.exists(chemin)) {
				Files.copy(fichier.getInputStream(), chemin);
				
				Image image = new Image();
				image.setArticleAVendre(article);
				image.setNom(nom);

				imagesService.ajouterImage(image);
			} else {
				fichier.transferTo(chemin);
			}

			return "redirect:/";

		} catch (BusinessException e) {
			e.getClefsExternalisations().forEach(key -> {
				model.addAttribute("msg", key);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "ajout-image";

	}

	@GetMapping("/retrait")
	public String retraitEnchere(@Valid @RequestParam(name = "id") long id,
			@RequestParam(name = "pseudo") String enchereur, @SessionAttribute("membreEnSession") Utilisateur vendeur) {
		Enchere enchereGagnante = enchereService.consulterEnchereParArticle(id);
		ArticleAVendre articleGagne = articleAVendreService.consulterArticleParID(id);
		enchereService.finaliserUneEnchere(vendeur, articleGagne, enchereGagnante);
		return "redirect:/";
	}
}