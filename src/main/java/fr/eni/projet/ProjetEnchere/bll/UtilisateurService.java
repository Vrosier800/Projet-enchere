package fr.eni.projet.ProjetEnchere.bll;

import java.util.List;

import fr.eni.projet.ProjetEnchere.bo.NewMotDePasse;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;

public interface UtilisateurService {
	
	void creerUtilisateur(Utilisateur utilisateur);
	
	Utilisateur consulterUtilisateurPseudo(String pseudo);
	
	Utilisateur consulterUtilisateurEmail(String email);
	
	void modifierUtilisateur(Utilisateur utilisateur);

	void modifierMotDePasse(Utilisateur utilisateur, NewMotDePasse newMotDePassew);

	List<Utilisateur> filtrerUtilisateurs(List<Utilisateur> utilisateurs, String recherche);

	List<Utilisateur> consulterUtilisateurs();

	void deleteByPseudo(String pseudo);

	void motDePasseOublierChanger(Utilisateur utilisateur, String newMotDePasse);
}
