package fr.eni.projet.ProjetEnchere.bo;

import jakarta.validation.constraints.NotBlank;

public class NewMotDePasse {

	@NotBlank
	private String nouveauMotDePasse;
	@NotBlank
	private String ancienMotDePasse;
	
	public NewMotDePasse() {
	}
	
	public NewMotDePasse(String nouveauMotDePasse, String ancienMotDePasse) {
		super();
		this.nouveauMotDePasse = nouveauMotDePasse;
		this.ancienMotDePasse = ancienMotDePasse;
	}

	public String getNouveauMotDePasse() {
		return nouveauMotDePasse;
	}

	public void setNouveauMotDePasse(String nouveauMotDePasse) {
		this.nouveauMotDePasse = nouveauMotDePasse;
	}

	public String getAncienMotDePasse() {
		return ancienMotDePasse;
	}

	public void setAncienMotDePasse(String ancienMotDePasse) {
		this.ancienMotDePasse = ancienMotDePasse;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NewMotDePasse [nouveauMotDePasse=");
		builder.append(nouveauMotDePasse);
		builder.append(", ancienMotDePasse=");
		builder.append(ancienMotDePasse);
		builder.append("]");
		return builder.toString();
	}
	
}
