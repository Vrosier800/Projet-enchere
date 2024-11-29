package fr.eni.projet.ProjetEnchere.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "UTILISATEURS")
public class Utilisateur implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@NotBlank
	@Id
	@Column(name = "pseudo") 
	private String pseudo;
	
	@NotBlank
	@Column(name = "nom") 
	private String nom;
	
	@NotBlank
	@Column(name = "prenom") 
	private String prenom;
	
	@NotBlank
	@Column(name = "email") 
	private String email;
	
	@Column(name = "telephone") 
	private String telephone;
	
	@NotBlank
	@Size(min = 8, max = 20)
	@Column(name = "mot_de_passe") 
	private String motDePasse;
	
	@Column(name = "credit") 
	private int credit;
	@Column(name = "administrateur") 
	private boolean admin;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "no_adresse") 
	private Adresse adresse;
	@ManyToMany
	private List<ArticleAVendre> listeArticles = new ArrayList<ArticleAVendre>();
	

	public Utilisateur() {
	}

	public Utilisateur(String pseudo, String nom, String prenom, String email, String telephone, String motDePasse,
			int credit, Adresse adresse) {
		super();
		this.pseudo = pseudo;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.telephone = telephone;
		this.motDePasse = motDePasse;
		this.credit = credit;
		this.adresse = adresse;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	public List<ArticleAVendre> getListeArticle() {
		return listeArticles;
	}
	public void setListeArticle(List<ArticleAVendre> listeArticle) {
		this.listeArticles = listeArticle;
	}
	public void setArticle(ArticleAVendre articleAVendre) {
		this.listeArticles.add(articleAVendre);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Utilisateur [pseudo=");
		builder.append(pseudo);
		builder.append(", nom=");
		builder.append(nom);
		builder.append(", prenom=");
		builder.append(prenom);
		builder.append(", email=");
		builder.append(email);
		builder.append(", telephone=");
		builder.append(telephone);
		builder.append(", motDePasse=");
		builder.append(motDePasse);
		builder.append(", credit=");
		builder.append(credit);
		builder.append(", admin=");
		builder.append(admin);
		builder.append(", adresse=");
		builder.append(adresse);
		builder.append(", listeArticles=");
		builder.append(listeArticles);
		builder.append("]");
		return builder.toString();
	}

}
