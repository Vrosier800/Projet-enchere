package fr.eni.projet.ProjetEnchere.bo;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class Enchere {
	
	
	private LocalDate date;
	@NotNull
	private int montant;
	
	private Utilisateur acquereur;

	private ArticleAVendre articleAVendre;

	public Enchere() {
	}

	public Enchere(LocalDate date, int montant, Utilisateur acquereur, ArticleAVendre articleAVendre) {
		this.date = date;
		this.montant = montant;
		this.acquereur = acquereur;
		this.articleAVendre = articleAVendre;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getMontant() {
		return montant;
	}

	public void setMontant(int montant) {
		this.montant = montant;
	}

	public Utilisateur getAcquereur() {
		return acquereur;
	}

	public void setAcquereur(Utilisateur acquereur) {
		this.acquereur = acquereur;
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
		builder.append("Enchere [date=");
		builder.append(date);
		builder.append(", montant=");
		builder.append(montant);
		builder.append(", acquereur=");
		builder.append(acquereur);
		builder.append(", articleAVendre=");
		builder.append(articleAVendre);
		builder.append("]");
		return builder.toString();
	}
	
}
