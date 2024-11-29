package fr.eni.projet.ProjetEnchere.bll;

import java.util.List;

import fr.eni.projet.ProjetEnchere.bo.Categorie;

public interface CategorieService {

	Categorie consulterCategorieParID(long id);

	List<Categorie> consulterCategorie();

}
