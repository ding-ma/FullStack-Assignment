package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Volunteer extends Person{
    private Event event;
    
    @ManyToOne(optional = false)
    public Event getEvent() {
        return event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }
}
