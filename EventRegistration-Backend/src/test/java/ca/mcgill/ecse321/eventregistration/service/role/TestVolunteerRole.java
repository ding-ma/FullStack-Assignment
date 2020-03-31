package ca.mcgill.ecse321.eventregistration.service.role;

import ca.mcgill.ecse321.eventregistration.dao.EventRepository;
import ca.mcgill.ecse321.eventregistration.dao.PersonRepository;
import ca.mcgill.ecse321.eventregistration.dao.RegistrationRepository;
import ca.mcgill.ecse321.eventregistration.dao.VolunteerRepository;
import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Volunteer;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestVolunteerRole {
    @Autowired
    private EventRegistrationService service;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;

    @After
    public void clearDatabase() {
        // First, we clear registrations to avoid exceptions due to inconsistencies
        registrationRepository.deleteAll();
        // Then we can clear the other tables
        personRepository.deleteAll();
        eventRepository.deleteAll();
        volunteerRepository.deleteAll();
    }

    @Test
    public void test_01_CreateVolunteer() {
        try {
            String name = "validname";
            service.createVolunteer(name);
            List<Volunteer> volunteers = service.getAllVolunteers();
            assertEquals(volunteers.size(), 1);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void test_02_CreateVolunteerWithEmptyName() {
        try {
            String name = "";
            service.createVolunteer(name);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Volunteer name cannot be empty!", e.getMessage());
            List<Volunteer> volunteers = service.getAllVolunteers();
            assertEquals(volunteers.size(), 0);
        }
    }

    @Test
    public void test_04_CreateVolunteerDuplicate() {
        try {
            String name = "validname";
            service.createVolunteer(name);
            List<Volunteer> volunteers = service.getAllVolunteers();
            assertEquals(volunteers.size(), 1);
        } catch (IllegalArgumentException e) {
            fail();
        }
    
        try {
            String name = "validname";
            service.createVolunteer(name);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Volunteer has already been created!", e.getMessage());
            List<Volunteer> volunteers = service.getAllVolunteers();
            assertEquals(volunteers.size(), 1);
        }
    }
    
    //todo find out wtf is getVolunteersFor
    @Test
    public void test_05_VolunteersEvent() {
        try {
            Volunteer Volunteer = service.createVolunteer("validname");
            Event event = VolunteerRoleTestData.setupEvent(service, "eventname");
            service.volunteersEvent(Volunteer, event);
            assertEquals(Volunteer.getVolunteersFor().size(), 1);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }
    
    @Test
    public void test_06_VolunteersEventWithNullVolunteer() {
        try {
            Volunteer Volunteer = null;
            Event event = VolunteerRoleTestData.setupEvent(service, "eventname");
            service.volunteersEvent(Volunteer, event);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Volunteer needs to be selected for volunteers!", e.getMessage());
        }
    }

    @Test
    public void test_09_VolunteersEventWithNonExsistantEvent() {
        try {
            Volunteer Volunteer = service.createVolunteer("validname");
            Event event = new Event();
            event.setName("concert");
            service.volunteersEvent(Volunteer, event);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Event does not exist!", e.getMessage());
        }
    }

    @Test
    public void test_10_GetAllVolunteers() {
        try {
            service.createVolunteer("validname1");
            service.createVolunteer("validname2");
            List<Volunteer> volunteers = service.getAllVolunteers();
            assertEquals(volunteers.size(), 2);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void test_11_GetVolunteer() {
        try {
            service.createVolunteer("Volunteer");
            service.getVolunteer("Volunteer");
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void test_12_GetVolunteerWithNullName() {
        try {
            service.getVolunteer(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Person name cannot be empty!", e.getMessage());
        }
    }
}
