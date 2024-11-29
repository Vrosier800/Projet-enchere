package fr.eni.projet.ProjetEnchere.exceptions;

public class BusinessCode {
	
	// Clefs de validation BO Utilisateur
	public static final String VALIDATION_UTILISATEUR_NULL = "validation.utilisateur.null";
	public static final String VALIDATION_UTILISATEUR_PSEUDO_BLANK = "validation.utilisateur.pseudo.blank";
	public static final String VALIDATION_UTILISATEUR_PSEUDO_NOTVALID = "validation.utilisateur.pseudo.notvalid";
	public static final String VALIDATION_UTILISATEUR_EMAIL_BLANK = "validation.utilisateur.email.blank";
	public static final String VALIDATION_UTILISATEUR_PRENOM_BLANK = "validation.utilisateur.prenom.blank";
	public static final String VALIDATION_UTILISATEUR_NOM_BLANK = "validation.utilisateur.nom.blank";
	public static final String VALIDATION_UTILISATEUR_TELEPHONE_NOTVALID = "validation.utilisateur.telephone.notvalid";
	public static final String VALIDATION_UTILISATEUR_ADRESSE_NULL = "validation.utilisateur.adresse.null";
	public static final String VALIDATION_UTILISATEUR_MDP_BLANK = "validation.utilisateur.mdp.blank";
	public static final String VALIDATION_UTILISATEUR_MDP_LENGTH = "validation.utilisateur.mdp.length";
	public static final String VALIDATION_UTILISATEUR_MDP_NOTVALID = "validation.utilisateur.mdp.notvalid";
	public static final String VALIDATION_UTILISATEUR_PSEUDO_UNIQUE = "validation.utilisateur.pseudo.unique";
	public static final String VALIDATION_UTILISATEUR_EMAIL_UNIQUE = "validation.utilisateur.email.unique";
	public static final String VALIDATION_UTILISATEUR_ANCIEN_MDP = "validation.utilisateur.ancien.mdp";
	
	public static final String BLL_UTILISATEUR_MODIF_ERREUR = "validation.utilisateur.modif.erreur";
	public static final String BLL_UTILISATEUR_CREER_ERREUR = "validation.utilisateur.creer.erreur";
	
	//Clef de validation BO Article
	public static final String VALIDATION_ARTICLE_CATEGORIE_ID_INCONNU = "validation.article.categorie.id.inconnu";
	public static final String VALIDATION_ARTICLE_NOM_NOTVALID = "validation.article.nom.notvalid";
	public static final String VALIDATION_ARTICLE_DESCRIPTION_NULL = "validation.article.description.null";
	public static final String VALIDATION_ARTICLE_CREDIT_NOTVALID = "validation.article.credit.notvalid";
	public static final String VALIDATION_ARTICLE_DATE_DEBUT_NOTAFTER = "validation.article.date.debut.notafter";
	public static final String VALIDATION_ARTICLE_DATE_DEBUT_NOTBEFORE = "validation.article.date.debut.notbefore";
	public static final String VALIDATION_ARTICLE_DATE_FIN_NOTBEFORE = "validation.article.date.fin.notbefore";
	public static final String VALIDATION_ARTICLE_ERREUR = "validation.article.erreur";
	public static final String VALIDATION_ARTICLE_CREDIT_NOTDECIMAL = "validation.article.credit.notdecimal";
	public static final String VALIDATION_ARTICLE_MODIFICATION_ERREUR = "validation.article.modification.erreur";
	public static final String VALIDATION_STATUT_ARTICLE_ANNULE = "validation.statut.article.annule";
	public static final String VALIDATION_STATUT_ARTICLE_VENDU = "validation.statut.article.vendu";
	public static final String VALIDATION_STATUT_ENCHERE_EN_COURS = "validation.statut.enchere.en.cours";
	
	//Clefs de validation BO Enchere
	public static final String VALIDATION_CREDIT_INSUFFISANT = "validation.credit.insuffisant";
	public static final String VALIDATION_CREDIT_VALEUR_TROP_PETITE = "validation.credit.valeur.trop.petite";
	public static final String VALIDATION_STATUT_ENCHERE_NON_DEBUTE = "validation.statut.enchere.non.debute";
	public static final String VALIDATION_ARTICLE_ENCHERE_ERREUR = "validation.article.enchere.erreur";
	public static final String VALIDATION_ARTICLE_DATE_FIN_ENCHERE = "validation.article.date.fin.enchere";
	public static final String VALIDATION_ENCHERE_CREDIT_VALEUR_MIN = "validation.credit.valeur.min";
	public static final String VALIDATION_ENCHERE_MOINS_HAUTE = "validation.enchere.moins.haute";
	public static final String VALIDATION_ENCHERE_UTILISATEUR_DIFFERENT = "validation.enchere.utilisateur.different";
	public static final String VALIDATION_ENCHERE_NON_VENDEUR = "validation.enchere.non.vendeur";
	
	//Clefs de validation BO Images
	public static final String VALIDATION_IMAGE_ID_NULL = "validation.image.id.null";
	public static final String VALIDATION_IMAGE_ARTICLE_NULL = "validation.image.article.null";
	public static final String VALIDATION_IMAGE_NOM_NULL = "validation.image.nom.null";
	public static final String VALIDATION_IMAGE_NOM_TROPLONG= "validation.image.nom.troplong";
	
	//Clefs de validation retrait enchère
	public static final String VALIDATION_ARTICLE_RETRAIT = "validation.article.retrait";
	public static final String VALIDATION_RETRAIT_MONTANT = "validation.retrait.montant";
	public static final String VALIDATION_RETRAIT_VENDEUR = "validation.retrait.vendeur";
	public static final String VALIDATION_RETRAIT_ERREUR = "validation.retrait.erreur";
}
