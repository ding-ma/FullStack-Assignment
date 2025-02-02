package ca.mcgill.ecse321.eventregistration.controller;

import ca.mcgill.ecse321.eventregistration.dao.EventRepository;
import ca.mcgill.ecse321.eventregistration.dao.PersonRepository;
import ca.mcgill.ecse321.eventregistration.dao.RegistrationRepository;
import ca.mcgill.ecse321.eventregistration.dao.VolunteerRepository;
import ca.mcgill.ecse321.eventregistration.dto.*;
import ca.mcgill.ecse321.eventregistration.model.*;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
public class EventRegistrationRestController {
	
	@Autowired
	private EventRegistrationService service;
	@Autowired
	private RegistrationRepository registrationRepository;
	@Autowired
	private VolunteerRepository volunteerRepository;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private PersonRepository personRepository;
	
	// POST Mappings
	
	// @formatter:off
	// Turning off formatter here to ease comprehension of the sample code by
	// keeping the linebreaks
	// Example REST call:
	// http://localhost:8088/persons/John
	@PostMapping(value = {"/persons/{name}", "/persons/{name}/"})
	public PersonDto createPerson(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Person person = service.createPerson(name);
		return convertToDto(person);
	}

	// @formatter:off
	// Example REST call:
	// http://localhost:8080/events/testevent?date=2013-10-23&startTime=00:00&endTime=23:59
	@PostMapping(value = { "/events/{name}", "/events/{name}/" })
	public CircusDto createEvent(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime)
			throws IllegalArgumentException {
		// @formatter:on
		if(startTime == null || endTime == null)
			throw  new IllegalArgumentException("Time can't be empty! ");
		Event event = service.createEvent(name, date, Time.valueOf(startTime), Time.valueOf(endTime));
		return convertToEventDToToCircusDTO(event);
	}
	
	private CircusDto convertToEventDToToCircusDTO(Event event){
		//String name, Date date, Time startTime, Time endTime, String company
		return new CircusDto(event.getName(), event.getDate(),event.getStartTime(),event.getEndTime(),"--");
	}
	// @formatter:off
	@PostMapping(value = { "/register", "/register/" })
	public RegistrationDto registerPersonForEvent(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// @formatter:on

		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.register(p, e);
		return convertToDto(r, p, e);
	}

	// GET Mappings

	@GetMapping(value = { "/events", "/events/" })
	public List<CircusDto> getAllEvents() {
		List<CircusDto> eventDtos = new ArrayList<>();
		for (Event event : service.getAllEvents()) {
			eventDtos.add(convertEventToCircus(event));
		}
		return eventDtos;
	}
	
	private CircusDto convertEventToCircus(Event event){
		return new CircusDto(event.getName(), event.getDate(), event.getStartTime(),event.getEndTime(),"--");
	}
	// Example REST call:
	// http://localhost:8088/events/person/JohnDoe
	@GetMapping(value = { "/events/person/{name}", "/events/person/{name}/" })
	public List<EventDto> getEventsOfPerson(@PathVariable("name") PersonDto pDto) {
		Person p = convertToDomainObject(pDto);
		return createAttendedEventDtosForPerson(p);
	}

