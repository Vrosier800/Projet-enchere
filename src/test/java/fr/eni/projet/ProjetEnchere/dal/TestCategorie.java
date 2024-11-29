package fr.eni.projet.ProjetEnchere.dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import fr.eni.projet.ProjetEnchere.bo.Categorie;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestCategorie {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	CategorieDAO categorieDAO;
	
	@Test
	void testCategorie_queryForList() {
		List<Categorie> categories = categorieDAO.findAll();
		assertNotNull(categories);
		assertEquals(4, categories.size());
		logger.info("QueryForList");
		categories.forEach(e -> logger.info(e));
	}
	
	@Test
	void testCategorie_queryForObject() {
		Categorie categorie = categorieDAO.read(1);
		assertNotNull(categorie);
		assertEquals("Ameublement", categorie.getLibelle());
		logger.info("QueryForObject");
		logger.info(categorie);
	}
	
	@Test 
	void testCreate() {
		Categorie categorie = new Categorie("Manga");
		categorieDAO.create(categorie);
		Categorie categorieTest = categorieDAO.read(5);
		assertNotNull(categorieTest);
		assertEquals("Manga", categorieTest.getLibelle());
		logger.info("testCreate");
		logger.info(categorieTest);
	}
	
	@Test
	void testUpdate() {
		Categorie categorie = categorieDAO.read(5);
		categorie.setLibelle("Animé");
		categorieDAO.update(categorie);
		assertNotNull(categorie);
		assertEquals("Animé", categorie.getLibelle());
		logger.info("testUpdate");
		logger.info(categorie);
	}
	
	@Test
	void testDelete() {
		Categorie categorie = categorieDAO.read(5);
		categorieDAO.delete(categorie.getId());
		List<Categorie> categories = categorieDAO.findAll();
		assertEquals(4, categories.size());
		logger.info("testDelete");
		logger.info(categories);
	}
}
