package fr.eni.projet.ProjetEnchere.bll;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;

public interface ArticleAVendreService {

	List<ArticleAVendre> consulterArticles();

	ArticleAVendre consulterArticleParID(long id);

	void creerArticle(ArticleAVendre article);

	void modifierArticle(ArticleAVendre article);

	void annulerArticle(ArticleAVendre article);

	List<ArticleAVendre> consulterArticlesActifs();

	List<ArticleAVendre> consulterArticlesMembreEnSession();

	List<ArticleAVendre> consulterArticleActifsParPseudo(String pseudo);

	List<ArticleAVendre> consulterArticleGagneParPseudo(String pseudo);

	List<ArticleAVendre> filtrerArticles(List<ArticleAVendre> articles, String categorie, String recherche);

	List<ArticleAVendre> consulterArticleActifsParEnchere(Utilisateur utilisateur);

	List<ArticleAVendre> consulterArticleTermineeParEnchere(Utilisateur utilisateur);

	List<ArticleAVendre> consulterArticlesByPseudo(String pseudo);

	void deleteByPseudo(String pseudo);

	void miseAJourStatut();

	List<ArticleAVendre> consulterArticlesParMembreEnSessionEtStatut(String pseudo, int statut);

	Page<ArticleAVendre> getArticles(Pageable pageable);

}
