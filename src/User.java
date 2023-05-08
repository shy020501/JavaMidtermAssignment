public class User {
    // stores user's name
    private String name;

    // Constructor for User class
    // gets name of the user as parameter and saves in member variable 'name'
    public User(String name)
    {
        this.name = name;
    }

    // Getter that returns name of the user
    public String getName()
    {
        return this.name;
    }
}
