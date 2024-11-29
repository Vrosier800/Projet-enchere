package fr.eni.projet.ProjetEnchere.bll;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.projet.ProjetEnchere.bo.Image;
import fr.eni.projet.ProjetEnchere.dal.ImagesDAO;
import fr.eni.projet.ProjetEnchere.exceptions.BusinessCode;
import fr.eni.projet.ProjetEnchere.exceptions.BusinessException;

@Service
public class ImagesServiceImpl implements ImagesService {

	private ImagesDAO imagesDAO;

	public ImagesServiceImpl(ImagesDAO imagesDAO) {
		this.imagesDAO = imagesDAO;
	}
	
	@Override
	public Image consulterImageParArticle(long id) {
		Image image = imagesDAO.consulterImageParArticle(id);
		
		return image;
	}

	@Transactional
	@Override
	public void ajouterImage(Image image) {
		BusinessException be = new BusinessException();
		boolean isValid = true;
		isValid &= validerArticle(image.getArticleAVendre().getId(), be);
		isValid &= validerNom(image.getNom(), be);

		if (isValid) {
			try {
				imagesDAO.create(image);
			} catch (DataAccessException e) {
				be.add(BusinessCode.VALIDATION_ARTICLE_ERREUR);
				throw be;
			}
		} else {
			throw be;
		}
	}

	//Validations
	private boolean validerArticle(long id, BusinessException be) {
		if (id < 1) {
			be.add(BusinessCode.VALIDATION_IMAGE_ARTICLE_NULL);
			return false;
		}
		return true;
	}
	
	private boolean validerNom(String nom, BusinessException be) {
		if (nom.isEmpty()) {
			be.add(BusinessCode.VALIDATION_IMAGE_NOM_NULL);
			return false;
		} else if(nom.length() > 255) {
			be.add(BusinessCode.VALIDATION_IMAGE_NOM_TROPLONG);
			return false;
		}
		return true;
	}
}
