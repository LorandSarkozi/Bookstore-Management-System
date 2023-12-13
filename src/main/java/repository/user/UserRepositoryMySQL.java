package repository.user;
import model.Book;
import model.User;
import model.builder.BookBuilder;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.USER;
import static java.util.Collections.singletonList;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM user;";

        List<User> users = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                users.add(getUserFromResultSet(resultSet));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    // SQL Injection Attacks should not work after fixing functions
    // Be careful that the last character in sql injection payload is an empty space
    // alexandru.ghiurutan95@gmail.com' and 1=1; --
    // ' or username LIKE '%admin%'; --

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {

        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();

            String fetchUserSql =
                    "Select * from `" + USER + "` where `username`=\'" + username + "\' and `password`=\'" + password + "\'";
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);
            if (userResultSet.next())
            {
                User user = new UserBuilder()
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();

                findByUsernameAndPasswordNotification.setResult(user);
            } else {
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                return findByUsernameAndPasswordNotification;
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");
        }

        return findByUsernameAndPasswordNotification;
    }

    @Override
    public boolean save(User user) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            user.setId(userId);

            rightsRolesRepository.addRolesToUser(user, user.getRoles());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByUsername(String email) {
        try {
            Statement statement = connection.createStatement();

            String fetchUserSql =
                    "Select * from `" + USER + "` where `username`=\'" + email + "\'";
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);
            return userResultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new UserBuilder()
                .setId(resultSet.getLong("id"))
                .setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .build();
    }

    public boolean deleteUserById(Long id){
        try {
            String sql = "DELETE FROM user WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);

            int rowsDeleted = statement.executeUpdate();

            return (rowsDeleted == 1);

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean updateUser(User user){
        try{
            String sql = "UPDATE user SET username = ?, password = ? WHERE id = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,user.getUsername());
            statement.setString(2,user.getPassword());
            statement.setLong(3,user.getId());
            rightsRolesRepository.deleteRoles(user);
            rightsRolesRepository.addRolesToUser(user, user.getRoles());

            int rowsUpdated = statement.executeUpdate();
            return (rowsUpdated == 1);

        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }



}