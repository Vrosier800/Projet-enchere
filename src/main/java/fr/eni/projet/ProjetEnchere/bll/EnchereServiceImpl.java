package fr.eni.projet.ProjetEnchere.bll;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;
import fr.eni.projet.ProjetEnchere.bo.Enchere;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;
import fr.eni.projet.ProjetEnchere.dal.ArticleAVendreDAO;
import fr.eni.projet.ProjetEnchere.dal.EnchereDAO;
import fr.eni.projet.ProjetEnchere.dal.UtilisateurDAO;
import fr.eni.projet.ProjetEnchere.exceptions.BusinessCode;
import fr.eni.projet.ProjetEnchere.exceptions.BusinessException;

@Service
public class EnchereServiceImpl implements EnchereService {

	private EnchereDAO enchereDAO;
	private ArticleAVendreDAO articleAVendreDAO;
	private UtilisateurDAO utilisateurDAO;

	public EnchereServiceImpl(EnchereDAO enchereDAO, ArticleAVendreDAO articleAVendreDAO,
			UtilisateurDAO utilisateurDAO) {
		this.enchereDAO = enchereDAO;
		this.articleAVendreDAO = articleAVendreDAO;
		this.utilisateurDAO = utilisateurDAO;
	}

	@Override
	public Enchere consulterEnchereParArticle(long id) {
		List<Enchere> encheres = enchereDAO.findAll(id);

		Enchere meilleureEnchere = encheres.stream().sorted(Comparator.comparingDouble(Enchere::getMontant).reversed())
				.findFirst().orElse(null);

		return meilleureEnchere;
	}

	@Override
	public List<Enchere> consulterEncheresParArticle(long id) {
		return enchereDAO.findAll(id);
	}

	@Override
	public List<Enchere> consulterEnchereParUtilisateur(Utilisateur utilisateur) {
		return enchereDAO.readByUser(utilisateur);
	}

	@Override
	@Transactional
	public void deleteByPseudo(String pseudo) {
		enchereDAO.deleteByPseudo(pseudo);
	}

	@Override
	@Transactional
	public void deleteByArticle(Enchere enchereParArticle) {
		int montant = enchereParArticle.getMontant();
		Utilisateur acquereur = utilisateurDAO.read(enchereParArticle.getAcquereur().getPseudo());
		enchereParArticle.getAcquereur().setCredit(acquereur.getCredit() + montant);
		utilisateurDAO.updateCredit(enchereParArticle.getAcquereur());
		enchereDAO.deleteByIdArticle(enchereParArticle.getArticleAVendre().getId());
	}

	@Override
	@Transactional
	public void creerUneEnchere(Enchere enchere) {
		BusinessException be = new BusinessException();

		boolean isValid = true;

		isValid &= validerNonVendeur(enchere, be);

		isValid &= validerUtilisateur(enchere.getAcquereur(), be);

		if (enchereDAO.countOffer(enchere) == 0) {
			isValid &= validerMontantDepart(enchere, be);
		} else {
			List<Enchere> dernieresEncheres = enchereDAO.findAll(enchere.getArticleAVendre().getId());
			Enchere derniereEnchere = dernieresEncheres.stream()
					.sorted(Comparator.comparingDouble(Enchere::getMontant).reversed()).findFirst().orElse(null);

			Utilisateur dernierAcquereur = utilisateurDAO.read(derniereEnchere.getAcquereur().getPseudo());
			isValid &= validerAcquereur(dernierAcquereur, enchere, be);
			isValid &= validerMontant(enchere, be);
		}

		isValid &= validerDateDebutEtFin(enchere.getArticleAVendre().getDateDebutEncheres(),
				enchere.getArticleAVendre().getDateFinEncheres(), be);

		isValid &= validerStatut(enchere.getArticleAVendre().getId(), be);

		if (isValid) {
			try {

				boolean premiereEnchere = premiereEnchere(enchere);

				if (!premiereEnchere) {
					Enchere premiereOffre = enchereDAO.read(enchere.getAcquereur(), enchere.getArticleAVendre());
					enchere.getAcquereur().setCredit(enchere.getAcquereur().getCredit() + premiereOffre.getMontant());
				}

				int creditAcquereur = enchere.getAcquereur().getCredit();
				int creditASoustraire = enchere.getMontant();
				enchere.getAcquereur().setCredit(creditAcquereur - creditASoustraire);

				utilisateurDAO.updateCredit(enchere.getAcquereur());
				if (premiereEnchere) {
					enchereDAO.create(enchere);
				}
				enchereDAO.update(enchere);
			} catch (DataAccessException e) {
				be.add(BusinessCode.VALIDATION_ARTICLE_ENCHERE_ERREUR);
				throw be;
			}
		} else {
			throw be;
		}
	}

