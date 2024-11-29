package fr.eni.projet.ProjetEnchere.bll;

import java.util.List;

import fr.eni.projet.ProjetEnchere.bo.Adresse;

public interface AdresseService {

	Adresse consulterAdresseParId(long id);
	
	List<Adresse> consulterAdresses();

	List<Adresse> consulterAdressesEni();
}
