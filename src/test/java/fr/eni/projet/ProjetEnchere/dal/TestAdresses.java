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

import fr.eni.projet.ProjetEnchere.bo.Adresse;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestAdresses {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	AdresseDAO adresseDAO;

	@Test
	void testAdresses_queryForList() {
		List<Adresse> adresses = adresseDAO.findAll();
		assertNotNull(adresses);
		assertEquals(5, adresses.size());
		logger.info("QueryForList");
		adresses.forEach(e -> logger.info(e));
	}

	@Test
	void testAdresses_queryForObject() {
		Adresse adresse = adresseDAO.read(1);
		assertNotNull(adresse);
		assertEquals("44800", adresse.getCodePostal());
		logger.info("QueryForObject");
		logger.info(adresse);
	}
	
	@Test
	void testCreate() {
		String rue = "Rue des poneys";
		String ville = "Poney-City";
		String codePostal = "56789";
		Adresse adresse = new Adresse(rue,codePostal,ville);
		adresseDAO.create(adresse);
		Adresse adresseTest = adresseDAO.read(6);
		assertNotNull(adresseTest);
		assertEquals("66666", adresseTest.getCodePostal());
		logger.info("testCreate");
		logger.info(adresse);
	}
	
	@Test
	void testUpdate() {
		Adresse adresse = adresseDAO.read(6);
		String rue = "Rue des poneys players";
		String ville = "Poney-Land";
		String codePostal = "77777";
		adresse.setCodePostal(codePostal);
		adresse.setRue(rue);
		adresse.setVille(ville);
		adresseDAO.update(adresse);
		Adresse adresseTest = adresseDAO.read(8);
		assertNotNull(adresseTest);
		assertEquals("77777", adresseTest.getCodePostal());
		logger.info("testUpdate");
		logger.info(adresse);
	}

	@Test
	void testDelete() {
		long delete = 6;
		adresseDAO.delete(delete);
		List<Adresse>adresses = adresseDAO.findAll();
		assertEquals(5, adresses.size());
		logger.info("testDelete");
		logger.info(adresses);
	}
}
