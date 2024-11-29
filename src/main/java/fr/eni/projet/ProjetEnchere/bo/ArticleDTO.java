package fr.eni.projet.ProjetEnchere.bo;

import java.time.format.DateTimeFormatter;

public class ArticleDTO {
	
	private long id;
	private String title;
    private String start;
    private String end;
    private String description;
    Utilisateur utilisateur;
    String acquereur;
    ArticleAVendre article;
    int prixInitial;
    int enchereMax;
    
	public ArticleDTO(ArticleAVendre article ,Utilisateur utilisateur, Enchere enchere ) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.id= article.getId();
		this.prixInitial=article.getPrixInitial();
		this.title = article.getNom();
		this.start = article.getDateDebutEncheres().format(formatter);
		this.end = article.getDateFinEncheres().format(formatter);
		this.description = article.getDescription();
		this.utilisateur = utilisateur;
		if (enchere.getAcquereur() != null) {
		    this.acquereur = enchere.getAcquereur().getPseudo();
		} else {
		    this.acquereur = null;  // ou toute autre valeur par défaut
		}
		this.article = article;
		this.enchereMax = enchere.getMontant();
	}
	
	
	
	
	
	
	public int getPrixInitial() {
		return prixInitial;
	}
	public void setPrixInitial(int prixInitial) {
		this.prixInitial = prixInitial;
	}
	public String getAcquereur() {
		return acquereur;
	}
	public void setAcquereur(String acquereur) {
		this.acquereur = acquereur;
	}
	public int getEnchereMax() {
		return enchereMax;
	}
	public void setEnchereMax(int enchereMax) {
		this.enchereMax = enchereMax;
	}
	public Utilisateur getUtilisateur() {
		return utilisateur;
	}
	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}
	public ArticleAVendre getArticle() {
		return article;
	}
	public void setArticle(ArticleAVendre article) {
		this.article = article;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}   
    
}
