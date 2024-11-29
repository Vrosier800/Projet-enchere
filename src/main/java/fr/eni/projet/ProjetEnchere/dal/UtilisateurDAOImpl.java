package fr.eni.projet.ProjetEnchere.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.projet.ProjetEnchere.bo.Adresse;
import fr.eni.projet.ProjetEnchere.bo.Utilisateur;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO {

	private static final String INSERT = "INSERT INTO utilisateurs (pseudo, nom, prenom, telephone, mot_de_passe, email, credit, administrateur, no_adresse) "
			+ " VALUES (:pseudo, :nom, :prenom, :telephone, :motDePasse, :email, :credit, :administrateur, :noAdresse) ";
	private static final String FIND_BY_PSEUDO = "SELECT pseudo, nom, prenom, telephone, email, credit, administrateur, no_adresse FROM utilisateurs WHERE pseudo = :pseudo ";
	private static final String FIND_BY_EMAIL = "SELECT pseudo, nom, prenom, telephone, email, credit, administrateur, no_adresse FROM utilisateurs WHERE email = :email ";
	private static final String FIND_ALL = "SELECT pseudo, nom, prenom, telephone, email, credit, administrateur, no_adresse FROM utilisateurs";
	private static final String FIND_MDP = "SELECT pseudo, mot_de_passe FROM utilisateurs WHERE pseudo = :pseudo ";
	private static final String UPDATE = "UPDATE utilisateurs SET "
			+ "nom=:nom, "
			+ "prenom=:prenom, "
			+ "telephone=:telephone, "
			+ "email=:email, "
			+ "no_adresse=:noAdresse "
			+ "WHERE pseudo =:pseudo ";
	private static final String UPDATE_MOT_DE_PASSE = "UPDATE utilisateurs SET "
			+ "mot_de_passe=:motDePasse "
			+ "WHERE pseudo =:pseudo ";
	private static final String FIND_UNIQUE_PSEUDO = "SELECT count(pseudo) FROM utilisateurs WHERE pseudo LIKE :pseudo ";
	private static final String FIND_UNIQUE_EMAIL = "SELECT count(email) FROM utilisateurs WHERE email LIKE :email ";
	private static final String UPDATE_CREDIT = "UPDATE utilisateurs SET credit = :credit WHERE pseudo LIKE :pseudo";
	private final String DELETE_BY_PSEUDO = "DELETE FROM utilisateurs WHERE pseudo = :pseudo";
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public void create(Utilisateur utilisateur) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", utilisateur.getPseudo());
		namedParameters.addValue("nom", utilisateur.getNom());
		namedParameters.addValue("prenom", utilisateur.getPrenom());
		namedParameters.addValue("telephone", utilisateur.getTelephone());
		namedParameters.addValue("motDePasse", utilisateur.getMotDePasse());
		namedParameters.addValue("email", utilisateur.getEmail());
		namedParameters.addValue("credit", utilisateur.getCredit());
		namedParameters.addValue("administrateur", utilisateur.isAdmin());
		namedParameters.addValue("noAdresse", utilisateur.getAdresse().getId());
		
		jdbcTemplate.update(INSERT, namedParameters);
	}

	@Override
	public Utilisateur read(String pseudo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", pseudo);
		return jdbcTemplate.queryForObject(FIND_BY_PSEUDO, namedParameters, new UtilisateurRowMapper());
	}

	@Override
	public Utilisateur readEmail(String email) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("email", email);
		return jdbcTemplate.queryForObject(FIND_BY_EMAIL, namedParameters, new UtilisateurRowMapper());
	}
	
	@Override
	public List<Utilisateur> findAll() {
		return jdbcTemplate.query(FIND_ALL, new UtilisateurRowMapper());
	}


	@Override
	public void update(Utilisateur utilisateur) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", utilisateur.getPseudo());
		namedParameters.addValue("nom", utilisateur.getNom());
		namedParameters.addValue("prenom", utilisateur.getPrenom());
		namedParameters.addValue("telephone", utilisateur.getTelephone());
		namedParameters.addValue("motDePasse", utilisateur.getMotDePasse());
		namedParameters.addValue("email", utilisateur.getEmail());
		namedParameters.addValue("noAdresse", utilisateur.getAdresse().getId());
		
		jdbcTemplate.update(UPDATE, namedParameters);
	}
	
	@Override
	public void deleteByPseudo(String pseudo) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", pseudo);

		jdbcTemplate.update(DELETE_BY_PSEUDO, namedParameters);
	}
	
	@Override
	public void updateMotDePasse(Utilisateur utilisateur) {
		System.out.println(utilisateur.getPseudo());
		System.out.println(utilisateur.getMotDePasse());
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", utilisateur.getPseudo());
		namedParameters.addValue("motDePasse", utilisateur.getMotDePasse());
		
		jdbcTemplate.update(UPDATE_MOT_DE_PASSE, namedParameters);
	}
	
	@Override
	public void updateCredit(Utilisateur utilisateur) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", utilisateur.getPseudo());
		namedParameters.addValue("credit", utilisateur.getCredit());
		
		jdbcTemplate.update(UPDATE_CREDIT, namedParameters);	
	}
	
	class UtilisateurRowMapper implements RowMapper<Utilisateur> {

		@Override
		public Utilisateur mapRow(ResultSet rs, int rowNum) throws SQLException {
			Utilisateur u = new Utilisateur();
			u.setPseudo(rs.getString("pseudo"));
			u.setNom(rs.getString("nom"));
			u.setPrenom(rs.getString("prenom"));
			u.setTelephone(rs.getString("telephone"));
			u.setEmail(rs.getString("email"));
			u.setCredit(rs.getInt("credit"));
			u.setAdmin(rs.getBoolean("administrateur"));
			
			Adresse adresse = new Adresse();
			adresse.setId(rs.getLong("no_adresse"));
			u.setAdresse(adresse);
			
			return u;
		}

	}
	
	class MDPRowMapper implements RowMapper<Utilisateur> {

		@Override
		public Utilisateur mapRow(ResultSet rs, int rowNum) throws SQLException {
			Utilisateur u = new Utilisateur();
			u.setPseudo(rs.getString("pseudo"));
			u.setMotDePasse(rs.getString("mot_de_passe"));
			
			return u;
		}

	}

	@Override
	public boolean findPseudo(String pseudo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", pseudo);
		
		int val = jdbcTemplate.queryForObject(FIND_UNIQUE_PSEUDO, namedParameters, Integer.class);
		return val >=1;
	}

	@Override
	public boolean findEmail(String email) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("email", email);
		
		int val = jdbcTemplate.queryForObject(FIND_UNIQUE_EMAIL, namedParameters, Integer.class);
		return val >=1;
	}

	@Override
	public Utilisateur readMDP(String pseudo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", pseudo);
		return jdbcTemplate.queryForObject(FIND_MDP, namedParameters, new MDPRowMapper());
	}

}
