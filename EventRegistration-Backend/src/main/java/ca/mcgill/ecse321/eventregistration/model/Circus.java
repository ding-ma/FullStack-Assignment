package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.Entity;


@Entity
public class Circus extends Event{
    private String company;
    
    public String getCompany() {
        return company;
    }
    
    public void setCompany(String company) {
        this.company = company;
    }
    
    @Override
    public String toString() {
        return "Circus{" +
                "name='" + getName() + '\'' +
                "date='" + getDate()+ '\'' +
                "startTime='" + getStartTime() + '\'' +
                "endTime='" + getEndTime() + '\'' +
                "company='" + company + '\'' +
                '}';
    }
}
