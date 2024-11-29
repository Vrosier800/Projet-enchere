package fr.eni.projet.ProjetEnchere.bll;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.projet.ProjetEnchere.bo.Adresse;
import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;
import fr.eni.projet.ProjetEnchere.bo.NewMotDePasse;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;
import fr.eni.projet.ProjetEnchere.dal.AdresseDAO;
import fr.eni.projet.ProjetEnchere.dal.ArticleAVendreDAO;
import fr.eni.projet.ProjetEnchere.dal.UtilisateurDAO;
import fr.eni.projet.ProjetEnchere.exceptions.BusinessCode;
import fr.eni.projet.ProjetEnchere.exceptions.BusinessException;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {
	
	UtilisateurDAO utilisateurDAO;
	AdresseDAO adresseDAO;
	ArticleAVendreDAO articleAVendreDAO;

	public UtilisateurServiceImpl(UtilisateurDAO utilisateurDAO, AdresseDAO adresseDAO,
			ArticleAVendreDAO articleAVendreDAO) {
		super();
		this.utilisateurDAO = utilisateurDAO;
		this.adresseDAO = adresseDAO;
		this.articleAVendreDAO = articleAVendreDAO;
	}

	@Override
	@Transactional
	public void creerUtilisateur(Utilisateur utilisateur) {
		BusinessException be = new BusinessException();
		boolean isValid = true;
		isValid &= validerUtilisateur(utilisateur, be);
		isValid &= validerPseudo(utilisateur.getPseudo(), be);
		isValid &= validerEmail(utilisateur.getEmail(), be);
		isValid &= validerNom(utilisateur.getNom(), be);
		isValid &= validerPrenom(utilisateur.getPrenom(), be);
		isValid &= validerTelephone(utilisateur.getTelephone(), be);
		isValid &= validerAdresse(utilisateur.getAdresse(), be);
		isValid &= validerMotDePasse(utilisateur.getMotDePasse(), be);
		
		if (isValid) {
			try {
				List<Adresse> adresses = adresseDAO.findAll();
				Adresse adresse = utilisateur.getAdresse();
				long newId;
				
				Optional<Adresse> isExist = adresses.stream()
						.filter(a -> utilisateur.getAdresse().getRue().equals(a.getRue())
								&& utilisateur.getAdresse().getVille().equals(a.getVille())
								&& utilisateur.getAdresse().getCodePostal().equals(a.getCodePostal()))
						.findAny();
				if (isExist.isEmpty()) {
					adresseDAO.create(adresse);
					Optional<Adresse> newAdresse = adresseDAO.findAll().stream()
							.filter(a -> utilisateur.getAdresse().getRue().equals(a.getRue())
									&& utilisateur.getAdresse().getVille().equals(a.getVille())
									&& utilisateur.getAdresse().getCodePostal().equals(a.getCodePostal()))
							.findAny();
					newId = newAdresse.get().getId();
					adresse.setId(newId);
					utilisateur.setAdresse(adresse);
				} else {
					newId = isExist.get().getId();
					adresse.setId(newId);
					utilisateur.setAdresse(adresse);
				}
				if (utilisateur.getTelephone().length() == 0) utilisateur.setTelephone(null);
				utilisateur.setCredit(10);
				
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
				String EncodedPassword = encoder.encode(utilisateur.getMotDePasse());
				utilisateur.setMotDePasse("{bcrypt}" + EncodedPassword);
				
				utilisateurDAO.create(utilisateur);
			} catch (DataAccessException e) {
				be.add(BusinessCode.BLL_UTILISATEUR_CREER_ERREUR);
				throw be;
			}
		} else {
			throw be;
		}
	}

	@Override
	public Utilisateur consulterUtilisateurPseudo(String pseudo) {
		Utilisateur utilisateur = utilisateurDAO.read(pseudo);

		if (utilisateur != null) {
			chargeradresse1Utilisateur(utilisateur);

			List<ArticleAVendre> listeArticles = articleAVendreDAO.findAllByPseudo(pseudo);
			if (listeArticles != null) {
				utilisateur.setListeArticle(listeArticles);
			}
		}

		return utilisateur;
	}
	
	@Override
	@Transactional
	public void deleteByPseudo(String pseudo) {
		utilisateurDAO.deleteByPseudo(pseudo);
	}

	@Override
	public Utilisateur consulterUtilisateurEmail(String email) {
		Utilisateur utilisateur = utilisateurDAO.readEmail(email);

		if (utilisateur != null) {
			chargeradresse1Utilisateur(utilisateur);
			
			List<ArticleAVendre> listeArticles = articleAVendreDAO.findAllByPseudo(utilisateur.getPseudo());
			if (listeArticles != null) {
				utilisateur.setListeArticle(listeArticles);
			}
		}

		return utilisateur;
	}
	
	@Override
	public List<Utilisateur> consulterUtilisateurs() {
		return utilisateurDAO.findAll();
	}
	
	@Override
	public List<Utilisateur> filtrerUtilisateurs(List<Utilisateur> utilisateurs, String recherche) {
		return utilisateurs.stream()
				.filter(a -> recherche.isEmpty() || a.getPseudo().toLowerCase().contains(recherche.toLowerCase()))
				.collect(Collectors.toList());
	}
	
	private void chargeradresse1Utilisateur(Utilisateur utilisateur) {
		Adresse adresse = adresseDAO.read(utilisateur.getAdresse().getId());

		utilisateur.setAdresse(adresse);
	}

	@Override
	@Transactional
	public void modifierUtilisateur(Utilisateur utilisateur) {
		BusinessException be = new BusinessException();
		boolean isValid = true;
		isValid &= validerUtilisateur(utilisateur, be);
		isValid &= validerEmailModifier(utilisateur.getEmail(), be);
		isValid &= validerNom(utilisateur.getNom(), be);
		isValid &= validerPrenom(utilisateur.getPrenom(), be);
		isValid &= validerTelephone(utilisateur.getTelephone(), be);
		isValid &= validerAdresse(utilisateur.getAdresse(), be);
		
		if (isValid) {
			try {
				List<Adresse> adresses = adresseDAO.findAll();
				Adresse adresse = utilisateur.getAdresse();
				long newId;
				
				Optional<Adresse> isExist = adresses.stream()
						.filter(a -> utilisateur.getAdresse().getRue().equals(a.getRue())
								&& utilisateur.getAdresse().getVille().equals(a.getVille())
								&& utilisateur.getAdresse().getCodePostal().equals(a.getCodePostal()))
						.findAny();
				if (isExist.isEmpty()) {
					adresseDAO.create(adresse);
					Optional<Adresse> newAdresse = adresseDAO.findAll().stream()
							.filter(a -> utilisateur.getAdresse().getRue().equals(a.getRue())
									&& utilisateur.getAdresse().getVille().equals(a.getVille())
									&& utilisateur.getAdresse().getCodePostal().equals(a.getCodePostal()))
							.findAny();
					newId = newAdresse.get().getId();
					adresse.setId(newId);
					utilisateur.setAdresse(adresse);
				} else {
					newId = isExist.get().getId();
					adresse.setId(newId);
					utilisateur.setAdresse(adresse);
				}
				if (utilisateur.getTelephone().length() == 0) utilisateur.setTelephone(null);
				
				utilisateurDAO.update(utilisateur);
				
			} catch (DataAccessException e) {
				be.add(BusinessCode.BLL_UTILISATEUR_MODIF_ERREUR);
				throw be;
			}
		} else {
			throw be;
		}
	}
	
	@Override
	@Transactional
	public void modifierMotDePasse(Utilisateur utilisateur, NewMotDePasse newMotDePasse) {
		BusinessException be = new BusinessException();
		boolean isValid = true;
		isValid &= validerAncienMotDePasse(utilisateur, newMotDePasse.getAncienMotDePasse(), be);
		isValid &= validerMotDePasse(newMotDePasse.getNouveauMotDePasse(), be);
		
		if (isValid) {
			try {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
				String EncodedPassword = encoder.encode(newMotDePasse.getNouveauMotDePasse());
				utilisateur.setMotDePasse("{bcrypt}" + EncodedPassword);
				utilisateurDAO.updateMotDePasse(utilisateur);
			} catch (DataAccessException e) {
				be.add(BusinessCode.BLL_UTILISATEUR_MODIF_ERREUR);
				throw be;
			}
		} else {
			throw be;
		}
	}
	
	@Override
	@Transactional
	public void motDePasseOublierChanger(Utilisateur utilisateur, String newMotDePasse) {
		BusinessException be = new BusinessException();
		try {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
			String EncodedPassword = encoder.encode(newMotDePasse);
			utilisateur.setMotDePasse("{bcrypt}" + EncodedPassword);
			utilisateurDAO.updateMotDePasse(utilisateur);
		} catch (DataAccessException e) {
			be.add(BusinessCode.BLL_UTILISATEUR_MODIF_ERREUR);
			throw be;
		}
	}

	private boolean validerAncienMotDePasse(Utilisateur utilisateur, String ancienMotDepasse, BusinessException be) {
		if (ancienMotDepasse == null || ancienMotDepasse.isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_MDP_BLANK);
			return false;
		}
		Utilisateur utilisateurEnBase = utilisateurDAO.readMDP(utilisateur.getPseudo());
		String motDePasseEnBase = utilisateurEnBase.getMotDePasse().replace("{bcrypt}", "");
		
		BCryptPasswordEncoder b = new BCryptPasswordEncoder();
		if (!b.matches(ancienMotDepasse, motDePasseEnBase)) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_ANCIEN_MDP);
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
	
	private boolean validerPseudo(String pseudo, BusinessException be) {
		try {
			if (pseudo == null || pseudo.isBlank()) {
				be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_BLANK);
				return false;
			}
			if (!isValidString(pseudo)) {
				be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_NOTVALID);
				return false;
		    }
			boolean pseudoExist = utilisateurDAO.findPseudo(pseudo);
			if (pseudoExist) {
				be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_UNIQUE);
				return false;
			}
		} catch (DataAccessException e) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PSEUDO_UNIQUE);
			return false;
		}
		return true;
	}
	
	public static boolean isValidString(String str) {
        return str.matches("^[a-zA-Z0-9_]*$");
    }
	
	private boolean validerEmail(String email, BusinessException be) {
		try {
			if (email == null || email.isBlank()) {
				be.add(BusinessCode.VALIDATION_UTILISATEUR_EMAIL_BLANK);
				return false;
			}
			boolean emailExist = utilisateurDAO.findEmail(email);
			if (emailExist) {
				be.add(BusinessCode.VALIDATION_UTILISATEUR_EMAIL_UNIQUE);
				return false;
			}
		} catch (DataAccessException e) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_EMAIL_UNIQUE);
			return false;
		}
		return true;
	}
	
	private boolean validerEmailModifier(String email, BusinessException be) {
		try {
			if (email == null || email.isBlank()) {
				be.add(BusinessCode.VALIDATION_UTILISATEUR_EMAIL_BLANK);
				return false;
			}
		} catch (DataAccessException e) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_EMAIL_UNIQUE);
			return false;
		}
		return true;
	}
	
	private boolean validerPrenom(String prenom, BusinessException be) {
		if (prenom == null || prenom.isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_PRENOM_BLANK);
			return false;
		}
		return true;
	}

	private boolean validerNom(String nom, BusinessException be) {
		if (nom == null || nom.isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_NOM_BLANK);
			return false;
		}
		return true;
	}
	
	private boolean validerTelephone(String telephone, BusinessException be) {
		if (telephone == null || telephone.isBlank()) {
			return true;
		}
		if (!isValidTelephone(telephone)) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_TELEPHONE_NOTVALID);
			return false;
	    }
		return true;
	}
	
	public static boolean isValidTelephone(String str) {
        return str.matches("^(?:(?:\\+|00)33[\\s.-]{0,3}(?:\\(0\\)[\\s.-]{0,3})?|0)[1-9](?:(?:[\\s.-]?\\d{2}){4}|\\d{2}(?:[\\s.-]?\\d{3}){2})$");
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
	
	private boolean validerMotDePasse(String motDePasse, BusinessException be) {
		if (motDePasse == null || motDePasse.isBlank()) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_MDP_BLANK);
			return false;
		}
		if (motDePasse.length() < 8 || motDePasse.length() > 20) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_MDP_LENGTH);
			return false;
		}
		if (!isValidMotDePasse(motDePasse)) {
			be.add(BusinessCode.VALIDATION_UTILISATEUR_MDP_NOTVALID);
			return false;
	    }
		return true;
	}
	
	public static boolean isValidMotDePasse(String str) {
        return str.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }

}
