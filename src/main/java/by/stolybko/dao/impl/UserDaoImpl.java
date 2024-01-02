package by.stolybko.dao.impl;

import by.stolybko.dao.Dao;
import by.stolybko.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Класс предоставляющий доступ к данным клиентов в базе данных.
 */
@Repository("userDao")
@RequiredArgsConstructor
public class UserDaoImpl implements Dao<Long, UserEntity> {

    private static final String SELECT_ALL = "SELECT user_id, full_name, passport_number, email FROM users";
    private static final String SELECT_ALL_LIMIT_OFFSET = SELECT_ALL + " LIMIT ? OFFSET ?";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE user_id = ?";
    private static final String INSERT = "INSERT INTO users (full_name, passport_number, email, password) VALUES(?,?,?,?)";
    private static final String UPDATE = "UPDATE users SET full_name = ?, passport_number = ?, email = ?, password = ? WHERE user_id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM users WHERE user_id =?";

    private final DataSource dataSource;


    @Override
    @SneakyThrows
    public List<UserEntity> findAll() {
        List<UserEntity> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_ALL);

            while (resultSet.next()) {
                users.add(UserEntity.builder()
                        .id(resultSet.getLong("user_id"))
                        .fullName(resultSet.getString("full_name"))
                        .passportNumber(resultSet.getString("passport_number"))
                        .email("email")
                        .build());
            }
            return users;
        }
    }

    @Override
    @SneakyThrows
    public List<UserEntity> findAll(int limit, int offset) {
        List<UserEntity> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_LIMIT_OFFSET)) {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(UserEntity.builder()
                        .id(resultSet.getLong("user_id"))
                        .fullName(resultSet.getString("full_name"))
                        .passportNumber(resultSet.getString("passport_number"))
                        .email("email")
                        .build());
            }
            return users;
        }
    }

    @Override
    @SneakyThrows
    public Optional<UserEntity> findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? Optional.of(UserEntity.builder()
                    .id(resultSet.getLong("user_id"))
                    .fullName(resultSet.getString("full_name"))
                    .passportNumber(resultSet.getString("passport_number"))
                    .email(resultSet.getString("email"))
                    .build())
                    : Optional.empty();
        }
    }

    @Override
    @SneakyThrows
    public Optional<UserEntity> save(UserEntity entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, entity.getFullName());
            preparedStatement.setString(2, entity.getPassportNumber());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setObject(4, entity.getPassword());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong("user_id"));
            }

            return Optional.of(entity);
        }
    }

    @Override
    @SneakyThrows
    public Optional<UserEntity> update(UserEntity entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {

            preparedStatement.setString(1, entity.getFullName());
            preparedStatement.setString(2, entity.getPassportNumber());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setObject(4, entity.getPassword());
            preparedStatement.setLong(5, entity.getId());
            preparedStatement.executeUpdate();

            return Optional.of(entity);
        }
    }

    @Override
    @SneakyThrows
    public void delete(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }
}
