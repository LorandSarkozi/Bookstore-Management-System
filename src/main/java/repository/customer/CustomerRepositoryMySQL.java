package repository.customer;

import model.Book;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerRepositoryMySQL implements CustomerRepository {

    private final Connection connection;

    public CustomerRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    public void buyBook(User employee,User user , Book book ){

        String sql = "INSERT INTO bought_books(employee_id,user_id,book_id) VALUES(?,?,?);";
        try{

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,employee.getId());
            preparedStatement.setLong(2,user.getId());
            preparedStatement.setLong(3,book.getId());

            preparedStatement.executeUpdate();


        } catch (SQLException e){
            e.printStackTrace();
        }
    }


}
