package repository.book;

import model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository{

    private final List<Book> books;


    public BookRepositoryMock(){
        books = new ArrayList<>();
    }

    @Override
    public List<Book> findAll() {
        return books;
    }


    @Override
    public Optional<Book> findById(Long id) {
        return books.parallelStream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean save(Book book) {
        return books.add(book);
    }

    @Override
    public void removeAll() {
        books.clear();
    }

    @Override
    public void updateBookQuantity(Book book) {

    }

    @Override
    public boolean deleteBookById(Long id){
        Optional<Book> bookToRemove = findById(id);

        if (bookToRemove.isPresent()) {
            return books.remove(bookToRemove.get());
        }
        return false;
    }
}