package fr.eni.projet.ProjetEnchere.bll;

import java.util.List;

import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;
import fr.eni.projet.ProjetEnchere.bo.Enchere;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;

public interface EnchereService {
	
	Enchere consulterEnchereParArticle(long id);
	
	List<Enchere> consulterEnchereParUtilisateur(Utilisateur utilisateur);

	void creerUneEnchere( Enchere enchere);

	boolean premiereEnchere(Enchere enchere);

	void deleteByPseudo(String pseudo);

	void deleteByArticle(Enchere enchereParArticle);

	List<Enchere> consulterEncheresParArticle(long id);

	void finaliserUneEnchere(Utilisateur vendeur, ArticleAVendre article, Enchere enchere);

}
