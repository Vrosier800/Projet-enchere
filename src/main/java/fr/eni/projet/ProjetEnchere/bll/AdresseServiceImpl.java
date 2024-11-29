package fr.eni.projet.ProjetEnchere.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.projet.ProjetEnchere.bo.Adresse;
import fr.eni.projet.ProjetEnchere.dal.AdresseDAO;

@Service
public class AdresseServiceImpl implements AdresseService {
	
	private AdresseDAO adresseDAO;
	

	public AdresseServiceImpl(AdresseDAO adresseDAO) {
		this.adresseDAO = adresseDAO;
	}


	@Override
	public Adresse consulterAdresseParId(long id) {
		Adresse adresse = adresseDAO.read(id);
		return adresse;
	}


	@Override
	public List<Adresse> consulterAdresses() {
		List<Adresse> adresses = adresseDAO.findAll();
		return adresses;
	}
	
	@Override
	public List<Adresse> consulterAdressesEni() {
		List<Adresse> adresses = adresseDAO.findAllEni();
		return adresses;
	}

}
