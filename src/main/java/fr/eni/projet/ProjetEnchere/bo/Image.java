package fr.eni.projet.ProjetEnchere.bo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Image {

	@Id
	@GeneratedValue
	@NotNull
	private long id;
	
	@NotBlank
	private String nom;
	
	@NotBlank
	@ManyToOne
	private ArticleAVendre articleAVendre;
	
	
	public Image() {
	}


	public Image(String nom, ArticleAVendre articleAVendre) {
		this.nom = nom;
		this.articleAVendre = articleAVendre;
	}


	public Image(long id, String nom, ArticleAVendre articleAVendre) {
		this.id = id;
		this.nom = nom;
		this.articleAVendre = articleAVendre;
		articleAVendre.setImage(this);
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


	public ArticleAVendre getArticleAVendre() {
		return articleAVendre;
	}


	public void setArticleAVendre(ArticleAVendre articleAVendre) {
		this.articleAVendre = articleAVendre;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Image [id=");
		builder.append(id);
		builder.append(", nom=");
		builder.append(nom);
		builder.append(", articleAVendre=");
		builder.append(articleAVendre);
		builder.append("]");
		return builder.toString();
	}
	
}
