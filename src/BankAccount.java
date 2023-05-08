
public class BankAccount {

    // stores user's bankNumber
    private String bankNumber;
    // stores user's pin code
    private int pinCode;
    // stores user's balance
    private double balance;
    // stores User variable (name)
    private User user;

    // Constructor for BankAccount class
    // gets bankNumber, pinCode, balance, and user as parameter and stores them in member variables
    public BankAccount(String bankNumber, int pinCode, double balance, User user)
    {
        this.bankNumber = bankNumber;
        this.pinCode = pinCode;
        this.balance = balance;
        this.user = user;
    }

    // Getter that returns user's bank number
    public String getBankNumber()
    {
        return this.bankNumber;
    }

    // Getter that returns user's pin code
    public int getPinCode()
    {
        return this.pinCode;
    }

    // Getter that returns the current balance
    public double getBalance()
    {
        return this.balance;
    }

    // Getter that returns user's name
    public String getUserName() { return this.user.getName(); }

    // Setter that modifies the balance
    // Used for deposit, withdraw, and transfer function
    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    // Setter that modifies the pinCode
    public void setPinCode(int pinCode) { this.pinCode = pinCode; }
}
