package com.kmd.uog.logbook.database;

public class Contact {

    private int id;
    private String imageFilePath; // Changed the type to String for image file path
    private String name;
    private String date;
    private String email;

    public Contact() {

    }

    public Contact(int id, String name, String date, String email) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getEmail() {
        return email;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }
}