	@Override
	@Transactional
	public void finaliserUneEnchere(Utilisateur vendeur, ArticleAVendre article, Enchere enchere) {
		
				vendeur.setCredit(vendeur.getCredit() + enchere.getMontant());
				
				utilisateurDAO.updateCredit(vendeur);
				
				articleAVendreDAO.updateStatut(article, 3);		
	}

	@Override
	public boolean premiereEnchere(Enchere enchere) {
		int nombreEnchere = enchereDAO.countOfferUser(enchere);
		if (nombreEnchere > 0) {
			return false;
		}
		return true;
	}

	private boolean validerNonVendeur(Enchere enchere, BusinessException be) {
		if (enchere.getAcquereur().getPseudo().equals(enchere.getArticleAVendre().getVendeur().getPseudo())) {
			be.add(BusinessCode.VALIDATION_ENCHERE_NON_VENDEUR);
			return false;
		}
		return true;
	}

	private boolean validerAcquereur(Utilisateur dernierAcquereur, Enchere enchere, BusinessException be) {
		if (dernierAcquereur.getPseudo().equals(enchere.getAcquereur().getPseudo())) {
			be.add(BusinessCode.VALIDATION_ENCHERE_UTILISATEUR_DIFFERENT);
			return false;
		}
		return true;
	}

	private boolean validerUtilisateur(Utilisateur utilisateur, BusinessException be) {
		if (utilisateur == null) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_NULL);
			return false;
		}
		return true;
	}

	private boolean validerStatut(long id, BusinessException be) {
		int statut = articleAVendreDAO.checkStatut(id);
		System.out.println(statut);
		if (statut == 0) {
			be.add(BusinessCode.VALIDATION_STATUT_ENCHERE_NON_DEBUTE);
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

	private boolean validerDateDebutEtFin(LocalDate dateDebut, LocalDate dateFin, BusinessException be) {
		LocalDate dateDuJour = LocalDate.now();
		if (dateDebut.isAfter(dateDuJour)) {
			be.add(BusinessCode.VALIDATION_ARTICLE_DATE_DEBUT_NOTBEFORE);
			return false;
		} else if (dateDuJour.isAfter(dateFin)) {
			be.add(BusinessCode.VALIDATION_ARTICLE_DATE_FIN_ENCHERE);
			return false;
		}
		return true;
	}

	private boolean validerMontant(Enchere enchere, BusinessException be) {
		int creditUtilsateur = enchere.getAcquereur().getCredit();
		int enchereUtilisateur = enchere.getMontant();
		int enchereLaPlusHaute = enchereDAO.getMaxOffer(enchere);

		if (creditUtilsateur < enchereUtilisateur) {
			be.add(BusinessCode.VALIDATION_CREDIT_INSUFFISANT);
			return false;
		} else if (enchereUtilisateur <= enchereLaPlusHaute) {
			be.add(BusinessCode.VALIDATION_CREDIT_VALEUR_TROP_PETITE);
			return false;
		}
		return true;

	}

	private boolean validerMontantDepart(Enchere enchere, BusinessException be) {
		int montant = enchere.getMontant();
		int prixDepart = enchere.getArticleAVendre().getPrixInitial();
		if (montant < prixDepart) {
			be.add(BusinessCode.VALIDATION_ENCHERE_CREDIT_VALEUR_MIN);
			return false;
		}
		return true;
	}
}
