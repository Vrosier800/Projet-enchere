package fr.eni.projet.ProjetEnchere.dal;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.projet.ProjetEnchere.bo.ArticleAVendre;
import fr.eni.projet.ProjetEnchere.bo.Image;

@Repository
public class ImagesDAOImpl implements ImagesDAO{
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	private final String CREATE = "INSERT INTO IMAGES (no_article, nom) VALUES (:no_article,:nom)";
	private final String READBYARTICLE = "SELECT * FROM IMAGES WHERE no_article = :no_article";


	@Override
	public void create(Image image) {
		System.out.println(image);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_article", image.getArticleAVendre().getId());
		namedParameters.addValue("nom", image.getNom());

		jdbcTemplate.update(CREATE, namedParameters);
		
	}


	@Override
	public Image consulterImageParArticle(long id) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_article", id);
		
		return jdbcTemplate.queryForObject(READBYARTICLE, namedParameters, new ImageRowMapper());
	}
	
	class ImageRowMapper implements RowMapper<Image> {

		@Override
		public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
			Image i = new Image();
			i.setId(rs.getLong("id"));
			i.setNom(rs.getString("nom"));
			ArticleAVendre article = new ArticleAVendre();
			article.setId(rs.getLong("no_article"));
			i.setArticleAVendre(article);
	
			return i;
		}
	}

}
