package repository.book;

import model.Book;
import model.EBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EBookRepositoryMock implements EBookRepository{

    private final List<EBook> books;


    public EBookRepositoryMock(){
        books = new ArrayList<>();
    }

    @Override
    public List<EBook> findAll() {
        return books;
    }


    @Override
    public Optional<EBook> findById(Long id) {
        return books.parallelStream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean save(EBook book) {
        return books.add(book);
    }

    @Override
    public void removeAll() {
        books.clear();
    }
}