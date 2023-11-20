package service.ebook;

import model.EBook;

import java.util.List;

public interface EBookService {

    List<EBook> findAll();
    EBook findById(Long id);

    boolean save(EBook book);

    int getAgeOfBook(Long id);

    String getFormatOfEBook(Long id);
}