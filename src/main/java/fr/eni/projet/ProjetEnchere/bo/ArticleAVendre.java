package fr.eni.projet.ProjetEnchere.bo;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "ARTICLES_A_VENDRE")
public class ArticleAVendre implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue
	@Column(name = "no_article") 
	private long id;
	
	@NotBlank
	@Column(name = "nom_article") 
	private String nom;
	
	@NotBlank
	@Column(name = "description") 
	private String description;
	
	@NotNull
	@Column(name = "date_debut_encheres") 
	private LocalDate dateDebutEncheres;
	
	@NotNull
	@Column(name = "date_fin_encheres") 
	private LocalDate dateFinEncheres;
	
	@Column(name = "statut_enchere")
	private int statut = 0 ; 
	
	@NotNull
	@Column(name = "prix_initial") 
	private int prixInitial;
	@Column(name = "prix_vente") 
	private Integer prixVente;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "no_adresse_retrait") 
	private Adresse retrait;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "no_categorie") 
	private Categorie categorie;
	
	@ManyToOne
	@JoinColumn(name = "id_utilisateur") 
	private Utilisateur vendeur;
	
	@ManyToOne
	private Image image;

	public ArticleAVendre() {
	}

	public ArticleAVendre(String nom, String description, LocalDate dateDebutEncheres, LocalDate dateFinEncheres, int prixInitial, Adresse retrait, Categorie categorie, Utilisateur vendeur) {
		this.nom = nom;
		this.description = description;
		this.dateDebutEncheres = dateDebutEncheres;
		this.dateFinEncheres = dateFinEncheres;
		this.prixInitial = prixInitial;
		this.retrait = retrait;
		this.categorie = categorie;
		this.vendeur = vendeur;
		vendeur.setArticle(this);
	}

	public ArticleAVendre(long id, String nom, String description, LocalDate dateDebutEncheres,
			LocalDate dateFinEncheres, int statut, int prixInitial, int prixVente, Adresse retrait, Categorie categorie,
			Utilisateur vendeur) {
		this.id = id;
		this.nom = nom;
		this.description = description;
		this.dateDebutEncheres = dateDebutEncheres;
		this.dateFinEncheres = dateFinEncheres;
		this.statut = statut;
		this.prixInitial = prixInitial;
		this.prixVente = prixVente;
		this.retrait = retrait;
		this.categorie = categorie;
		this.vendeur = vendeur;
		vendeur.setArticle(this);
	}

	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDateDebutEncheres() {
		return dateDebutEncheres;
	}

	public void setDateDebutEncheres(LocalDate dateDebutEncheres) {
		this.dateDebutEncheres = dateDebutEncheres;
	}

	public LocalDate getDateFinEncheres() {
		return dateFinEncheres;
	}

	public void setDateFinEncheres(LocalDate dateFinEncheres) {
		this.dateFinEncheres = dateFinEncheres;
	}

	public int getStatut() {
		return statut;
	}

	public void setStatut(int statut) {
		this.statut = statut;
	}

	public int getPrixInitial() {
		return prixInitial;
	}

	public void setPrixInitial(int prixInitial) {
		this.prixInitial = prixInitial;
	}

	public int getPrixVente() {
		return prixVente;
	}


	 public void setPrixVente(Integer prixVente) {
	        if (prixVente != null) {
	            this.prixVente = prixVente;
	        } else {
	            this.prixVente = 0; // Valeur par défaut
	        }
	    }

	public Utilisateur getVendeur() {
		return vendeur;
	}

	public void setVendeur(Utilisateur vendeur) {
		this.vendeur = vendeur;
	}

	public Adresse getRetrait() {
		return retrait;
	}

	public void setRetrait(Adresse retrait) {
		this.retrait = retrait;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ArticleAVendre [id=");
		builder.append(id);
		builder.append(", nom=");
		builder.append(nom);
		builder.append(", description=");
		builder.append(description);
		builder.append(", dateDebutEncheres=");
		builder.append(dateDebutEncheres);
		builder.append(", dateFinEncheres=");
		builder.append(dateFinEncheres);
		builder.append(", statut=");
		builder.append(statut);
		builder.append(", prixInitial=");
		builder.append(prixInitial);
		builder.append(", prixVente=");
		builder.append(prixVente);
		builder.append(", retrait=");
		builder.append(retrait);
		builder.append(", categorie=");
		builder.append(categorie);
		builder.append("]");
		return builder.toString();
	}
	
}
