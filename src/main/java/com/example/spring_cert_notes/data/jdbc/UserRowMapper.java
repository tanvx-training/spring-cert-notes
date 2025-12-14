package com.example.spring_cert_notes.data.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ROWMAPPER EXAMPLE
 * <p>
 * RowMapper maps a single row to an object.
 * Called for each row in the ResultSet.
 * <p>
 * Use when: Each row = one object
 */
@Component
public class UserRowMapper implements RowMapper<User> {
    
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setBalance(rs.getDouble("balance"));
        return user;
    }
}
