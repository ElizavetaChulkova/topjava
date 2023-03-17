package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;

@Transactional(readOnly = true)
@Repository
public class JdbcUserRepository implements UserRepository {
    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final ResultSetExtractor<List<User>> ROW_EXTRACTOR = rs -> {
        List<User> list = new ArrayList<>();
        while (rs.next()) {
            Role role = Role.valueOf(rs.getString("role"));
            User user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                    rs.getString("password"), rs.getInt("calories_per_day"),
                    rs.getBoolean("enabled"), rs.getDate("registered"),
                    Set.of(role));
            list.add(user);
        }
        return list;
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional
    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
        jdbcTemplate.update("DELETE FROM user_role WHERE user_id = ?", user.getId());
        batchUpdate(user.getRoles(), user);
        return user;
    }

    public void batchUpdate(Set<Role> roles, User user) {
        jdbcTemplate.batchUpdate("INSERT INTO user_role (user_id, role) values (?, ?)",
                roles, roles.size(), (ps, roleArg) -> {
                    ps.setInt(1, user.id());
                    ps.setString(2, roleArg.name());
                });
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_role r ON u.id = r.user_id WHERE user_id=?",
                ROW_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_role r ON u.id = r.user_id WHERE email=?",
                ROW_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        Map<Integer, Set<Role>> userRoles = new HashMap<>();
        jdbcTemplate.query("SELECT * FROM user_role", rse -> {
            userRoles.put(rse.getInt("user_id"), Set.of(Role.valueOf(rse.getString("role"))));
        });
        users.forEach(u -> u.setRoles(userRoles.get(u.getId())));
        return users;
    }
}
