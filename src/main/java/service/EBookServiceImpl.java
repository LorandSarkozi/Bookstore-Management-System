package service;

import model.EBook;
import repository.book.EBookRepository;
import repository.book.BookRepositoryMySQL;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public abstract class EBookServiceImpl implements EBookService{
    private final EBookRepository eBookRepository;

    public EBookServiceImpl(EBookRepository eBookRepository){
        this.eBookRepository = eBookRepository;
    }

    @Override
    public List<EBook> findAll() {
        return eBookRepository.findAll();
    }

    @Override
    public EBook findById(Long id) {
        return eBookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id: %d not found".formatted(id)));
    }

    @Override
    public boolean save(EBook book) {
        return eBookRepository.save(book);
    }

    @Override
    public int getAgeOfBook(Long id) {
        EBook book = this.findById(id);

        LocalDate now = LocalDate.now();

        return (int) ChronoUnit.YEARS.between(book.getPublishedDate(), now);
    }

    public String getFormatOfBook(Long id){
        EBook book=this.findById(id);

        return book.getFormat();
    }
}