	@GetMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto getPersonByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getPerson(name));
	}

	@GetMapping(value = { "/registrations", "/registrations/" })
	public RegistrationDto getRegistration(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.getRegistrationByPersonAndEvent(p, e);
		return convertToDtoWithoutPerson(r);
	}

	@GetMapping(value = { "/registrations/person/{name}", "/registrations/person/{name}/" })
	public List<RegistrationDto> getRegistrationsForPerson(@PathVariable("name") PersonDto pDto)
			throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		return createRegistrationDtosForPerson(p);
	}

	@GetMapping(value = { "/persons", "/persons/" })
	public List<PersonDto> getAllPersons() {
		List<PersonDto> persons = new ArrayList<>();
		for (Person person : service.getAllPersons()) {
			persons.add(convertToDto(person));
		}
		return persons;
	}
	
	@GetMapping(value = {"/events/{name}", "/events/{name}/"})
	public EventDto getEventByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getEvent(name));
	}
	
	/*
	
	
	
	COMPANY
	
	
	 */
	
	@PostMapping(value = { "/events/{name}/company/{cName}", "/events/{name}/company/{cName}/" })
	public CircusDto createEventWithCompany(@PathVariable("name") String name, @PathVariable("cName") String cName, @RequestParam Date date,
										   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
								@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime)
			throws IllegalArgumentException {
		// @formatter:on
		if(startTime == null || endTime == null)
			throw  new IllegalArgumentException("Time can't be empty! ");
		Circus circus = service.createCircus(name, date, Time.valueOf(startTime) , Time.valueOf(endTime), cName);
		return convertToDTO(circus);
	}
	
	@GetMapping(value = {"/events/companies", "/events/companies/"})
	public List<CircusDto> getAllCompaniesEvents(){
		List<CircusDto> eventDtoList = new ArrayList<>();
		for(Circus e: service.getAllCircuses()){
			eventDtoList.add(convertToDTO(e));
		}
		return eventDtoList;
	}
	/*
	
	
	VOLUNTEER
	
	
	 */
	
	@PostMapping(value = {"/volunteer/{name}", "/volunteer/{name}"})
	public VolunteerDTO createVolunteer(@PathVariable("name") String name) throws IllegalArgumentException {
		Volunteer volunteer = service.createVolunteer(name);
		return convertToDto(volunteer);
	}
	
	@PostMapping(value = {"/volunteer/{name}/event/{eventName}", "/volunteer/{name}/event/{eventName}"})
	public VolunteerDTO registerVolunteerForEvent(@PathVariable String name, @PathVariable String eventName) {
		Volunteer volunteer = volunteerRepository.findVolunteerByName(name);
		Event event = eventRepository.findByName(eventName);
		return convertToDTO(service.volunteersEvent(volunteer, event));
	}
	
	@GetMapping(value = {"/volunteers", "volunteers/"})
	public List<VolunteerDTO> getAllVolunteers() {
		List<VolunteerDTO> volunteerDTOS = new ArrayList<>();
		for (Volunteer volunteer : service.getAllVolunteers()) {
			volunteerDTOS.add(convertToDTO(volunteer));
		}
		return volunteerDTOS;
	}
	
	
	/*


	PAYMENTS

	every registration has one credit card
	 */
	
	@PostMapping(value = {"/payment/{person}/{event}", "/payment/{person}/{event}/"})
	public RegistrationDto createPayment(@PathVariable String person, @PathVariable String event, @RequestParam String accountNumber, @RequestParam int amount) throws IllegalArgumentException {
		CreditCard creditCard = service.createCreditCardPay(accountNumber, amount);
		Registration registration = registrationRepository.findByPersonNameAndEvent_Name(person, event);
		registration = service.pay(registration, creditCard);
		return convertToDTO(registration);
	}
	
	@GetMapping(value = {"/payment/{name}/{eventName}", "/payment/{name}/{eventName}/"})
	public RegistrationDto getPayment(@PathVariable String name, @PathVariable String eventName){
		Person person = personRepository.findByName(name);
		Event event = eventRepository.findByName(eventName);
		return  convertToDTO(service.getRegistrationByPersonAndEvent(person,event));
	}
	
	
	
	
	
	// Model - DTO conversion methods (not part of the API)
	
	private CircusDto convertToDTO(Circus circus){
		return new CircusDto(circus.getName(), circus.getDate(), circus.getStartTime(),circus.getEndTime(),circus.getCompany());

	}
	
	private VolunteerDTO convertToDTO(Volunteer volunteer){
		Person person = personRepository.findByName(volunteer.getName());
		return new VolunteerDTO(volunteer.getName(),createAttendedEventDtosForPerson(person));
	}
	
	
	private RegistrationDto convertToDTO(Registration registration) {
		PersonDto personDto = convertToDto(registration.getPerson());
		EventDto eventDto = convertToDto(registration.getEvent());
		CreditCardDto creditCardDto = convertToDto(registration.getCreditCard());
		return new RegistrationDto(personDto,eventDto,creditCardDto);
	}
	
	
	private VolunteerDTO convertToDto(Volunteer volunteer) {
		Set<Event> volunteeringEvents = volunteer.getVolunteersFor();
		List<EventDto> eventDtos = new ArrayList<>();
		if(volunteeringEvents != null){
		for (Event e : volunteeringEvents) {
			eventDtos.add(convertToDto(e));
		}
		} else{
			eventDtos = null;
		}
		return new VolunteerDTO(volunteer.getName(), eventDtos);
	}
	
	private CreditCardDto convertToDto(CreditCard creditCard){
		if(creditCard == null){
			throw new IllegalArgumentException("There is no such CreditCard!");
		}
		return new CreditCardDto(creditCard.getAccountNumber(), creditCard.getAmount());
	}
	

	
	private EventDto convertToDto(Event e) {
		if (e == null) {
			throw new IllegalArgumentException("There is no such Event!");
		}
		EventDto eventDto = new EventDto(e.getName(), e.getDate(), e.getStartTime(), e.getEndTime());
		return eventDto;
	}

	private PersonDto convertToDto(Person p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Person!");
		}
		PersonDto personDto = new PersonDto(p.getName());
		personDto.setEventsAttended(createAttendedEventDtosForPerson(p));
		return personDto;
	}

	// DTOs for registrations
	private RegistrationDto convertToDto(Registration r, Person p, Event e) {
		EventDto eDto = convertToDto(e);
		PersonDto pDto = convertToDto(p);
		return new RegistrationDto(pDto, eDto);
	}

	private RegistrationDto convertToDto(Registration r) {
		EventDto eDto = convertToDto(r.getEvent());
		PersonDto pDto = convertToDto(r.getPerson());
		CreditCardDto cDto = convertToDto(r.getCreditCard());
		RegistrationDto rDto = new RegistrationDto(pDto, eDto, cDto);
		return rDto;
	}

	// return registration dto without peron object so that we are not repeating
	// data
	private RegistrationDto convertToDtoWithoutPerson(Registration r) {
		RegistrationDto rDto = convertToDto(r);
		rDto.setPerson(null);
		return rDto;
	}

	private Person convertToDomainObject(PersonDto pDto) {
		List<Person> allPersons = service.getAllPersons();
		for (Person person : allPersons) {
			if (person.getName().equals(pDto.getName())) {
				return person;
			}
		}
		return null;
	}

	// Other extracted methods (not part of the API)

	private List<EventDto> createAttendedEventDtosForPerson(Person p) {
		List<Event> eventsForPerson = service.getEventsAttendedByPerson(p);
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsForPerson) {
			events.add(convertToDto(event));
		}
		return events;
	}

	private List<RegistrationDto> createRegistrationDtosForPerson(Person p) {
		List<Registration> registrationsForPerson = service.getRegistrationsForPerson(p);
		List<RegistrationDto> registrations = new ArrayList<RegistrationDto>();
		for (Registration r : registrationsForPerson) {
			registrations.add(convertToDtoWithoutPerson(r));
		}
		return registrations;
	}
}
