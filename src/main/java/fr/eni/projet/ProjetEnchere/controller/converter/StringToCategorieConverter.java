package fr.eni.projet.ProjetEnchere.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import fr.eni.projet.ProjetEnchere.bll.CategorieService;
import fr.eni.projet.ProjetEnchere.bo.Categorie;

@Component
public class StringToCategorieConverter implements Converter<String, Categorie> {
	
	
	private CategorieService categorieService;

	public StringToCategorieConverter(CategorieService categorieService) {
		this.categorieService = categorieService;
	}

	@Override
	public Categorie convert(String id) {
		Long theId = Long.parseLong(id);

		return categorieService.consulterCategorieParID(theId);
	}
}
