package fr.eni.projet.ProjetEnchere.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.projet.ProjetEnchere.bll.ArticleAVendreServiceImpl;
import fr.eni.projet.ProjetEnchere.bll.EnchereService;
import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;
import fr.eni.projet.ProjetEnchere.bo.ArticleDTO;
import fr.eni.projet.ProjetEnchere.bo.Enchere;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;
import fr.eni.projet.ProjetEnchere.exceptions.BusinessException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SessionAttributes({ "membreEnSession", "adresseEnSession", "categorieEnSession" })
public class ArticleCalendrierController {

	private ArticleAVendreServiceImpl articleService;
	private EnchereService enchereService;

	public ArticleCalendrierController(ArticleAVendreServiceImpl articleService, EnchereService enchereService) {
		this.articleService = articleService;
		this.enchereService = enchereService;
	}

	@GetMapping("/articles")
	public List<ArticleDTO> getAllArticles(@ModelAttribute("membreEnSession") Utilisateur membreEnSession, Model model) {
	    if (membreEnSession != null) {
	        List<ArticleAVendre> articles = articleService.consulterArticlesActifs();
	        
	        return articles.stream().map(article -> {
	            Enchere enchere = enchereService.consulterEnchereParArticle(article.getId());
	            
	            return new ArticleDTO(article, membreEnSession, enchere != null ? enchere : new Enchere());
	        }).collect(Collectors.toList());
	    }

	    System.out.println("Aucun membre connecté");
	    return Collections.emptyList();
	}

	@GetMapping("/utilisateur")
	public ResponseEntity<Utilisateur> getUtilisateur(@ModelAttribute("membreEnSession") Utilisateur membreEnSession) {
		return ResponseEntity.ok(membreEnSession);
	}

	@PostMapping("/encherir")
	public ResponseEntity<Map<String, Object>> creerEnchere(@Valid @RequestParam("montant") int montant,
			@RequestParam("id") long id, @RequestParam("pseudo") String pseudo, @RequestParam("date") LocalDate date,
			Model model, @ModelAttribute("membreEnSession") Utilisateur membreEnSession) {

		Enchere newEnchere = new Enchere();
		newEnchere.setMontant(montant);
		newEnchere.setAcquereur(membreEnSession);
		newEnchere.setDate(date);
		newEnchere.setArticleAVendre(articleService.consulterArticleParID(id));

		if (newEnchere.getAcquereur() == null || newEnchere.getArticleAVendre() == null) {
			return ResponseEntity.badRequest().body(Map.of("message", "Acquéreur ou article non trouvé"));
		}

		try {

			enchereService.creerUneEnchere(newEnchere);

		} catch (BusinessException e) {
			return ResponseEntity.badRequest().body(Map.of("message",
					"Erreur lors de la création/modification de l'enchère", "errors", e.getClefsExternalisations()));
		}

		return ResponseEntity.ok(
				Map.of("message",  "Enchère créée avec succès" ));
	}
}
