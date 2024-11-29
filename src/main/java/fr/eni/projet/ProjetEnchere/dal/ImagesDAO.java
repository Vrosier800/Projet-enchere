package fr.eni.projet.ProjetEnchere.dal;

import fr.eni.projet.ProjetEnchere.bo.Image;

public interface ImagesDAO {
	
	void create(Image image);
	
	Image consulterImageParArticle(long id);


}
