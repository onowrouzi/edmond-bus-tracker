package edu.uco.edmond.bus.tracker.Dtos;

public class Notification extends Dto {
    
    private int id;
    private String text;
    private User sender;
    
    public Notification(int id, String text, User sender) {
        this.id = id;
        this.text = text;
        this.sender = sender;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
