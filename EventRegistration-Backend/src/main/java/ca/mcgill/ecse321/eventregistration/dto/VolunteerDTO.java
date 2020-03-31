package ca.mcgill.ecse321.eventregistration.dto;

import java.util.Set;

public class VolunteerDTO extends PersonDto {
    private Set<EventDto> eventsDto;
    
    public Set<EventDto> getEventsDto() {
        return eventsDto;
    }
    
    public void setEventsDto(Set<EventDto> eventsDto) {
        this.eventsDto = eventsDto;
    }
    
    @Override
    public String toString() {
        return "VolunteerDTO{" +
                "eventDtos=" + eventsDto +
                '}';
    }
}
