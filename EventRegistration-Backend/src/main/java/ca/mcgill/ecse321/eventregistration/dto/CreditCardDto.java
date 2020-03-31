package ca.mcgill.ecse321.eventregistration.dto;


public class CreditCardDto {
    private int amount;
    private String accountNumber;
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    @Override
    public String toString() {
        return "CreditCardDto{" +
                "amount=" + amount +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }
}
