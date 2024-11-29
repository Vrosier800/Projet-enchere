package fr.eni.projet.ProjetEnchere.dal;

import java.util.List;

import fr.eni.projet.ProjetEnchere.bo.Categorie;

public interface CategorieDAO {
	Categorie read(long id);
	List<Categorie>findAll();
	void create(Categorie categorie);
	void update(Categorie categorie);
	void delete(long id);
}
