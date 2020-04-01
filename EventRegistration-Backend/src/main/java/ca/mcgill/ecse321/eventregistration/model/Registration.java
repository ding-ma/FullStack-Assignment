package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Registration {
	private CreditCard creditCard;
	private int id;
	private Person person;
	private Event event;
	
	@OneToOne
	public CreditCard getCreditCard() {
		return creditCard;
	}
	
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	
	public void setId(int value) {
		this.id = value;
	}

	@Id
	public int getId() {
		return this.id;
	}

	@ManyToOne(optional = false)
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	
	@ManyToOne(optional = false)
    public Event getEvent() {
        return this.event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }
    
    @Override
    public String toString() {
        return "Registration{" +
                "creditCard=" + creditCard +
                ", id=" + id +
                ", person=" + person +
                ", event=" + event +
                '}';
    }
}
