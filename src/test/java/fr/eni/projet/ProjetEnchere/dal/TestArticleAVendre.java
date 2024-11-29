package fr.eni.projet.ProjetEnchere.dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import fr.eni.projet.ProjetEnchere.bo.Adresse;
import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;
import fr.eni.projet.ProjetEnchere.bo.Categorie;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestArticleAVendre {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	ArticleAVendreDAO articleDAO;
	@Autowired
	AdresseDAO adresseDAO;
	@Autowired
	CategorieDAO categorieDAO;
	@Autowired
	UtilisateurDAO utilisateurDAO;
	

//	@Test
//	void testArticleAVendre_queryForList() {
//		List<ArticleAVendre> articles = articleDAO.findAll();
//		assertNotNull(articles);
//		assertEquals(6, articles.size());
//		logger.info("QueryForList");
//		articles.forEach(e -> logger.info(e));
//	}
//	
//	@Test
//	void testArticleAVendreByPseudo_queryForList() {
//		List<ArticleAVendre> articles = articleDAO.findAllByPseudo("coach_toto");
//		assertNotNull(articles);
//		assertEquals(4, articles.size());
//		logger.info("QueryForList");
//		articles.forEach(e -> logger.info(e));
//	}
//	
//	@Test
//	void testArticleAVendre_QueryForObject() {
//		ArticleAVendre article = articleDAO.read(1);
//		assertNotNull(article);
//		assertEquals(1,article.getId() );
//		logger.info("QueryForObject");
//		logger.info(article);
//	}
	
	@Test
	void testCreate() {
		Adresse adresse = adresseDAO.read(2);
		Categorie categorie = categorieDAO.read(2);
		Utilisateur utilisateur = utilisateurDAO.read("coach_admin");
		LocalDate dateDebut = LocalDate.parse("2024-09-09");
		LocalDate dateFin = LocalDate.parse("2025-01-02");
		ArticleAVendre articleAVendre = new ArticleAVendre("PC_Poney","Un super pc poney",dateDebut,dateFin,1,adresse,categorie,utilisateur);
		
		articleDAO.create(articleAVendre);
		List<ArticleAVendre>articles =articleDAO.findAll();
		assertNotNull(articles);
		assertEquals(8, articles.size());
		logger.info("testCreate");
		articles.forEach(e -> logger.info(e));
	}
//	
//	@Test
//	void testUpdate() {
//		Adresse adresse = adresseDAO.read(3);
//		Categorie categorie = categorieDAO.read(1);
//		Utilisateur utilisateur = utilisateurDAO.read("coach_tata");
//		LocalDate dateDebut = LocalDate.parse("2024-09-09");
//		LocalDate dateFin = LocalDate.parse("2024-09-10");
//		String title = "Super Poney 2000";
//		String description = "Un poney avec des ailes velues";
//		int prix = 10;
//		ArticleAVendre artcileUpdate = new ArticleAVendre(title,description,dateDebut,dateFin,prix,adresse,categorie, utilisateur);
//		artcileUpdate.setStatut(1);
//		
//		artcileUpdate.setId(7);
//		articleDAO.update(artcileUpdate);
//		ArticleAVendre article = articleDAO.read(7);
//		assertNotNull(article);
//		int test = 125000;
//		assertEquals(test, article.getPrixVente());
//		logger.info("testUpdate");
//		logger.info(article);
//	}
//	
//	@Test
//	void testDelete() {
//		ArticleAVendre article = articleDAO.read(7);
//		articleDAO.delete(article.getId());
//		List<ArticleAVendre>articles = articleDAO.findAll();
//		assertEquals(6, articles.size());
//		logger.info("testDelete");
//		logger.info(articles);
//	}
}
