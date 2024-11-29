package fr.eni.projet.ProjetEnchere.dal;

import java.util.List;

import fr.eni.projet.ProjetEnchere.bo.Utilisateur;

public interface UtilisateurDAO {
	void create(Utilisateur utilisateur);

	Utilisateur readEmail(String email);
	
	Utilisateur read(String pseudo);
	
	Utilisateur readMDP(String pseudo);

	void update(Utilisateur utilisateur);
	
	boolean findPseudo(String pseudo);
	
	boolean findEmail(String email);

	void updateMotDePasse(Utilisateur utilisateur);

	void updateCredit(Utilisateur utilisateur);

	List<Utilisateur> findAll();

	void deleteByPseudo(String pseudo);
}
