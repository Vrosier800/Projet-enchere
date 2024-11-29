package fr.eni.projet.ProjetEnchere.dal;

import java.util.List;

import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;
import fr.eni.projet.ProjetEnchere.bo.Enchere;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;

public interface EnchereDAO {
	Enchere read(Utilisateur acquereur, ArticleAVendre article);
	List<Enchere> readByUser(Utilisateur acquereur);
	List<Enchere> findAll(long id);
	void create(Enchere enchere);
	void delete(Enchere enchere);
	void update(Enchere enchere);
	int countOfferUser(Enchere enchere );
	Enchere getLastOffer(Enchere enchere);
	int countOffer(Enchere enchere);
	int getMaxOffer(Enchere enchere);
	void deleteByPseudo(String pseudo);
	void deleteByIdArticle(long id);
}
