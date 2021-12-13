package de.samply.icd10dictionary.dao;

import de.samply.icd10dictionary.model.IcdCode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Postgres implementation.
 */
@Repository
public class IcdCodeDaoPostgres implements IcdCodeDao {

  private final JdbcTemplate jdbcTemplate;

  public IcdCodeDaoPostgres(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void insert(IcdCode icdCode) {
    this.jdbcTemplate.update(
        "INSERT INTO IcdCode (code, kind, display, definition, parentCode, childCodes) "
            + "VALUES (?, ?, ?, ?, ?, ?)",
        icdCode.code(),
        icdCode.kind(),
        icdCode.display(),
        icdCode.definition(),
        icdCode.parentCode(),
        icdCode.childCodes());
  }

  @Override
  public Optional<IcdCode> selectIcdCodeByCode(String codeParam) {
    IcdCode icdCode =
        this.jdbcTemplate.queryForObject(
            "SELECT code, kind, display, definition, parentCode, childCodes "
                + "FROM IcdCode "
                + "WHERE code = ?",
            new Object[]{codeParam},
            new int[]{1},
            ((resultSet, i) -> createIcdCode(resultSet)));
    return Optional.ofNullable(icdCode);
  }

  @Override
  public int deleteByCode(String code) {
    this.jdbcTemplate.update("DELETE FROM IcdCode WHERE id = ?", code);
    return 0;
  }

  @Override
  public int deleteAll() {
    this.jdbcTemplate.update("DELETE FROM IcdCode");
    return 0;
  }

  @Override
  public int count() {
    List<Integer> countList = this.jdbcTemplate.query(
        "SELECT count(*) FROM IcdCode ",
        (resultSet, i) -> resultSet.getInt(1));
    return countList.get(0);
  }

  @Override
  public List<IcdCode> retrieveCodesByQueryText(String queryText) {
    if (queryText == null || queryText.isBlank()) {
      return List.of();
    }

    return this.jdbcTemplate.query(
        "SELECT code, kind, display, definition, parentCode, childCodes "
            + "FROM IcdCode "
            + "WHERE childCodes = ''"
            + "AND (LOWER(code) like LOWER(?)"
            + "OR to_tsvector('german', definition) @@ "
            + "  array_to_string("
            + "    array(select unnest || ':*' "
            + "      from unnest("
            + "        regexp_split_to_array(plainto_tsquery('german', ?)::text, "
            + "          '\\s&\\s')"
            + "      )"
            + "    ), ' & '"
            + "  )::tsquery)",
        ((resultSet, i) -> createIcdCode(resultSet)),
        "%" + queryText + "%");
  }

  @Override
  public List<IcdCode> retrieveAll() {
    return this.jdbcTemplate.query(
        "SELECT code, kind, display, definition, parentCode, childCodes FROM IcdCode ",
        ((resultSet, i) -> createIcdCode(resultSet)));
  }

  private IcdCode createIcdCode(ResultSet resultSet) throws SQLException {
    String code = resultSet.getString("code");
    String kind = resultSet.getString("kind");
    String display = resultSet.getString("display");
    String definition = resultSet.getString("definition");
    String parentCode = resultSet.getString("parentCode");
    String childCodes = resultSet.getString("childCodes");
    return new IcdCode(code, kind, display, definition, parentCode, childCodes);
  }
}
