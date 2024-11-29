package fr.eni.projet.ProjetEnchere.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.projet.ProjetEnchere.bll.ArticleAVendreService;
import fr.eni.projet.ProjetEnchere.bll.CategorieService;
import fr.eni.projet.ProjetEnchere.bll.EnchereService;
import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;
import fr.eni.projet.ProjetEnchere.bo.Categorie;
import fr.eni.projet.ProjetEnchere.bo.Enchere;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;

@Controller
@RequestMapping("/")
@SessionAttributes({ "membreEnSession", "adresseEnSession", "categorieEnSession", "articlesSession" })
public class indexController {

	private ArticleAVendreService articleAVendreService;
	private EnchereService enchereService;
	private CategorieService categorieService;

	public indexController(ArticleAVendreService articleAVendreService, EnchereService enchereService,
			CategorieService categorieService) {
		this.articleAVendreService = articleAVendreService;
		this.enchereService = enchereService;
		this.categorieService = categorieService;
	}

	@ModelAttribute("categorieEnSession")
	public List<Categorie> chargerCategorie() {
		return categorieService.consulterCategorie();
	}
	
	@ModelAttribute("articlesSession")
	public List<ArticleAVendre> chargeListEnSession() {
		return articleAVendreService.consulterArticlesActifs();
	}

	@GetMapping
	public String afficherArticles(@RequestParam(name = "page", defaultValue = "0") Integer page,
	                               @RequestParam(name = "size", defaultValue = "2") Integer size, 
	                               @ModelAttribute("articlesSession") List<ArticleAVendre> articles,Model model) {
		
		Pageable pageable = PageRequest.of(page, size);
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), articles.size());
		List<ArticleAVendre> paginatedList = articles.subList(start, end);

		Page<ArticleAVendre> articlesModifiesPage = new PageImpl<>(paginatedList, pageable, articles.size());

		articlesModifiesPage.getContent().forEach(a -> {
			Enchere enchere = enchereService.consulterEnchereParArticle(a.getId());
			if (enchere != null) {
				a.setPrixVente(enchere.getMontant());
			} else {
				a.setPrixVente(0);
			}
		});
		model.addAttribute("articles", articlesModifiesPage);
		
	    return "index";
	}

	@PostMapping
	public String filtrerArticles(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "2") Integer size,
			@RequestParam(value = "search", required = false, defaultValue = "") String recherche,
			@RequestParam(value = "category", required = false, defaultValue = "all") String categorie,
			@RequestParam(value = "type", required = true, defaultValue = "achats") String typeEnchere,
			@RequestParam(value = "achats", required = false) String achats,
			@RequestParam(value = "ventes", required = false) String ventes,
			@SessionAttribute(name = "membreEnSession", required = false) Utilisateur membreEnSession, Model model) {

		List<ArticleAVendre> articlesFiltres;
		List<ArticleAVendre> articlesActifs = articleAVendreService.consulterArticlesActifs();

		// PREMIER FILTRAGE HORS LIGNE

		articlesFiltres = articleAVendreService.filtrerArticles(articlesActifs, categorie, recherche);

		// DEUXIEME FILTRAGE EN LIGNE

		if (membreEnSession != null) {
			List<ArticleAVendre> articlesGagnees = articleAVendreService
					.consulterArticleTermineeParEnchere(membreEnSession);
			List<ArticleAVendre> articlesEncheres = articleAVendreService
					.consulterArticleActifsParEnchere(membreEnSession);
			if (typeEnchere.equals("achats")) {
				switch (achats) {
				case "achatsOuvertes":
					articlesFiltres = articleAVendreService.filtrerArticles(articlesActifs, categorie, recherche);
					break;
				case "achatsEnCours":
					articlesFiltres = articleAVendreService.filtrerArticles(articlesEncheres, categorie, recherche);
					break;
				case "achatsGagnees":
					articlesFiltres = articleAVendreService.filtrerArticles(articlesGagnees, categorie, recherche);
					break;
				}
			} else if (typeEnchere.equals("ventes")) {
				List<ArticleAVendre> articlesUtilisateur = new ArrayList<>();
				switch (ventes) {
				case "ventesEnCours":
					articlesUtilisateur = articleAVendreService
							.consulterArticlesParMembreEnSessionEtStatut(membreEnSession.getPseudo(), 1);
					articlesFiltres = articleAVendreService.filtrerArticles(articlesUtilisateur, categorie, recherche);
					break;
				case "ventesNonDebutees":
					articlesUtilisateur = articleAVendreService
							.consulterArticlesParMembreEnSessionEtStatut(membreEnSession.getPseudo(), 0);
					articlesFiltres = articleAVendreService.filtrerArticles(articlesUtilisateur, categorie, recherche);
					break;
				case "ventesTerminees":
					articlesUtilisateur = articleAVendreService
							.consulterArticlesParMembreEnSessionEtStatut(membreEnSession.getPseudo(), 2);
					articlesUtilisateur.addAll(articleAVendreService
							.consulterArticlesParMembreEnSessionEtStatut(membreEnSession.getPseudo(), 3));
					System.out.println(articlesUtilisateur);
					articlesFiltres = articleAVendreService.filtrerArticles(articlesUtilisateur, categorie, recherche);
					break;
				}
			}
		}
		
		model.addAttribute("articlesSession", articlesFiltres);
		return "redirect:/";
	}

}
