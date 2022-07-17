package com.hkxps17.turnup;

public class Event {

    String eventTitle;
    String eventLocation;
    String eventDate;
    String eventRating;
    String eventCategory;
    String eventDescription;
    String eventImage;

    public Event(String eventTitle, String eventLocation, String eventDate, String eventDescription,
                 String eventImage, String eventRating, String eventCategory) {
        this.eventTitle = eventTitle;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
        this.eventImage = eventImage;
        this.eventRating = eventRating;
        this.eventCategory = eventCategory;
    }

}
