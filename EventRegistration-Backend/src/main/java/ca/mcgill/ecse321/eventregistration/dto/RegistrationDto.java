package ca.mcgill.ecse321.eventregistration.dto;

public class RegistrationDto {
	
	private PersonDto person;
	private EventDto event;
	private CreditCardDto creditCardDto;
	
	public RegistrationDto() {
	}
	
	public RegistrationDto(PersonDto person, EventDto event, CreditCardDto creditCardDto) {
		this.person = person;
		this.event = event;
		this.creditCardDto = creditCardDto;
	}
	
	public RegistrationDto(PersonDto person, EventDto event) {
		this.person = person;
		this.event = event;
	}
	
	public EventDto getEvent() {
		return event;
	}
	
	public void setEvent(EventDto event) {
		this.event = event;
	}
	
	public PersonDto getPerson() {
		return person;
	}
	
	public void setPerson(PersonDto person) {
		this.person = person;
	}
	
	public CreditCardDto getCreditCardDto() {
		return creditCardDto;
	}
	
	public void setCreditCardDto(CreditCardDto creditCardDto) {
		this.creditCardDto = creditCardDto;
	}
	
	@Override
	public String toString() {
		return "RegistrationDto{" +
				"person=" + person +
				", event=" + event +
				", creditCardDto=" + creditCardDto +
				'}';
	}
}
