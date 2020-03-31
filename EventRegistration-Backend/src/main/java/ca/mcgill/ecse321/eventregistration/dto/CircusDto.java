package ca.mcgill.ecse321.eventregistration.dto;

public class CircusDto extends EventDto {
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
                "company='" + company + '\'' +
                '}';
    }
}
