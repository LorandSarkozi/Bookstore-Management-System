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

    public void buyBook(Book book, User user){

        String sql = "INSERT INTO bought_books(user_id,book_id) VALUES(?,?);";
        try{

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,user.getId());
            preparedStatement.setLong(2,book.getId());

            int rowsInserted = preparedStatement.executeUpdate();


        } catch (SQLException e){
            e.printStackTrace();
        }
    }


}
