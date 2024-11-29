package fr.eni.projet.ProjetEnchere.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.projet.ProjetEnchere.bo.Adresse;
import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;
import fr.eni.projet.ProjetEnchere.bo.Categorie;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;

@Repository
public class ArticleAVendreDAOImpl implements ArticleAVendreDAO {

	

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private final String FIND_ALL_USER = "SELECT * FROM ARTICLES_A_VENDRE WHERE id_utilisateur = :pseudo ";
	private final String FIND_ALL_ACTIVE = "SELECT * FROM ARTICLES_A_VENDRE WHERE statut_enchere= :statut";
	private final String FIND_ALL = "SELECT * FROM ARTICLES_A_VENDRE";
	private final String FIND_ALL_BY_PSEUDO = "SELECT * FROM ARTICLES_A_VENDRE WHERE id_utilisateur = :pseudo";
	private final String READ = "SELECT * FROM ARTICLES_A_VENDRE WHERE no_article = :no_article";
	private final String CREATE = "INSERT INTO ARTICLES_A_VENDRE "
			+ "(nom_article, description, date_debut_encheres, date_fin_encheres,statut_enchere, prix_initial, id_utilisateur, no_categorie,no_adresse_retrait ) "
			+ "VALUES (:nom_article,:description,:date_debut,:date_fin,:statut,:prix_init,:id_utilisateur,:no_categorie,:no_adresse_retrait )";
	private final String UPDATE = "UPDATE ARTICLES_A_VENDRE SET " + "nom_article = :nom_article,"
			+ "description = :description," + "date_debut_encheres = :date_debut," + "date_fin_encheres = :date_fin,"
			+ "statut_enchere = :statut," + "prix_initial = :prix_init," + "prix_vente = :prix_vente,"
			+ "id_utilisateur = :id_utilisateur," + "no_categorie = :no_categorie,"
			+ "no_adresse_retrait = :no_adresse_retrait " + "WHERE no_article = :id";
	private final String DELETE = "DELETE FROM ARTICLES_A_VENDRE WHERE no_article=:id";
	private final String DELETE_BY_PSEUDO = "DELETE FROM ARTICLES_A_VENDRE WHERE id_utilisateur=:idUtilisateur";
	private final String UPDATE_STATUT = "UPDATE ARTICLES_A_VENDRE SET statut_enchere=:statut WHERE no_article=:no_article";
	private final String UPDATE_PRIX_VENTE = "UPDATE ARTICLES_A_VENDRE SET prix_vente=:prix_vente WHERE no_article=:no_article";
	private final String CHECK_STATUT = "SELECT statut_enchere FROM ARTICLES_A_VENDRE WHERE no_article = :no_article";
	private final String FIND_ALL_ACTIVE_BY_USER = "SELECT * FROM ARTICLES_A_VENDRE WHERE statut_enchere=:statut AND id_utilisateur =:pseudo";
	private final String FIND_ALL_CLOSE_BY_USER = "SELECT * FROM ARTICLES_A_VENDRE WHERE (statut_enchere=:vendu OR statut_enchere=:cloture) AND id_utilisateur = :pseudo";
	private final String FIND_BY_PSEUDO_AND_STATUT = "SELECT * FROM ARTICLES_A_VENDRE WHERE statut_enchere =:statut AND id_utilisateur =:pseudo";
	
	@Override
	public ArticleAVendre read(long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_article", id);

		return jdbcTemplate.queryForObject(READ, namedParameters, new ArticleAVendrerRowMapper());
	}

	@Override
	public List<ArticleAVendre> findAll() {
		return jdbcTemplate.query(FIND_ALL, new ArticleAVendrerRowMapper());
	}

	@Override
	public List<ArticleAVendre> findAllActive() {
		int statut = 1;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("statut", statut);
		return jdbcTemplate.query(FIND_ALL_ACTIVE, namedParameters, new ArticleAVendrerRowMapper());
	}

	@Override
	public List<ArticleAVendre> findAllUser() {
		return jdbcTemplate.query(FIND_ALL_USER, new ArticleAVendrerRowMapper());
	}

	@Override
	public List<ArticleAVendre> findAllByPseudo(String pseudo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", pseudo);

		return jdbcTemplate.query(FIND_ALL_BY_PSEUDO, namedParameters, new ArticleAVendrerRowMapper());
	}

	@Override
	public List<ArticleAVendre> findAllActiveByUser(String pseudo) {
		int statut = 1;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", pseudo);	
		namedParameters.addValue("statut", statut);

		return jdbcTemplate.query(FIND_ALL_ACTIVE_BY_USER, namedParameters, new ArticleAVendrerRowMapper());
	}

