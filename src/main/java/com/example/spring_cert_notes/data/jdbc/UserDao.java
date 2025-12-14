package com.example.spring_cert_notes.data.jdbc;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * USER DAO - JdbcTemplate Examples
 * <p>
 * Demonstrates:
 * - query() with RowMapper
 * - queryForObject()
 * - update() for INSERT/UPDATE/DELETE
 * - Lambda RowMapper
 * - NamedParameterJdbcTemplate
 * - SimpleJdbcInsert
 */
@Repository
public class UserDao {
    
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedTemplate;
    private final SimpleJdbcInsert simpleInsert;
    private final UserRowMapper userRowMapper;
    
    // Lambda RowMapper (inline)
    private final RowMapper<User> lambdaMapper = (rs, rowNum) -> 
        new User(rs.getLong("id"), rs.getString("name"), 
                 rs.getString("email"), rs.getDouble("balance"));
    
    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate, DataSource dataSource, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("users")
            .usingGeneratedKeyColumns("id");
        this.userRowMapper = userRowMapper;
    }
    
    // ============================================================
    // QUERY METHODS
    // ============================================================
    
    public List<User> findAll() {
        System.out.println(Prefixes.DATA_JDBC + "Finding all users");
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }
    
    public List<User> findAllWithLambda() {
        System.out.println(Prefixes.DATA_JDBC + "Finding all users (lambda mapper)");
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, lambdaMapper);
    }
    
    public Optional<User> findById(Long id) {
        System.out.println(Prefixes.DATA_JDBC + "Finding user by id: " + id);
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, id);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public List<User> findByName(String name) {
        System.out.println(Prefixes.DATA_JDBC + "Finding users by name: " + name);
        String sql = "SELECT * FROM users WHERE name LIKE ?";
        return jdbcTemplate.query(sql, userRowMapper, "%" + name + "%");
    }
    
    // Named parameters (safer, more readable)
    public List<User> findByNameNamed(String name) {
        System.out.println(Prefixes.DATA_JDBC + "Finding users by name (named params): " + name);
        String sql = "SELECT * FROM users WHERE name LIKE :name";
        MapSqlParameterSource params = new MapSqlParameterSource("name", "%" + name + "%");
        return namedTemplate.query(sql, params, userRowMapper);
    }
    
    public int count() {
        System.out.println(Prefixes.DATA_JDBC + "Counting users");
        String sql = "SELECT COUNT(*) FROM users";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }
    
    // ============================================================
    // INSERT METHODS
    // ============================================================
    
    public int insert(User user) {
        System.out.println(Prefixes.DATA_JDBC + "Inserting user: " + user.getName());
        String sql = "INSERT INTO users (name, email, balance) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getBalance());
    }
    
    public Long insertAndGetId(User user) {
        System.out.println(Prefixes.DATA_JDBC + "Inserting user and getting ID: " + user.getName());
        Map<String, Object> params = new HashMap<>();
        params.put("name", user.getName());
        params.put("email", user.getEmail());
        params.put("balance", user.getBalance());
        return simpleInsert.executeAndReturnKey(params).longValue();
    }
    
    // ============================================================
    // UPDATE METHODS
    // ============================================================
    
    public int update(User user) {
        System.out.println(Prefixes.DATA_JDBC + "Updating user: " + user.getId());
        String sql = "UPDATE users SET name = ?, email = ?, balance = ? WHERE id = ?";
        return jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getBalance(), user.getId());
    }
    
    public int updateBalance(Long userId, double newBalance) {
        System.out.println(Prefixes.DATA_JDBC + "Updating balance for user " + userId + " to " + newBalance);
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        return jdbcTemplate.update(sql, newBalance, userId);
    }
    
    // ============================================================
    // DELETE METHODS
    // ============================================================
    
    public int delete(Long id) {
        System.out.println(Prefixes.DATA_JDBC + "Deleting user: " + id);
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
    
    public int deleteAll() {
        System.out.println(Prefixes.DATA_JDBC + "Deleting all users");
        String sql = "DELETE FROM users";
        return jdbcTemplate.update(sql);
    }
    
    // ============================================================
    // BATCH OPERATIONS
    // ============================================================
    
    public int[] batchInsert(List<User> users) {
        System.out.println(Prefixes.DATA_JDBC + "Batch inserting " + users.size() + " users");
        String sql = "INSERT INTO users (name, email, balance) VALUES (?, ?, ?)";
        return jdbcTemplate.batchUpdate(sql, users, users.size(),
            (ps, user) -> {
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setDouble(3, user.getBalance());
            });
    }
}
