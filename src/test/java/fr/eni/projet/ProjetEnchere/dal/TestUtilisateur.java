package fr.eni.projet.ProjetEnchere.dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import fr.eni.projet.ProjetEnchere.bo.Adresse;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestUtilisateur {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	UtilisateurDAO utilisateurDAO;
	@Autowired
	AdresseDAO adresseDAO;
	@Test
	void testUtilisateur_queryForObject() {
		Utilisateur utilisateur = utilisateurDAO.read("coach_admin");
		assertNotNull(utilisateur);
		assertEquals("COACH",utilisateur.getNom());
		logger.info("QueryForObject");
		logger.info(utilisateur);
	}
	
	@Test
	void createUtilisateur() {
		String pseudo = "super_pseudo";
		String nom = "Bane";
		String prenom = "Ane";
		String email = "baneane@campus-eni.fr";
		String motDePasse = "Pa$$w0rd"; 
		int credit = 10;
		Adresse adresse = adresseDAO.read(2);
		Utilisateur utilisateur = new Utilisateur(pseudo, nom,prenom,email,null, motDePasse, credit,adresse);
		utilisateur.setAdmin(true);		
		utilisateurDAO.create(utilisateur);
		Utilisateur utilisateurtest = utilisateurDAO.read("super_pseudo");
		assertNotNull(utilisateur);
		assertEquals("Bane",utilisateurtest.getNom());
		logger.info("QueryForObject");
		logger.info(utilisateur);
	}
	
	@Test
	void updateUtilisateur() {
		Utilisateur utilisateurTest = utilisateurDAO.read("super_pseudo");
		
		String nom = "Banane";
		String prenom = "Lane";
		String email = "banelane@campus-eni.fr";
		int credit = 2;
		Adresse adresse = adresseDAO.read(2);	
		
		utilisateurTest.setAdresse(adresse);
		utilisateurTest.setCredit(credit);
		utilisateurTest.setAdmin(false);
		utilisateurTest.setNom(nom);
		utilisateurTest.setPrenom(prenom);
		utilisateurTest.setEmail(email);
		
		utilisateurDAO.update(utilisateurTest);
		Utilisateur utilisateurUpdate = utilisateurDAO.read("super_pseudo");
		
		assertNotNull(utilisateurUpdate);
		assertEquals("Banane",utilisateurUpdate.getNom());
		logger.info("QueryForObject");
		logger.info(utilisateurTest);
	}
}
