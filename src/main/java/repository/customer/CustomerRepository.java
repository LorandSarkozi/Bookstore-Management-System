package repository.customer;

import model.Book;
import model.User;

public interface CustomerRepository {

    public void buyBook(Book book, User user);



}
