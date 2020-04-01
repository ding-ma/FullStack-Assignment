package ca.mcgill.ecse321.eventregistration.dto;

import java.util.List;
import java.util.Set;

public class VolunteerDTO extends PersonDto {
    private List<EventDto> eventsDto;
    
    public VolunteerDTO() {
    
    }
    
    public VolunteerDTO(String name,  List<EventDto> volunteeringEvents) {
        super(name);
        this.eventsDto = volunteeringEvents;
    }
    
    public List<EventDto> getEventsDto() {
        return eventsDto;
    }
    
    public void setEventsDto(List<EventDto> eventsDto) {
        this.eventsDto = eventsDto;
    }
    
    @Override
    public String toString() {
        return "VolunteerDTO{" +
                "eventDtos=" + eventsDto +
                "name='" + getName() + '\'' +
                '}';
    }
}