	@Override
	public List<ArticleAVendre> findAllCloseByUser(String pseudo) {
		int vendu = 2;
		int cloture = 3;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", pseudo);
		namedParameters.addValue("vendu", vendu);
		namedParameters.addValue("cloture", cloture);
		
		return jdbcTemplate.query(FIND_ALL_CLOSE_BY_USER, namedParameters , new ArticleAVendrerRowMapper());
	}
	@Override
	public List<ArticleAVendre> findByUserAndStatut(String pseudo, int statut){
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", pseudo);
		namedParameters.addValue("statut", statut);
		
		return jdbcTemplate.query(FIND_BY_PSEUDO_AND_STATUT, namedParameters , new ArticleAVendrerRowMapper());
	}
	@Override
	public void create(ArticleAVendre article) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("nom_article", article.getNom());
		namedParameters.addValue("description", article.getDescription());
		namedParameters.addValue("date_debut", article.getDateDebutEncheres());
		namedParameters.addValue("date_fin", article.getDateFinEncheres());
		namedParameters.addValue("statut", article.getStatut());
		namedParameters.addValue("prix_init", article.getPrixInitial());
		namedParameters.addValue("id_utilisateur", article.getVendeur().getPseudo());
		namedParameters.addValue("no_categorie", article.getCategorie().getId());
		namedParameters.addValue("no_adresse_retrait", article.getRetrait().getId());

		jdbcTemplate.update(CREATE, namedParameters, keyHolder);

		if (keyHolder != null && keyHolder.getKey() != null) {
			article.setId(keyHolder.getKey().longValue());
		}
	}

	@Override
	public void delete(long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", id);

		int rowsAffected = jdbcTemplate.update(DELETE, namedParameters);
		System.out.println("Rows affected: " + rowsAffected);

	}
	
	@Override
	public void deleteByPseudo(String pseudo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idUtilisateur", pseudo);

		int rowsAffected = jdbcTemplate.update(DELETE_BY_PSEUDO, namedParameters);
		System.out.println("Rows affected: " + rowsAffected);

	}

	@Override
	public void update(ArticleAVendre article) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", article.getId());
		namedParameters.addValue("nom_article", article.getNom());
		namedParameters.addValue("description", article.getDescription());
		namedParameters.addValue("date_debut", article.getDateDebutEncheres());
		namedParameters.addValue("date_fin", article.getDateFinEncheres());
		namedParameters.addValue("statut", article.getStatut());
		namedParameters.addValue("prix_init", article.getPrixInitial());
		namedParameters.addValue("prix_vente", article.getPrixVente());
		namedParameters.addValue("id_utilisateur", article.getVendeur().getPseudo());
		namedParameters.addValue("no_categorie", article.getCategorie().getId());
		namedParameters.addValue("no_adresse_retrait", article.getRetrait().getId());

		jdbcTemplate.update(UPDATE, namedParameters);
	}

	@Override
	public void updateStatut(ArticleAVendre article, int statut) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("statut", statut);
		namedParameters.addValue("no_article", article.getId());

		jdbcTemplate.update(UPDATE_STATUT, namedParameters);
	}
	@Override
	public void updatePrixVente(ArticleAVendre article, int prixVente) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("prix_vente", prixVente);
		namedParameters.addValue("no_article", article.getId());
		
		jdbcTemplate.update(UPDATE_PRIX_VENTE, namedParameters);
	}
	@Override
	public int checkStatut(long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_article", id);
		int statut = jdbcTemplate.queryForObject(CHECK_STATUT, namedParameters, Integer.class);
		System.out.println(statut);
		return statut;
	}

	class ArticleAVendrerRowMapper implements RowMapper<ArticleAVendre> {

		@Override
		public ArticleAVendre mapRow(ResultSet rs, int rowNum) throws SQLException {
			ArticleAVendre a = new ArticleAVendre();
			a.setId(rs.getLong("no_article"));
			a.setNom(rs.getString("nom_article"));
			a.setDescription(rs.getString("description"));
			a.setDateDebutEncheres(LocalDate.parse(rs.getString("date_debut_encheres")));
			a.setDateFinEncheres(LocalDate.parse(rs.getString("date_fin_encheres")));
			a.setStatut(rs.getInt("statut_enchere"));
			a.setPrixInitial(rs.getInt("prix_initial"));
			a.setPrixVente(rs.getInt("prix_vente"));
			Utilisateur user = new Utilisateur();
			user.setPseudo(rs.getString("id_utilisateur"));
			a.setVendeur(user);
			Categorie c = new Categorie();
			c.setId(rs.getLong("no_categorie"));
			a.setCategorie(c);
			Adresse ad = new Adresse();
			ad.setId(rs.getLong("no_adresse_retrait"));
			a.setRetrait(ad);
			return a;
		}
	}

}
