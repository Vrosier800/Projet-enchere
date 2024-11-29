package fr.eni.projet.ProjetEnchere.dal;

import java.util.List;

import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;

public interface ArticleAVendreDAO {
	ArticleAVendre read (long id);
	List<ArticleAVendre>findAll();
	void create(ArticleAVendre article);
	void delete(long id);
	void update(ArticleAVendre article);
	List<ArticleAVendre> findAllByPseudo(String pseudo);
	List<ArticleAVendre> findAllActive();
	void updateStatut(ArticleAVendre article, int statut);
	int checkStatut(long id);
	List<ArticleAVendre> findAllUser();
	List<ArticleAVendre> findAllActiveByUser(String pseudo);
	List<ArticleAVendre> findAllCloseByUser(String pseudo);
	void deleteByPseudo(String pseudo);
	void updatePrixVente(ArticleAVendre article, int prixVente);
	List<ArticleAVendre> findByUserAndStatut(String pseudo, int statut);
}
