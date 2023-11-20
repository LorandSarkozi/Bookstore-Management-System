package model;

import model.Book;

public class EBook extends Book {
    private String format;


    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    @Override
    public String toString() {
        return String.format("EBook author: %s | title: %s | format: %s | Published Date: %s.", this.getAuthor(), this.getTitle(),format, this.getPublishedDate());
    }



}
