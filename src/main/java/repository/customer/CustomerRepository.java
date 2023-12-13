package repository.customer;

import model.Book;
import model.User;

import java.util.List;

public interface CustomerRepository {

    public void buyBook( User epmployee , User user , Book book);

    List<Book> findAllBooksByEmployeeId(Long id);


}
