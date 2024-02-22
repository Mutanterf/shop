import java.util.ArrayList;
import java.util.List;


abstract class User {
    private String username;
    private String password;
    private int userId;
    private String email;


    public User(String username, String password, int userId, String email) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public abstract void displayInfo();


    public int getUserId() {
        return userId;
    }

    public void setCustomerId(int userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}

class Customer extends User {
    public Customer(String username, String password, int userId, String email) {
        super(username, password, userId, email);
    }

    @Override
    public void displayInfo() {
        System.out.println("Customer Information:");
        System.out.println("Username: " + getUsername());
    }

    public void displayCart(int customer_id) {
        app.displayUserProducts(customer_id);
    }
    @Override
    public void buyProduct(int product_id, int quantity) {
        super.buyProduct(product_id, quantity);
    }

}

class Staff extends User {

    private final double discount;

    public Staff(String username, String password, int userId, String email, double discount) {
        super(username, password, userId, email);
        this.discount = discount;
    }

    @Override
    public void displayInfo() {
        System.out.println("Staff Information:");
        System.out.println("Username: " + getUsername());
    }

    public double getDiscount() {
        return discount;
    }

    @Override
    public void buyProduct(int product_id, int quantity) {
        app.addToCart(1, product_id, quantity);
    }

}