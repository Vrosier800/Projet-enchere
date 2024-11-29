package fr.eni.projet.ProjetEnchere.bll;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.projet.ProjetEnchere.bo.Adresse;
import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;
import fr.eni.projet.ProjetEnchere.bo.Categorie;
import fr.eni.projet.ProjetEnchere.bo.Enchere;
import fr.eni.projet.ProjetEnchere.bo.Image;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;
import fr.eni.projet.ProjetEnchere.dal.AdresseDAO;
import fr.eni.projet.ProjetEnchere.dal.ArticleAVendreDAO;
import fr.eni.projet.ProjetEnchere.dal.CategorieDAO;
import fr.eni.projet.ProjetEnchere.dal.EnchereDAO;
import fr.eni.projet.ProjetEnchere.dal.UtilisateurDAO;
import fr.eni.projet.ProjetEnchere.exceptions.BusinessCode;
import fr.eni.projet.ProjetEnchere.exceptions.BusinessException;
import fr.eni.projet.ProjetEnchere.repository.ArticleAVendreRepository;

@Service
public class ArticleAVendreServiceImpl implements ArticleAVendreService {

	private ArticleAVendreDAO articleAVendreDAO;
	private AdresseDAO adresseDAO;
	private CategorieDAO categorieDAO;
	private UtilisateurDAO utilisateurDAO;
	private EnchereDAO enchereDAO;
	private ArticleAVendreRepository articleRepository;
	private ImagesService imagesService;

	public ArticleAVendreServiceImpl(ArticleAVendreDAO articleAVendreDAO, AdresseDAO adresseDAO,
			CategorieDAO categorieDAO, UtilisateurDAO utilisateurDAO, EnchereDAO enchereDAO, 
			ArticleAVendreRepository articleRepository, ImagesService imagesService) {
		this.articleAVendreDAO = articleAVendreDAO;
		this.adresseDAO = adresseDAO;
		this.categorieDAO = categorieDAO;
		this.utilisateurDAO = utilisateurDAO;
		this.enchereDAO = enchereDAO;
		this.articleRepository =articleRepository;
		this.imagesService = imagesService;
	}
	
