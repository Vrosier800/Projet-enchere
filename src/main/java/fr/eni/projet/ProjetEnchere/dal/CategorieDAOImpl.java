package fr.eni.projet.ProjetEnchere.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.projet.ProjetEnchere.bo.Categorie;

@Repository
public class CategorieDAOImpl implements CategorieDAO {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private final String FIND_ALL = "SELECT * FROM CATEGORIES";
	private final String READ = "SELECT * FROM CATEGORIES WHERE no_categorie = :id";
	private final String CREATE = "INSERT INTO CATEGORIES (libelle) VALUES (:libelle)";
	private final String UPDATE = "UPDATE CATEGORIES SET libelle = :libelle WHERE no_categorie =:id";
	private static final String DELETE = "DELETE FROM CATEGORIES WHERE no_categorie=:id";

	
	@Override
	public Categorie read(long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", id);

		return jdbcTemplate.queryForObject(READ, namedParameters, new CategorieRowMapper());
	}
	
	@Override
	public List<Categorie> findAll() {
		return jdbcTemplate.query(FIND_ALL, new CategorieRowMapper());
	}

	@Override
	public void create(Categorie categorie) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("libelle", categorie.getLibelle());

		jdbcTemplate.update(CREATE, namedParameters, keyHolder);

		if (keyHolder != null && keyHolder.getKey() != null) {
			categorie.setId(keyHolder.getKey().longValue());
		}
	}

	@Override
	public void update(Categorie categorie) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", categorie.getId());
		namedParameters.addValue("libelle", categorie.getLibelle());

		jdbcTemplate.update(UPDATE, namedParameters);
	}

	@Override
	public void delete(long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", id);

		int rowsAffected = jdbcTemplate.update(DELETE, namedParameters);
		System.out.println("Rows affected: " + rowsAffected);

	}
	
	class CategorieRowMapper implements RowMapper<Categorie> {

		@Override
		public Categorie mapRow(ResultSet rs, int rowNum) throws SQLException {
			Categorie c = new Categorie();
			c.setId(rs.getLong("no_categorie"));
			c.setLibelle(rs.getString("libelle"));
			return c;
		}
	}
}
