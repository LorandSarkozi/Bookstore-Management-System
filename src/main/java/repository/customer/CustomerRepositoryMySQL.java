package repository.customer;

import model.Book;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public List<Book> findAllBooksByEmployeeId(Long id) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM bought_books WHERE employee_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                Book book ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
        }



}
