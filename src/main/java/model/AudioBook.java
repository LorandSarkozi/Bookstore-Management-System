package model;

import java.util.Date;

public class AudioBook extends Book{


    private int runTime;



    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }



    public int getRunTime() {
        return runTime;
    }



    @Override
    public String toString() {
        return String.format("AudioBook author: %s | title: %s | runtime: %d | Published Date: %s.", this.getAuthor(), this.getTitle(),runTime, this.getPublishedDate());
    }
}
