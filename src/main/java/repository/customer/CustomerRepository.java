package repository.customer;

import model.Book;
import model.User;

public interface CustomerRepository {

    public void buyBook( User epmployee , User user , Book book);

}
