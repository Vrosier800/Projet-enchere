package fr.eni.projet.ProjetEnchere.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;
import fr.eni.projet.ProjetEnchere.bo.Enchere;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;

@Repository
public class EnchereDAOImpl implements EnchereDAO {

	

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	private final String READ ="SELECT * FROM ENCHERES WHERE id_utilisateur = :id_utilisateur AND no_article=:no_article";
	private final String FIND_ALL = "SELECT * FROM ENCHERES WHERE no_article=:no_article";
	private final String READ_BY_USER = "SELECT * FROM ENCHERES WHERE id_utilisateur = :id_utilisateur";
	private final String CREATE = "INSERT INTO ENCHERES (id_utilisateur, no_article, montant_enchere, date_enchere) VALUES (:id_utilisateur,:no_article, :montant,:date_enchere)";
	private final String UPDATE = "UPDATE ENCHERES SET montant_enchere= :montant_enchere, date_enchere = :date_enchere WHERE id_utilisateur =:id_utilisateur AND no_article= :no_article";
	private final String DELETE = "DELETE FROM ENCHERES WHERE id_utilisateur =:id_utilisateur AND no_article=:no_article";
	private final String DELETE_BY_ID_ARTICLE = "DELETE FROM ENCHERES WHERE no_article=:no_article";
	private final String DELETE_BY_PSEUDO = "DELETE FROM ENCHERES WHERE id_utilisateur = :id_utilisateur";
	private final String GET_MAX_OFFER = "SELECT MAX(montant_enchere) FROM ENCHERES WHERE no_article = :no_article";
	private final String COUNT_OFFER = "SELECT COUNT(montant_enchere) FROM ENCHERES WHERE id_utilisateur =:id_utilisateur AND no_article=:no_article ";
	private final String GET_LAST_OFFER = "SELECT * FROM ENCHERES WHERE id_utilisateur =:id_utilisateur AND no_article=:no_article ";
	private final String COUNT_TOTAL_OFFER = "SELECT COUNT(*) FROM ENCHERES WHERE no_article=:no_article";
	@Override
	public Enchere read(Utilisateur acquereur, ArticleAVendre article) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id_utilisateur", acquereur.getPseudo());
		namedParameters.addValue("no_article", article.getId());
		
		return jdbcTemplate.queryForObject(READ, namedParameters, new EnchererRowMapper());
	}
	
	@Override
	public List<Enchere> readByUser(Utilisateur acquereur) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id_utilisateur", acquereur.getPseudo());

		return jdbcTemplate.query(READ_BY_USER, namedParameters, new EnchererRowMapper());
	}

	@Override
	public List<Enchere> findAll(long id) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_article", id);

		return jdbcTemplate.query(FIND_ALL, namedParameters, new EnchererRowMapper());
	}

	@Override
	public void create(Enchere enchere) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id_utilisateur", enchere.getAcquereur().getPseudo());
		namedParameters.addValue("no_article", enchere.getArticleAVendre().getId());
		namedParameters.addValue("montant", enchere.getMontant());
		namedParameters.addValue("date_enchere", enchere.getDate());

		jdbcTemplate.update(CREATE, namedParameters);

	}

	@Override
	public void delete(Enchere enchere) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id_utilisateur", enchere.getAcquereur().getPseudo());
		namedParameters.addValue("no_article", enchere.getArticleAVendre().getId());

		jdbcTemplate.update(DELETE, namedParameters);
	}
	
	@Override
	public void deleteByPseudo(String pseudo) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id_utilisateur", pseudo);

		jdbcTemplate.update(DELETE_BY_PSEUDO, namedParameters);
	}
	
	@Override
	public void deleteByIdArticle(long id) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_article", id);

		jdbcTemplate.update(DELETE_BY_ID_ARTICLE, namedParameters);
	}

	@Override
	public void update(Enchere enchere) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id_utilisateur", enchere.getAcquereur().getPseudo());
		namedParameters.addValue("no_article", enchere.getArticleAVendre().getId());
		namedParameters.addValue("montant_enchere", enchere.getMontant());
		namedParameters.addValue("date_enchere", enchere.getDate());

		jdbcTemplate.update(UPDATE, namedParameters);

	}
	
	@Override
	public int getMaxOffer(Enchere enchere) {
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("no_article", enchere.getArticleAVendre().getId());

	    try {
	      
	        Optional<Integer> maxOffer = Optional.ofNullable(
	            jdbcTemplate.queryForObject(GET_MAX_OFFER, namedParameters, Integer.class)
	        );

	        
	        return maxOffer.orElse(0);
	    } catch (EmptyResultDataAccessException e) {
	    	
	        return 0;
	    }
	}
	
	@Override 
	public Enchere getLastOffer(Enchere enchere) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		    namedParameters.addValue("no_article", enchere.getArticleAVendre().getId());
		    namedParameters.addValue("id_utilisateur", enchere.getAcquereur().getPseudo());

		    return jdbcTemplate.queryForObject(GET_LAST_OFFER, namedParameters, new EnchererRowMapper());
	}
	
	@Override
	public int countOfferUser(Enchere enchere) {
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("no_article", enchere.getArticleAVendre().getId());
	    namedParameters.addValue("id_utilisateur", enchere.getAcquereur().getPseudo());

	    Integer count = jdbcTemplate.queryForObject(COUNT_OFFER, namedParameters, Integer.class);

	    return (count != null) ? count : 0;
	}
	
	@Override
	public int countOffer(Enchere enchere) {
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("no_article", enchere.getArticleAVendre().getId());

	    Integer count = jdbcTemplate.queryForObject(COUNT_TOTAL_OFFER, namedParameters, Integer.class);

	    return (count != null) ? count : 0;
	}
	
	
	class EnchererRowMapper implements RowMapper<Enchere> {

	    @Override
	    public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
	        Enchere e = new Enchere();
	        
	       
	        String fullDateTime = rs.getString("date_enchere");
	        
	        String dateOnly = fullDateTime.substring(0, 10);
	        
	        e.setDate(LocalDate.parse(dateOnly));
	        
	        e.setMontant(rs.getInt("montant_enchere"));
	        
	        Utilisateur user = new Utilisateur();
	        user.setPseudo(rs.getString("id_utilisateur"));
	        e.setAcquereur(user);
	        
	        ArticleAVendre a = new ArticleAVendre();
	        a.setId(rs.getLong("no_article"));
	        e.setArticleAVendre(a);
	        return e;
	    }
	}	

}
