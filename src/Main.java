import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<BankAccount> bankAccounts = new ArrayList<>();

        // Create User objects
        User user1 = new User("Kevin");
        User user2 = new User("Paul");
        User user3 = new User("Eric");
        User user4 = new User("Michael");

        // Create BankAccount objects
        bankAccounts.add(new BankAccount("1111111111111", 1111, 100000.0, user1));
        bankAccounts.add(new BankAccount("2222222222222", 2222, 200000.0, user2));
        bankAccounts.add(new BankAccount("3333333333333", 3333, 300000.0, user3));
        bankAccounts.add(new BankAccount("4444444444444", 4444, 400000.0, user4));

        new MainFrame(bankAccounts);
    }
}
