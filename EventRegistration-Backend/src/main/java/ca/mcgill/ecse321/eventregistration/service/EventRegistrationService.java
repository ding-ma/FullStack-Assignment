package ca.mcgill.ecse321.eventregistration.service;

import ca.mcgill.ecse321.eventregistration.dao.*;
import ca.mcgill.ecse321.eventregistration.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventRegistrationService {
    
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private CircusRepository circusRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    
    @Transactional
    public Person createPerson(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Person name cannot be empty!");
        } else if (personRepository.existsById(name)) {
            throw new IllegalArgumentException("Person has already been created!");
        }
        Person person = new Person();
        person.setName(name);
		personRepository.save(person);
		return person;
	}


	@Transactional
	public Person getPerson(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		}
		Person person = personRepository.findByName(name);
		return person;
	}

	@Transactional
	public List<Person> getAllPersons() {
		return toList(personRepository.findAll());
	}

	@Transactional
	public Event buildEvent(Event event, String name, Date date, Time startTime, Time endTime) {
		// Input validation
		String error = "";
		if (name == null || name.trim().length() == 0) {
			error = error + "Event name cannot be empty! ";
		} else if (eventRepository.existsById(name)) {
			throw new IllegalArgumentException("Event has already been created!");
		}
		if (date == null) {
			error = error + "Event date cannot be empty! ";
		}
		if (startTime == null) {
			error = error + "Event start time cannot be empty! ";
		}
		if (endTime == null) {
			error = error + "Event end time cannot be empty! ";
		}
		if (endTime != null && startTime != null && endTime.before(startTime)) {
			error = error + "Event end time cannot be before event start time!";
		}
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		event.setName(name);
		event.setDate(date);
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		return event;
	}

	@Transactional
	public Event createEvent(String name, Date date, Time startTime, Time endTime) {
		Event event = new Event();
		buildEvent(event, name, date, startTime, endTime);
		eventRepository.save(event);
		return event;
	}

	@Transactional
	public Event getEvent(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Event name cannot be empty!");
		}
		Event event = eventRepository.findByName(name);
		return event;
	}

	// This returns all objects of instance "Event" (Subclasses are filtered out)
	@Transactional
	public List<Event> getAllEvents() {
		return toList(eventRepository.findAll()).stream().filter(e -> e.getClass().equals(Event.class)).collect(Collectors.toList());
	}

	@Transactional
	public Registration register(Person person, Event event) {
		String error = "";
		if (person == null) {
			error = error + "Person needs to be selected for registration! ";
		} else if (!personRepository.existsById(person.getName())) {
			error = error + "Person does not exist! ";
		}
		if (event == null) {
			error = error + "Event needs to be selected for registration!";
		} else if (!eventRepository.existsById(event.getName())) {
			error = error + "Event does not exist!";
		}
		if (registrationRepository.existsByPersonAndEvent(person, event)) {
			error = error + "Person is already registered to this event!";
		}

		error = error.trim();

		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}

		Registration registration = new Registration();
		registration.setId(person.getName().hashCode() * event.getName().hashCode());
		registration.setPerson(person);
		registration.setEvent(event);

		registrationRepository.save(registration);

		return registration;
	}

	@Transactional
	public List<Registration> getAllRegistrations() {
		return toList(registrationRepository.findAll());
	}

	@Transactional
	public Registration getRegistrationByPersonAndEvent(Person person, Event event) {
		if (person == null || event == null) {
			throw new IllegalArgumentException("Person or Event cannot be null!");
		}

		return registrationRepository.findByPersonAndEvent(person, event);
	}
	@Transactional
	public List<Registration> getRegistrationsForPerson(Person person){
		if(person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		return registrationRepository.findByPerson(person);
	}

	@Transactional
	public List<Registration> getRegistrationsByPerson(Person person) {
		return toList(registrationRepository.findByPerson(person));
	}

	@Transactional
	public List<Event> getEventsAttendedByPerson(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		List<Event> eventsAttendedByPerson = new ArrayList<>();
		for (Registration r : registrationRepository.findByPerson(person)) {
			eventsAttendedByPerson.add(r.getEvent());
		}
		return eventsAttendedByPerson;
	}

	private <T> List<T> toList(Iterable<T> iterable) {
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
	
	@Transactional
	public List<Circus> getAllCircuses() {
		return toList(circusRepository.findAll());
	}
	
	@Transactional
	public List<Volunteer> getAllVolunteers() {
		return toList(volunteerRepository.findAll());
	}
	
	@Transactional
	public Circus createCircus(String name, Date circusDate, Time startTime, Time endTime, String company) {
		String error = "";
		if (name == null || name.trim().length() == 0) {
			error = error + "Event name cannot be empty! ";
		} else if (eventRepository.existsById(name)) {
			throw new IllegalArgumentException("Event has already been created!");
		}
		if (circusDate == null) {
			error = error + "Event date cannot be empty! ";
		}
		if (startTime == null) {
			error = error + "Event start time cannot be empty! ";
		}
		if (endTime == null) {
			error = error + "Event end time cannot be empty! ";
		}
		if (endTime != null && startTime != null && endTime.before(startTime)) {
			error = error + "Event end time cannot be before event start time!";
		}
		if (company == null || company.trim().length() == 0) {
			error = error + "Circus company cannot be empty!";
		}
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		Circus circus = new Circus();
		circus.setName(name);
		circus.setDate(circusDate);
		circus.setStartTime(startTime);
		circus.setEndTime(endTime);
		circus.setCompany(company);
		circusRepository.save(circus);
		System.out.println("Service"+circus);
		System.out.println(company);
		return circus;
	}
	
	@Transactional
	public Volunteer getVolunteer(String name) {
        String error = "";
        if (name == null || name.trim().length() == 0) {
            error = error + "Person name cannot be empty! ";
        }
        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }
        return volunteerRepository.findVolunteerByName(name);
    }
	
	@Transactional
	public Volunteer createVolunteer(String name) {
        String error = "";
        if (name == null || name.trim().length() == 0) {
            error = error + "Volunteer name cannot be empty! ";
        } else if (volunteerRepository.findVolunteerByName(name) != null) {
            error = error + "Volunteer has already been created! ";
        }
		
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		Volunteer volunteer = new Volunteer();
		volunteer.setName(name);
		volunteerRepository.save(volunteer);
		return volunteer;
	}
	
	//todo: error handling, check if volunteer is already registered to event
	@Transactional
	public Volunteer volunteersEvent(Volunteer volunteer, Event event) {
		String error = "";
		if (volunteer == null) {
			error = error + "Volunteer needs to be selected for volunteers! ";
		}
		if (eventRepository.findByName(event.getName()) == null) { //event wasnt saved
			error = error + "Event does not exist!";
		}
		if (registrationRepository.existsByPersonAndEvent(volunteer, event)) {
			error += "Person is already registered to this event!";
		}
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		System.out.println(event);
		Set<Event> events;
		if (volunteer.getVolunteersFor() == null) {
			events = new HashSet<>();
		} else {
			System.out.println("not -n");
			//todo check if it was already added
			events = volunteer.getVolunteersFor();
		}
		events.add(event);
		volunteer.setVolunteersFor(events); //hmm quite stupid that you have to set it??
		volunteerRepository.save(volunteer);
		return volunteer;
	}
	
	//link the amount to the credit card
	@Transactional
	public CreditCard createCreditCardPay(String accountNumber, int amount) {
		String error = "";
		if (amount < 0) {
			error += "Payment amount cannot be negative! ";
		}
		//if it doesnt match the regex fail also.
		if (accountNumber == null || accountNumber.trim().length() == 0 || !accountNumber.matches("\\d{4}-\\d{4}")) {
			error += "Account number is null or has wrong format! ";
		}
		
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		
		CreditCard creditCard = creditCardRepository.findAllByAccountNumber(accountNumber);
		if (creditCard == null) {
			creditCard = new CreditCard();
			creditCard.setAccountNumber(accountNumber);
		}
		creditCard.setAmount(amount);
		creditCardRepository.save(creditCard);
		return creditCard;
	}
	
	//todo error handling
	//check if person is registered to even before paying
	@Transactional
	public Registration pay(Registration registration, CreditCard pay) {
		String error = "";
		if (pay == null || registration == null) {
			error += "Registration and payment cannot be null! ";
		}
		if (!registrationRepository.existsByPersonAndEvent(registration.getPerson(), registration.getEvent())) {
			error += "Person is not yet registered to the event! ";
		}
//		if(registration.getCreditCard() != null){
//			error += "You have already paid for the event! ";
//		}
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		registration.setCreditCard(pay);
		registrationRepository.save(registration);
		return registration;
	}
}
