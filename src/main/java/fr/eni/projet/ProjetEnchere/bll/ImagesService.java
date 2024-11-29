package fr.eni.projet.ProjetEnchere.bll;

import fr.eni.projet.ProjetEnchere.bo.Image;

public interface ImagesService {
	
	void ajouterImage(Image image);
	
	Image consulterImageParArticle(long id);

}
