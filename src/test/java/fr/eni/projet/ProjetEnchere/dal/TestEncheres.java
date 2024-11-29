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

import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;
import fr.eni.projet.ProjetEnchere.bo.Enchere;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestEncheres {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	EnchereDAO enchereDAO;

	@Autowired
	UtilisateurDAO utilisateurDAO;
	
	@Autowired
	ArticleAVendreDAO articleDAO;

//	@Test
//	void testEncheres_queryForList() {
//		List<Enchere> encheres = enchereDAO.findAll(4);
//		assertNotNull(encheres);
//		assertEquals(2, encheres.size());
//		logger.info("QueryForList");
//		encheres.forEach(e -> logger.info(e));
//	}
//
//	@Test
//	void testEncheres_queryForListUtilisateur() {
//		Utilisateur utilisateur = utilisateurDAO.read("coach_titi");
//		List<Enchere>encheres = enchereDAO.readByUser(utilisateur);
//		assertNotNull(encheres);
//		assertEquals(2, encheres.size());
//		logger.info("QueryForList");
//		encheres.forEach(e -> logger.info(e));
//	}
//	
//	@Test
//	void testEncheres_queryForObject() {
//		ArticleAVendre article = articleDAO.read(4);
//		Utilisateur utilisateur = utilisateurDAO.read("coach_titi");
//		Enchere enchere = enchereDAO.read(utilisateur, article);
//		assertNotNull(enchere);
//		assertEquals(4, enchere.getArticleAVendre().getId());
//		logger.info("QueryForObject");
//		logger.info(enchere);
//	}
	
	@Test
	void testCreate() {
		LocalDate date = LocalDate.parse("2024-09-05");
		int montant = 10;
		Utilisateur utilisateur = utilisateurDAO.read("coach_titi");
		ArticleAVendre article = articleDAO.read(5);
		Enchere enchere = new Enchere (date, montant,utilisateur,article);
		enchereDAO.create(enchere);
		List<Enchere>encheres = enchereDAO.readByUser(utilisateur);
		assertNotNull(encheres);
		assertEquals(1, encheres.size());
		logger.info("testCreate");
		encheres.forEach(e -> logger.info(e));
		
	}
	
//	@Test
//	void testUpdate() {
//		LocalDate date = LocalDate.parse("2025-09-05");
//		int montant = 2;
//		Utilisateur utilisateur = utilisateurDAO.read("coach_admin");
//		ArticleAVendre article = articleDAO.read(5);
//		Enchere enchere = new Enchere (date, montant,utilisateur,article);
//		enchereDAO.update(enchere);
//		Enchere enchereTest = enchereDAO.read(utilisateur, article);
//		assertNotNull(enchereTest);
//		assertEquals(2, enchereTest.getMontant());
//		logger.info("testUpdate");
//		logger.info(enchereTest);
//	}
//	
//	@Test
//	void testDelete() {
//		Utilisateur utilisateur = utilisateurDAO.read("coach_admin");
//		assertNotNull(utilisateur,"l'utilisateur doit exister");
//		ArticleAVendre article = articleDAO.read(5);
//		long id = article.getId();
//		assertEquals(5, id);
//		assertNotNull(article,"l'article doit exister");
//		article.setId(id);
//		Enchere enchere = enchereDAO.read(utilisateur, article);
//		assertNotNull(enchere,"l'enchere doit exister");
//		enchereDAO.delete(enchere);
//		List<Enchere> encheres = enchereDAO.readByUser(utilisateur);
//		assertNotNull(encheres);
//		assertEquals(0, encheres.size());
//		logger.info("testDelete");
//		encheres.forEach(e -> logger.info(e));
//	}

}
