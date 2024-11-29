package fr.eni.projet.ProjetEnchere.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.projet.ProjetEnchere.bo.Categorie;
import fr.eni.projet.ProjetEnchere.dal.CategorieDAO;

@Service
public class CategorieServiceImpl implements CategorieService {
	
	private CategorieDAO categorieDAO;
	

	public CategorieServiceImpl(CategorieDAO categorieDAO) {
		this.categorieDAO = categorieDAO;
	}

	
	@Override
	public List<Categorie> consulterCategorie(){
		return categorieDAO.findAll();
	}
	
	@Override
	public Categorie consulterCategorieParID(long id) {
		Categorie categorie = categorieDAO.read(id);
		return categorie;
	}

}
