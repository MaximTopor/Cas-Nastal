package sk.upjs.paz.dao.psdao.mapper;

import org.springframework.jdbc.core.RowMapper;
import sk.upjs.paz.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getLong("id_user"),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getString("email"),
                rs.getString("phone_number"),
                rs.getString("password_hash"),

                rs.getString("rodne_cislo"),
                rs.getObject("date_of_birth", LocalDate.class),
                rs.getString("adresa"),

                rs.getInt("role_id"),
                rs.getLong("district_id"),

                rs.getObject("created_at", LocalDateTime.class),
                rs.getObject("update_at", LocalDateTime.class)
        );
    }
}
