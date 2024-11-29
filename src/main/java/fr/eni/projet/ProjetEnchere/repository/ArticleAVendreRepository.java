package fr.eni.projet.ProjetEnchere.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;

public interface ArticleAVendreRepository extends JpaRepository<ArticleAVendre, Long> {
	 List<ArticleAVendre> findAll();
}