	@Override
	public Page<ArticleAVendre> getArticles(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

	@Override
	public List<ArticleAVendre> consulterArticles() {
		return articleAVendreDAO.findAll();
	}

	@Override
	public List<ArticleAVendre> consulterArticlesByPseudo(String pseudo) {
		return articleAVendreDAO.findAllByPseudo(pseudo);
	}

	@Override
	@Transactional
	public void deleteByPseudo(String pseudo) {
		articleAVendreDAO.deleteByPseudo(pseudo);
	}

	@Override
	public List<ArticleAVendre> consulterArticlesMembreEnSession() {
		return articleAVendreDAO.findAllUser();
	}

	@Override
	public List<ArticleAVendre> consulterArticlesParMembreEnSessionEtStatut(String pseudo, int statut){
		
		List<ArticleAVendre> allActive = articleAVendreDAO.findByUserAndStatut(pseudo, statut);

		for (ArticleAVendre active : allActive) {
		
			try {
				Image image = imagesService.consulterImageParArticle(active.getId());
				active.setImage(image);
			} catch (Exception e) {

			}
		}
		return allActive;
	}
	
	@Override
	public List<ArticleAVendre> consulterArticlesActifs() {
		
		List<ArticleAVendre> allActive = articleAVendreDAO.findAllActive();

		for (ArticleAVendre active : allActive) {
		
			try {
				Image image = imagesService.consulterImageParArticle(active.getId());
				active.setImage(image);
			} catch (Exception e) {

			}
		}
		
		return allActive;
	}

	@Override
	public ArticleAVendre consulterArticleParID(long id) {
		ArticleAVendre article = articleAVendreDAO.read(id);
		Adresse adresse = adresseDAO.read(article.getRetrait().getId());
		Categorie categorie = categorieDAO.read(article.getCategorie().getId());
		Utilisateur vendeur = utilisateurDAO.read(article.getVendeur().getPseudo());

		article.setRetrait(adresse);
		article.setCategorie(categorie);
		article.setVendeur(vendeur);

		return article;
	}

	@Override
	public List<ArticleAVendre> consulterArticleActifsParEnchere(Utilisateur utilisateur) {
		List<ArticleAVendre> articles = new ArrayList<>();
		List<Enchere> encheres = enchereDAO.readByUser(utilisateur);

		for (Enchere enchere : encheres) {
			ArticleAVendre article = articleAVendreDAO.read(enchere.getArticleAVendre().getId());

			if (article.getStatut() == 1) {
				if (!article.getVendeur().getPseudo().equals(utilisateur.getPseudo())) {
					articles.add(article);
				}
			}
		}
		

		for (ArticleAVendre active : articles) {
		
			try {
				Image image = imagesService.consulterImageParArticle(active.getId());
				active.setImage(image);
			} catch (Exception e) {

			}
		}
		
		return articles;
	}

	@Override
	public List<ArticleAVendre> consulterArticleTermineeParEnchere(Utilisateur utilisateur) {
		List<ArticleAVendre> articles = new ArrayList<>();
		List<Enchere> encheres = enchereDAO.readByUser(utilisateur);

		for (Enchere enchere : encheres) {
			ArticleAVendre article = articleAVendreDAO.read(enchere.getArticleAVendre().getId());

			if (article.getStatut() == 2 || article.getStatut() == 3) {
				if (!article.getVendeur().getPseudo().equals(utilisateur.getPseudo())) {
					articles.add(article);
				}
			}
		}

		return articles;
	}

	@Override
	public List<ArticleAVendre> consulterArticleActifsParPseudo(String pseudo) {
		return articleAVendreDAO.findAllActiveByUser(pseudo);
	}

	@Override
	public List<ArticleAVendre> consulterArticleGagneParPseudo(String pseudo) {
		return articleAVendreDAO.findAllCloseByUser(pseudo);
	}

	@Override
	@Transactional
	public void creerArticle(ArticleAVendre article) {
		BusinessException be = new BusinessException();
		boolean isValid = true;
		isValid &= validerAdresse(article.getRetrait(), be);
		isValid &= validerUtilisateur(article.getVendeur(), be);
		isValid &= validerCategorie(article.getCategorie(), be);
		isValid &= validerCredit(article.getPrixInitial(), be);
		isValid &= validerDescription(article.getDescription(), be);
		isValid &= validerNom(article.getNom(), be);
		isValid &= validerDateDebutEtFin(article.getDateDebutEncheres(), article.getDateFinEncheres(), be);

		if (isValid) {
			try {
				articleAVendreDAO.create(article);
			} catch (DataAccessException e) {
				be.add(BusinessCode.VALIDATION_ARTICLE_ERREUR);
				throw be;
			}
		} else {
			throw be;
		}
	}

	@Override
	@Transactional
	public void annulerArticle(ArticleAVendre article) {
		int annuler = 100;
		BusinessException be = new BusinessException();
		boolean isValid = true;
		isValid &= validerDateDebut(article.getDateDebutEncheres(), be);
		isValid &= validerStatut(article.getId(), be);

		if (isValid) {
			try {
				articleAVendreDAO.updateStatut(article, annuler);
			} catch (DataAccessException e) {
				be.add(BusinessCode.VALIDATION_ARTICLE_MODIFICATION_ERREUR);
				throw be;
			}
		} else {
			throw be;
		}
	}

	@Override
	@Transactional
	public void modifierArticle(ArticleAVendre article) {

		BusinessException be = new BusinessException();
		boolean isValid = true;
		ArticleAVendre articleEnBase = articleAVendreDAO.read(article.getId());
		isValid &= validerDateDebut(articleEnBase.getDateDebutEncheres(), be);
		isValid &= validerStatut(articleEnBase.getId(), be);
		isValid &= validerAdresse(article.getRetrait(), be);
		isValid &= validerUtilisateur(article.getVendeur(), be);
		isValid &= validerCategorie(article.getCategorie(), be);
		isValid &= validerCredit(article.getPrixInitial(), be);
		isValid &= validerDescription(article.getDescription(), be);
		isValid &= validerNom(article.getNom(), be);
		isValid &= validerDateDebutEtFin(article.getDateDebutEncheres(), article.getDateFinEncheres(), be);
		if (isValid) {
			try {
				articleAVendreDAO.update(article);
			} catch (DataAccessException e) {
				be.add(BusinessCode.VALIDATION_ARTICLE_MODIFICATION_ERREUR);
				throw be;
			}
		} else {
			throw be;
		}
	}

	private boolean validerUtilisateur(Utilisateur utilisateur, BusinessException be) {
		if (utilisateur == null) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_NULL);
			return false;
		}
		return true;
	}

	private boolean validerAdresse(Adresse adresse, BusinessException be) {
		if (adresse == null) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_ADRESSE_NULL);
			return false;
		}
		if (adresse.getRue().isEmpty()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_ADRESSE_NULL);
			return false;
		}
		if (adresse.getVille().isEmpty()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_ADRESSE_NULL);
			return false;
		}
		if (adresse.getCodePostal().isEmpty()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_ADRESSE_NULL);
			return false;
		}
		return true;
	}

	private boolean validerDateDebutEtFin(LocalDate dateDebut, LocalDate dateFin, BusinessException be) {
		LocalDate dateDuJour = LocalDate.now();
		if (dateDebut.isAfter(dateFin)) {
			be.add(BusinessCode.VALIDATION_ARTICLE_DATE_DEBUT_NOTAFTER);
			return false;
		} else if (dateDuJour.isAfter(dateDebut)) {
			be.add(BusinessCode.VALIDATION_ARTICLE_DATE_DEBUT_NOTBEFORE);
			return false;
		} else if (dateDuJour.isAfter(dateFin)) {
			be.add(BusinessCode.VALIDATION_ARTICLE_DATE_FIN_NOTBEFORE);
			return false;
		}
		return true;
	}

	private boolean validerDateDebut(LocalDate date, BusinessException be) {
		LocalDate dateDuJour = LocalDate.now();
		if (dateDuJour.isAfter(date)) {
			return false;
		}
		return true;
	}

	private boolean validerCredit(int credit, BusinessException be) {
		if (credit < 0) {
			be.add(BusinessCode.VALIDATION_ARTICLE_CREDIT_NOTVALID);
			return false;
		} else {
			if ((credit % 1) != 0) {
				be.add(BusinessCode.VALIDATION_ARTICLE_CREDIT_NOTDECIMAL);
				return false;
			}
		}
		return true;
	}

	private boolean validerDescription(String description, BusinessException be) {
		if (description.isEmpty()) {
			be.add(BusinessCode.VALIDATION_ARTICLE_DESCRIPTION_NULL);
			return false;
		}
		return true;
	}

	private boolean validerNom(String nom, BusinessException be) {
		if (nom.length() < 4) {
			be.add(BusinessCode.VALIDATION_ARTICLE_NOM_NOTVALID);
			return false;
		}
		return true;
	}

	private boolean validerCategorie(Categorie categorie, BusinessException be) {
		if (categorie == null) {
			be.add(BusinessCode.VALIDATION_ARTICLE_CATEGORIE_ID_INCONNU);
			return false;
		}
		try {
			Categorie categorieEnBase = categorieDAO.read(categorie.getId());
			if (categorieEnBase == null) {
				be.add(BusinessCode.VALIDATION_ARTICLE_CATEGORIE_ID_INCONNU);
				return false;
			}
		} catch (DataAccessException e) {
			be.add(BusinessCode.VALIDATION_ARTICLE_CATEGORIE_ID_INCONNU);
			return false;
		}
		return true;
	}

	private boolean validerStatut(long id, BusinessException be) {
		int statut = articleAVendreDAO.checkStatut(id);
		System.out.println(statut);
		if (statut == 1) {
			be.add(BusinessCode.VALIDATION_STATUT_ENCHERE_EN_COURS);
			return false;
		} else if (statut == 2) {
			be.add(BusinessCode.VALIDATION_STATUT_ARTICLE_VENDU);
			return false;
		} else if (statut == 100) {
			be.add(BusinessCode.VALIDATION_STATUT_ARTICLE_ANNULE);
			return false;
		}
		return true;
	}

	@Override
	public List<ArticleAVendre> filtrerArticles(List<ArticleAVendre> articles, String categorie, String recherche) {
		return articles.stream()
				.filter(a -> categorie.equals("all") || categorie.equals("" + a.getCategorie().getId() + ""))
				.filter(a -> recherche.isEmpty() || a.getNom().toLowerCase().contains(recherche.toLowerCase()))
				.collect(Collectors.toList());
	}

	@Override
	public void miseAJourStatut() {
		List<ArticleAVendre> articles = articleAVendreDAO.findAll();
		for (ArticleAVendre article : articles) {
			int statut = articleAVendreDAO.checkStatut(article.getId());
			switch (statut) {
			case 0:
				LocalDate debutEnchere = article.getDateDebutEncheres();
				if (LocalDate.now().isAfter(debutEnchere)) {
					articleAVendreDAO.updateStatut(article, 1);
				}
				break;
			case 1:
				LocalDate finEnchere = article.getDateFinEncheres();
				if (LocalDate.now().isAfter(finEnchere)) {
					List<Enchere> encheres = enchereDAO.findAll(article.getId());
					if (encheres.size()>0) {
					Enchere meilleureEnchere = encheres.stream().sorted(Comparator.comparingDouble(Enchere::getMontant).reversed())
							.findFirst().orElse(null);
						articleAVendreDAO.updateStatut(article, 2);
						articleAVendreDAO.updatePrixVente(article, meilleureEnchere.getMontant());
						for (Enchere enchere : encheres) {
							if (enchere.getMontant() < enchereDAO.getMaxOffer(enchere)) {
								Utilisateur acquereur = utilisateurDAO.read(enchere.getAcquereur().getPseudo());
								acquereur.setCredit(acquereur.getCredit() + enchere.getMontant());
								utilisateurDAO.updateCredit(acquereur);
								enchereDAO.delete(enchere);
							}
						}
					} else {
						articleAVendreDAO.updateStatut(article, 100);
					}
				}
				break;
			case 3:
				break;
			case 100:
				break;
			}
		}
	}
}