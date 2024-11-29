package fr.eni.projet.ProjetEnchere.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import fr.eni.projet.ProjetEnchere.bll.AdresseService;
import fr.eni.projet.ProjetEnchere.bo.Adresse;

@Component
public class StringToAdresseConverter implements Converter<String, Adresse> {

	private AdresseService adresseService;
	
	public StringToAdresseConverter(AdresseService adresseService) {
		this.adresseService = adresseService;
	}
	
	@Override
	public Adresse convert(String id) {
		Long theId = Long.parseLong(id);
		
		return adresseService.consulterAdresseParId(theId);
	}

}
