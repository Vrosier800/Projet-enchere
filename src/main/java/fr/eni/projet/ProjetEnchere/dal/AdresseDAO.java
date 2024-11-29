package fr.eni.projet.ProjetEnchere.dal;

import java.util.List;

import fr.eni.projet.ProjetEnchere.bo.Adresse;

public interface AdresseDAO {
	Adresse read(long id);
	List<Adresse>findAll();
	void create(Adresse adresse);
	void update(Adresse adresse);
	void delete(Long id );
	List<Adresse> findAllEni();
}