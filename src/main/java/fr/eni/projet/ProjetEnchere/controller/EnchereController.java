package fr.eni.projet.ProjetEnchere.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.projet.ProjetEnchere.bll.ArticleAVendreServiceImpl;
import fr.eni.projet.ProjetEnchere.bll.EnchereService;
import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;
import fr.eni.projet.ProjetEnchere.bo.Enchere;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;
import fr.eni.projet.ProjetEnchere.exceptions.BusinessException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/enchere")
@SessionAttributes({ "membreEnSession", "adresseEnSession", "categorieEnSession" })
public class EnchereController {

	EnchereService enchereService;
	ArticleAVendreServiceImpl articleService;

	public EnchereController(EnchereService enchereService, ArticleAVendreServiceImpl articleService) {
		this.enchereService = enchereService;
		this.articleService = articleService;
	}

	@PostMapping("/encherir")
	public String creerEnchere(@Valid @ModelAttribute("newEnchere") Enchere newEnchere, BindingResult bindingResult,
			@ModelAttribute("article") ArticleAVendre article,
			@ModelAttribute("membreEnSession") Utilisateur membreEnSession, Model model) {

		if (!bindingResult.hasErrors()) {
			try {
				newEnchere.setDate(LocalDate.now());
				newEnchere.setArticleAVendre(
						articleService.consulterArticleParID(newEnchere.getArticleAVendre().getId()));
				newEnchere.setAcquereur(membreEnSession);
				enchereService.creerUneEnchere(newEnchere);
				System.out.println(membreEnSession);
				return "redirect:/";

			} catch (BusinessException e) {
				e.getClefsExternalisations().forEach(key -> {
					ObjectError error = new ObjectError("globalError", key);
					bindingResult.addError(error);
				});
			}
		}

		model.addAttribute("article", newEnchere.getArticleAVendre());
		model.addAttribute("newEnchere", newEnchere);

		return "article";
	}
}
