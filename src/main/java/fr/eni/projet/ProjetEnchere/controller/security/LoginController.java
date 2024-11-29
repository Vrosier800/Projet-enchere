package fr.eni.projet.ProjetEnchere.controller.security;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.projet.ProjetEnchere.bll.ArticleAVendreService;
import fr.eni.projet.ProjetEnchere.bll.EnchereService;
import fr.eni.projet.ProjetEnchere.bll.UtilisateurService;
import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;
import fr.eni.projet.ProjetEnchere.bo.Enchere;
import fr.eni.projet.ProjetEnchere.bo.NewMotDePasse;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;
import fr.eni.projet.ProjetEnchere.exceptions.BusinessException;
import jakarta.validation.Valid;

@Controller
@SessionAttributes({ "membreEnSession" })
public class LoginController {

	private UtilisateurService utilisateurService;
	private EnchereService enchereService;
	private ArticleAVendreService articleAVendreService;

	public LoginController(UtilisateurService utilisateurService, EnchereService enchereService,
			ArticleAVendreService articleAVendreService) {
		this.utilisateurService = utilisateurService;
		this.enchereService = enchereService;
		this.articleAVendreService = articleAVendreService;
	}

	@GetMapping("/login")
	String login(@ModelAttribute("membreEnSession") Utilisateur membreEnSession) {
		if (membreEnSession.getPseudo() != null)
			return "redirect:/";
		return "login";
	}

	@GetMapping("/inscription")
	String inscription(Model model, @ModelAttribute("membreEnSession") Utilisateur membreEnSession) {
		if (membreEnSession.getPseudo() != null)
			return "redirect:/";
		model.addAttribute("utilisateur", new Utilisateur());
		return "inscription";
	}

	@GetMapping("/session")
	public String chargerMembreEnSession(@ModelAttribute("membreEnSession") Utilisateur membreEnSession,
			Principal principal) {
		String pseudo = principal.getName();
		Utilisateur aCharger = utilisateurService.consulterUtilisateurPseudo(pseudo);
		if (aCharger != null) {
			membreEnSession.setPseudo(aCharger.getPseudo());
			membreEnSession.setNom(aCharger.getNom());
			membreEnSession.setPrenom(aCharger.getPrenom());
			membreEnSession.setTelephone(aCharger.getTelephone());
			membreEnSession.setEmail(aCharger.getEmail());
			membreEnSession.setCredit(aCharger.getCredit());
			membreEnSession.setAdmin(aCharger.isAdmin());
			membreEnSession.setAdresse(aCharger.getAdresse());
		}
		System.out.println("Ajout membre en session : " + membreEnSession.getPseudo());
		return "redirect:/";
	}
	
	@GetMapping("/administration")
	public String administration(@SessionAttribute("membreEnSession") Utilisateur membreEnSession, Model model) {
		if (membreEnSession.getPseudo() == null || membreEnSession.isAdmin() == false)
			return "redirect:/";
		List<Utilisateur> utilisateurs = utilisateurService.consulterUtilisateurs();

		model.addAttribute("users", utilisateurs);
		return "administration";
	}
	
	@PostMapping("/administration")
	public String filtrerAdministration(@RequestParam(value = "search", required = false, defaultValue = "") String recherche,
			@SessionAttribute("membreEnSession") Utilisateur membreEnSession, Model model) {
		if (membreEnSession.getPseudo() == null || membreEnSession.isAdmin() == false)
			return "redirect:/";
		
		List<Utilisateur> utilisateurs = utilisateurService.consulterUtilisateurs();
		List<Utilisateur> usersFiltres = utilisateurService.filtrerUtilisateurs(utilisateurs, recherche);

		model.addAttribute("users", usersFiltres);
		return "administration";
	}
	
	@GetMapping("/maxSessionExpiration")
	public String maxSessionExpiration() {
		return "expirations/maxSessionExpiration";
	}
	
	@GetMapping("/invalidSessionExpiration")
	public String invalidSessionExpiration() {
		return "expirations/invalidSessionExpiration";
	}

	@ModelAttribute("membreEnSession")
	public Utilisateur membreEnSession() {
		return new Utilisateur();
	}

	@PostMapping("/inscription")
	public String inscription(@Valid @ModelAttribute("utilisateur") Utilisateur utilisateur,
			BindingResult bindingResult) {
		if (!bindingResult.hasErrors()) {
			try {
				utilisateurService.creerUtilisateur(utilisateur);
				return "redirect:/login";
			} catch (BusinessException e) {
				e.getClefsExternalisations().forEach(key -> {
					ObjectError error = new ObjectError("globalError", key);
					bindingResult.addError(error);
				});
			}
		}
		return "inscription";
	}
	
	@GetMapping("/profile")
	public String profile(@RequestParam("pseudo") String pseudo, Model model) {
		Utilisateur utilisateur = utilisateurService.consulterUtilisateurPseudo(pseudo);
		model.addAttribute("utilisateur", utilisateur);
		model.addAttribute("pseudo", pseudo);
		return "profile";
	}

