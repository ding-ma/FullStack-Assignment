package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Volunteer extends Person {
    
    private Set<Event> events;
    
    @ManyToMany
    public Set<Event> getVolunteersFor() {
        return this.events;
    }
    
    public void setVolunteersFor(Set<Event> eventss) {
        this.events = eventss;
    }
    
    @Override
    public String toString() {
        return "Volunteer{" +
                "events=" + events +
                '}';
    }
}
