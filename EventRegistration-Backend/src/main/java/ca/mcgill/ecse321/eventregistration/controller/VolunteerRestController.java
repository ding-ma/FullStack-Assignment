package ca.mcgill.ecse321.eventregistration.controller;

import ca.mcgill.ecse321.eventregistration.dto.EventDto;
import ca.mcgill.ecse321.eventregistration.dto.VolunteerDTO;
import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Volunteer;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
public class VolunteerRestController {
    @Autowired
    private EventRegistrationService service;
    
    @PostMapping(value = {"/volunteer/{name}","/volunteer/{name}"})
    public VolunteerDTO createVolunteer(@PathVariable("name") String name) throws  IllegalArgumentException{
        Volunteer volunteer = service.createVolunteer(name);
        return convertDto(volunteer);
    }
    
    private VolunteerDTO convertDto(Volunteer volunteer) {
        Set<Event> volunteeringEvents = volunteer.getVolunteersFor();
        List<EventDto> eventDtos = new ArrayList<>();
        for (Event e : volunteeringEvents){
            eventDtos.add(convertToDto(e));
        }
        return new VolunteerDTO(volunteer.getName(),eventDtos);
    }
    
    private EventDto convertToDto(Event e) {
        if (e == null) {
            throw new IllegalArgumentException("There is no such Event!");
        }
        return new EventDto(e.getName(), e.getDate(), e.getStartTime(), e.getEndTime());
    }
    
}