	@PostMapping("/profile")
	public String supprProfileByAdmin(@RequestParam("pseudo") String pseudo, @SessionAttribute("membreEnSession") Utilisateur membreEnSession) {
		if (membreEnSession.getPseudo() == null || membreEnSession.isAdmin() == false)
			return "redirect:/";
		
		Utilisateur consulterUtilisateurPseudo = utilisateurService.consulterUtilisateurPseudo(pseudo);
		if (membreEnSession.getPseudo() == consulterUtilisateurPseudo.getPseudo())
			return "redirect:/";
		
		List<Enchere> EncheresParUtilisateur = enchereService.consulterEnchereParUtilisateur(consulterUtilisateurPseudo);
		if (EncheresParUtilisateur.size() >= 1) enchereService.deleteByPseudo(pseudo);
		
		List<ArticleAVendre> ArticlesParPseudo = articleAVendreService.consulterArticlesByPseudo(pseudo);
		ArticlesParPseudo.forEach(a -> {
			List<Enchere> consulterEncheresParArticle = enchereService.consulterEncheresParArticle(a.getId());
			consulterEncheresParArticle.forEach(e -> {
				if (e != null)
					enchereService.deleteByArticle(e);
			});
		});
		if (ArticlesParPseudo.size() >= 1) articleAVendreService.deleteByPseudo(pseudo);
		
		utilisateurService.deleteByPseudo(pseudo);
		
		return "redirect:/administration";
	}
	
	@GetMapping("/monprofile")
	public String monprofile(@ModelAttribute("membreEnSession") Utilisateur utilisateur, Model model) {
		model.addAttribute("utilisateur", utilisateur);
		return "profile";
	}
	
	@GetMapping("/monprofile/modifier")
	public String modifier(@ModelAttribute("membreEnSession") Utilisateur utilisateur, Model model) {
		model.addAttribute("utilisateur", utilisateur);
		return "profile-modifier";
	}
	
	@PostMapping("/monprofile/modifier")
	public String modifier(@Valid @ModelAttribute("membreEnSession") Utilisateur utilisateur,
			BindingResult bindingResult) {
		try {
			utilisateurService.modifierUtilisateur(utilisateur);
			return "redirect:/session";
		} catch (BusinessException e) {
			e.getClefsExternalisations().forEach(key -> {
				ObjectError error = new ObjectError("globalError", key);
				bindingResult.addError(error);
			});
		}

		return "profile-modifier";
	}
	
	@GetMapping("/monprofile/modifier/supression")
	public String modifierSupression(@SessionAttribute("membreEnSession") Utilisateur utilisateur) {
		if (utilisateur.getPseudo() == null)
			return "redirect:/";
		
		String pseudo = utilisateur.getPseudo();
		Utilisateur consulterUtilisateurPseudo = utilisateurService.consulterUtilisateurPseudo(pseudo);
		
		List<Enchere> EncheresParUtilisateur = enchereService.consulterEnchereParUtilisateur(consulterUtilisateurPseudo);
		if (EncheresParUtilisateur.size() >= 1) enchereService.deleteByPseudo(pseudo);
		
		List<ArticleAVendre> ArticlesParPseudo = articleAVendreService.consulterArticlesByPseudo(pseudo);
		ArticlesParPseudo.forEach(a -> {
			List<Enchere> consulterEncheresParArticle = enchereService.consulterEncheresParArticle(a.getId());
			consulterEncheresParArticle.forEach(e -> {
				if (e != null)
					enchereService.deleteByArticle(e);
			});
		});
		if (ArticlesParPseudo.size() >= 1) articleAVendreService.deleteByPseudo(pseudo);
		
		utilisateurService.deleteByPseudo(pseudo);
		return "redirect:/logout";
	}

	@GetMapping("/monprofile/modifier/motdepasse")
	public String modifierMotDePasse(@ModelAttribute("newMotDePasse") NewMotDePasse newMotDePasse, Model model) {
		model.addAttribute("newMotDePasse", newMotDePasse);
		return "profile-modifier-password";
	}
	
	@PostMapping("/monprofile/modifier/motdepasse")
	public String modifierMotDePasse(@Valid @ModelAttribute("newMotDePasse") NewMotDePasse newMotDePasse,
			BindingResult bindingResult,
			@ModelAttribute("membreEnSession") Utilisateur utilisateur) {
		if (!bindingResult.hasErrors()) {
			try {
				utilisateurService.modifierMotDePasse(utilisateur, newMotDePasse);
				return "redirect:/session";
			} catch (BusinessException e) {
				e.getClefsExternalisations().forEach(key -> {
					ObjectError error = new ObjectError("globalError", key);
					bindingResult.addError(error);
				});
			}
		}
		return "profile-modifier-password";
	}
	
	@GetMapping("/forgotPassword")
	public String loadForgotPassword() {
		return "forgotPassword/forgot-password";
	}
	
	@PostMapping("/forgotPassword")
	public String forgotPassword(@RequestParam("email") String email, Model model) {
		Utilisateur utilisateur;
		try {
			utilisateur = utilisateurService.consulterUtilisateurEmail(email);
			
			if (utilisateur != null) {
				return "redirect:/resetPassword/" + utilisateur.getPseudo();
			}
		} catch (Exception e) {
			model.addAttribute("msg", "forgot-password.msg");
		}
		return "forgotPassword/forgot-password";
	}
	
	@GetMapping("/resetPassword/{pseudo}")
	public String resetForgotPassword(@PathVariable("pseudo") String pseudo, Model model) {
		model.addAttribute("pseudo", pseudo);
		return "forgotPassword/reset-password";
	}
	
	@PostMapping("/changePassword")
	public String resetPassword(@RequestParam("nouveauMotDePasse") String motDePasse, @RequestParam("pseudo") String pseudo, Model model) {
		try {
			Utilisateur utilisateur = utilisateurService.consulterUtilisateurPseudo(pseudo);
			utilisateurService.motDePasseOublierChanger(utilisateur, motDePasse);
			return "redirect:/login";
		} catch (BusinessException e) {
			model.addAttribute("msg", "reset-password.msg");
		}
		return "forgotPassword/reset-password";
	}
	
}
