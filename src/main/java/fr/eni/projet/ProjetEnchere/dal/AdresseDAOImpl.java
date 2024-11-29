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

import fr.eni.projet.ProjetEnchere.bo.Adresse;

@Repository
public class AdresseDAOImpl implements AdresseDAO {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private final String FIND_ALL = "SELECT * FROM ADRESSES";
	private final String READ = "SELECT * FROM ADRESSES WHERE no_adresse = :id";
	private final String CREATE = "INSERT INTO ADRESSES (rue, code_postal, ville) VALUES (:rue, :code_postal, :ville)";
	private final String UPDATE = "UPDATE ADRESSES SET rue=:rue, code_postal=:code_postal, ville=:ville WHERE no_adresse =:id";
	private final String DELETE = "DELETE FROM ADRESSES WHERE no_adresse=:id";
	private final String FIND_ALL_ENI = "SELECT * FROM ADRESSES WHERE adresse_eni = 1";
	
	@Override
	public Adresse read(long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", id);

		return jdbcTemplate.queryForObject(READ, namedParameters, new AdresseRowMapper());
	}

	@Override
	public List<Adresse> findAll() {
		return jdbcTemplate.query(FIND_ALL, new AdresseRowMapper());
	}
	@Override
	public List<Adresse> findAllEni(){
		return jdbcTemplate.query(FIND_ALL_ENI, new AdresseRowMapper());
	}

	@Override
	public void create(Adresse adresse) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("rue", adresse.getRue());
		namedParameters.addValue("code_postal", adresse.getCodePostal());
		namedParameters.addValue("ville", adresse.getVille());

		jdbcTemplate.update(CREATE, namedParameters, keyHolder);

		if (keyHolder != null && keyHolder.getKey() != null) {
			adresse.setId(keyHolder.getKey().longValue());
		}
	}

	@Override
	public void update(Adresse adresse) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", adresse.getId());
		namedParameters.addValue("rue", adresse.getRue());
		namedParameters.addValue("code_postal", adresse.getCodePostal());
		namedParameters.addValue("ville", adresse.getVille());

		jdbcTemplate.update(UPDATE, namedParameters);
	}

	@Override
	public void delete(Long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", id);

		int rowsAffected = jdbcTemplate.update(DELETE, namedParameters);
		System.out.println("Rows affected: " + rowsAffected);
	}
	
	class AdresseRowMapper implements RowMapper<Adresse> {

		@Override
		public Adresse mapRow(ResultSet rs, int rowNum) throws SQLException {
			Adresse a = new Adresse();
			a.setId(rs.getLong("no_adresse"));
			a.setRue(rs.getString("rue"));
			a.setCodePostal(rs.getString("code_postal"));
			a.setVille(rs.getString("ville"));

			return a;
		}
	}

}
