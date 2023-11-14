package repository.book;

public abstract class EBookRepositoryDecorator implements EBookRepository{

    protected EBookRepository decoratedRepository;

    public EBookRepositoryDecorator(EBookRepository eBookRepository){
        this.decoratedRepository = eBookRepository;
    }
}