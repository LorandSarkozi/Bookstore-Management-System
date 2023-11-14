package repository.book;

import model.EBook;

import java.util.List;
import java.util.Optional;

public class EBookRepositoryCacheDecorator extends EBookRepositoryDecorator{
    private Cache<EBook> cache;

    public EBookRepositoryCacheDecorator(EBookRepository eBookRepository, Cache<EBook> cache){
        super(eBookRepository);
        this.cache = cache;
    }
    @Override
    public List<EBook> findAll() {
        if(cache.hasResult()){
            return cache.load();
        }

        List<EBook> books = decoratedRepository.findAll();
        cache.save(books);
        return books;
    }

    @Override
    public Optional<EBook> findById(Long id) {
        return decoratedRepository.findById(id);
    }

    @Override
    public boolean save(EBook book) {
        cache.invalidateCache();
        return decoratedRepository.save(book);
    }

    @Override
    public void removeAll() {
        cache.invalidateCache();
        decoratedRepository.removeAll();
    }
}