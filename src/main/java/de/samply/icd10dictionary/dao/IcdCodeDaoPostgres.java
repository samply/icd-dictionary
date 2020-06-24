package de.samply.icd10dictionary.dao;

import de.samply.icd10dictionary.model.IcdCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
@Repository("postgres")
public class IcdCodeDaoPostgres implements IcdCodeDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public IcdCodeDaoPostgres(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insert(IcdCode icdCode) {
        final String SQL = "INSERT INTO IcdCode (code, kind, display, definition, parentCode, childCodes) VALUES (?, ?, ?, ?, ?, ?)";
        this.jdbcTemplate.update(SQL, icdCode.getCode(), icdCode.getKind(), icdCode.getDisplay(), icdCode.getDefinition(), icdCode.getParentCode(), icdCode.getChildCodes());
        return 0;
    }

    @Override
    public Optional<IcdCode> selectIcdCodeByCode(String codeParam) {
        final String SQL = "SELECT code, kind, display, definition, parentCode, childCodes " +
                "FROM IcdCode " +
                "WHERE code = ?";
        IcdCode icdCode =
                this.jdbcTemplate.queryForObject(SQL, new Object[]{codeParam}, ((resultSet, i) -> {
                    return createIcdCode(resultSet);
                }));
        return Optional.ofNullable(icdCode);
    }

    @Override
    public int deleteByCode(String code) {
        final String SQL = "DELETE FROM IcdCode WHERE id = ?";
        this.jdbcTemplate.update(SQL, code);
        return 0;
    }

    @Override
    public int deleteAll() {
        final String SQL = "DELETE FROM IcdCode";
        this.jdbcTemplate.update(SQL);
        return 0;
    }

    @Override
    public int count() {
        final String SQL = "SELECT count(*) FROM IcdCode ";
        List<Integer> countList =
                this.jdbcTemplate.query(SQL, (resultSet, i) -> resultSet.getInt(1));
        return countList.get(0);
    }

    @Override
    public List<IcdCode> retrieveCodesBySearchword(String searchword) {
        if (StringUtils.isBlank(searchword)) {
            return new ArrayList<>();
        }

        final String SQL = "SELECT code, kind, display, definition, parentCode, childCodes " +
                "FROM IcdCode " +
                "WHERE to_tsvector(definition) @@ to_tsquery(' " + searchword + ":*')";
        return this.jdbcTemplate.query(SQL, ((resultSet, i) -> createIcdCode(resultSet)));
    }

    @Override
    public List<IcdCode> retrieveAll() {
        final String SQL = "SELECT code, kind, display, definition, parentCode, childCodes FROM IcdCode ";
        return this.jdbcTemplate.query(SQL, ((resultSet, i) -> createIcdCode(resultSet)));
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
