import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class App {

    private final String url = "jdbc:postgresql://db-postgresql-ams3-47505-do-user-15053769-0.c.db.ondigitalocean.com:25060/defaultdb";
    private final String user = "doadmin";
    private final String password = "AVNS_w_VlxtdGYg344M9PbZC";

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public void displayProducts() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT p.name, p.price, i.quantity FROM Inventory i INNER JOIN Product p ON i.product_id = p.product_id")) {

            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                System.out.print("Name: " + name);
                System.out.print(", Price: " + price);
                System.out.println(", Quantity: " + quantity);
            }

        } catch (SQLException e) {
            System.out.println("Error displaying products: " + e.getMessage());
        }
    }



    public void createProduct(Product product) {
        try (Connection conn = connect()) {
            // Insert into the Product table
            String productSql = "INSERT INTO Product(name, price) VALUES (?, ?) RETURNING product_id";

            try (PreparedStatement productStatement = conn.prepareStatement(productSql)) {
                productStatement.setString(1, product.getProductName());
                productStatement.setDouble(2, product.getProductPrice());

                // Execute the statement and get the last inserted product_id
                try (ResultSet resultSet = productStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int productId = resultSet.getInt("product_id");
                        String inventorySql = "INSERT INTO Inventory(product_id, quantity) VALUES(?, ?)";
                        try(PreparedStatement inventoryStatement = conn.prepareStatement(inventorySql)) {
                            inventoryStatement.setInt(1, productId);
                            inventoryStatement.setInt(2, product.getProductQuantity());
                        }
                        // If the inserted product is a device
                        if (product instanceof Device) {
                            Device device = (Device) product;
                            // Insert into the Device table
                            String deviceSql = "INSERT INTO Device(product_id, os, screen_size) VALUES (?, ?, ?)";
                            try (PreparedStatement deviceStatement = conn.prepareStatement(deviceSql)) {
                                deviceStatement.setInt(1, productId);
                                deviceStatement.setString(2, device.getOs());
                                deviceStatement.setDouble(3, device.getScreenSize());
                                deviceStatement.executeUpdate();
                                System.out.println("Device created successfully.");
                            }
                        } else if (product instanceof Domestic) {
                            Domestic domestic = (Domestic) product;
                            // Insert into the Domestic table
                            String domesticSql = "INSERT INTO Domestic(product_id, can_connect_to_wifi) VALUES (?, ?)";
                            try (PreparedStatement domesticStatement = conn.prepareStatement(domesticSql)) {
                                domesticStatement.setInt(1, productId);
                                domesticStatement.setBoolean(2, domestic.getCanConnect());
                                domesticStatement.executeUpdate();
                                System.out.println("Domestic product created successfully.");
                            }
                        } else {
                            System.out.println("Unknown product type.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error creating product: " + e.getMessage());
        }
    }


    public void deleteProduct(int productId) {
        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM Product WHERE product_id=?")) {
            preparedStatement.setInt(1, productId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product not found or delete failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }

    public List<Product> getUserProducts(int customer_id) {
        List<Product> product_list = new ArrayList<>();
        try (Connection conn = connect();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT c.product_id AS product_id, c.quantity AS quantity, p.price AS price, p.name AS name FROM Cart c INNER JOIN Product p ON c.product_id = p.product_id WHERE customer_id = %s".formatted(customer_id))) {
            while (resultSet.next()) {
                int product_id = resultSet.getInt("product_id");
                int product_quantity = resultSet.getInt("quantity");
                double product_price = resultSet.getDouble("price");
                String product_name = resultSet.getString("name");
                Product current_product = new Product(product_id, product_name, product_price, product_quantity);
                product_list.add(current_product);
            }
        } catch (SQLException e) {
            System.out.println("Error creating customer: " + e.getMessage());
        }
        return product_list;
    }

    public double getStaffDiscount(int customer_id) {
        try (Connection conn = connect();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT discount FROM Staff WHERE customer_id = " + customer_id)) {

            if (resultSet.next()) {
                return resultSet.getDouble("discount");
            } else {
                return 0;
            }

        } catch (SQLException e) {
            System.out.println("Error getting staff discount: " + e.getMessage());
            return 0;
        }
    }

    public void updateQuantity(int productId, int newQuantity) {
        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement("UPDATE Product SET quantity = quantity - ? WHERE product_id = ?")) {
            preparedStatement.setInt(1, newQuantity);
            preparedStatement.setInt(2, productId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Product quantity updated successfully.");
            } else {
                System.out.println("Product not found or update failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating product quantity: " + e.getMessage());
        }
    }


    public void addToCart(int customer_id, int product_id, int quantity) {
        try (Connection conn = connect()) {
            // Check if the product already exists in the cart
            String isExistsSql = "SELECT quantity FROM Cart WHERE customer_id = ? AND product_id = ?";
            try (PreparedStatement isExistsStatement = conn.prepareStatement(isExistsSql)) {
                isExistsStatement.setInt(1, customer_id);
                isExistsStatement.setInt(2, product_id);
                try (ResultSet resultSet = isExistsStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Product already exists in the cart, update the quantity
                        int existingQuantity = resultSet.getInt("quantity");
                        int newQuantity = existingQuantity + quantity;

                        // Update the quantity in the Cart table
                        String updateSql = "UPDATE Cart SET quantity = ? WHERE customer_id = ? AND product_id = ?";
                        try (PreparedStatement updateStatement = conn.prepareStatement(updateSql)) {
                            updateStatement.setInt(1, newQuantity);
                            updateStatement.setInt(2, customer_id);
                            updateStatement.setInt(3, product_id);
                            updateStatement.executeUpdate();
                            System.out.println("Quantity updated in the cart.");
                        }
                    } else {
                        // Product doesn't exist in the cart, insert a new record
                        String cartSql = "INSERT INTO Cart(customer_id, product_id, quantity) VALUES(?, ?, ?)";
                        try (PreparedStatement cartStatement = conn.prepareStatement(cartSql)) {
                            cartStatement.setInt(1, customer_id);
                            cartStatement.setInt(2, product_id);
                            cartStatement.setInt(3, quantity);
                            cartStatement.executeUpdate();
                            System.out.println("Product added to the cart.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating/inserting into the cart: " + e.getMessage());
        }
    }

    public void removeFromCart(int customer_id, int product_id) {
        try (Connection conn = connect()) {
            // Check if the product exists in the cart
            String isExistsSql = "SELECT quantity FROM Cart WHERE customer_id = ? AND product_id = ?";
            try (PreparedStatement isExistsStatement = conn.prepareStatement(isExistsSql)) {
                isExistsStatement.setInt(1, customer_id);
                isExistsStatement.setInt(2, product_id);
                try (ResultSet resultSet = isExistsStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Product exists in the cart, remove it
                        String removeFromCartSql = "DELETE FROM Cart WHERE customer_id = ? AND product_id = ?";
                        try (PreparedStatement removeFromCartStatement = conn.prepareStatement(removeFromCartSql)) {
                            removeFromCartStatement.setInt(1, customer_id);
                            removeFromCartStatement.setInt(2, product_id);
                            removeFromCartStatement.executeUpdate();
                            System.out.println("Product removed from the cart.");
                        }
                    } else {
                        // Product doesn't exist in the cart
                        System.out.println("Product is not in the cart.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error removing from the cart: " + e.getMessage());
        }
    }

    public boolean isStaff(int customer_id) {
        try (Connection conn = connect();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Staff WHERE customer_id = " + customer_id)) {

            return resultSet.next(); // Вернуть true, если есть совпадение

        } catch (SQLException e) {
            System.out.println("Error checking if customer is staff: " + e.getMessage());
            return false;
        }
    }

    public User login(String username, String password) {
        try (Connection conn = connect()) {
            String loginSql = "SELECT * FROM Customer WHERE username = ? AND password = ?";
            try (PreparedStatement loginStatement = conn.prepareStatement(loginSql)) {
                loginStatement.setString(1, username);
                loginStatement.setString(2, password);

                try (ResultSet resultSet = loginStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int userId = resultSet.getInt("customer_id");
                        String email = resultSet.getString("email");

                        // Проверяем, является ли пользователь сотрудником
                        if (isStaff(userId)) {
                            Staff staff = new Staff(username, password, userId, email, getStaffDiscount(userId));
                            return staff;
                        } else {
                            Customer customer = new Customer(username, password, userId, email);
                            return customer;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
            return null;
        }
        return null;
    }


    public void registerUser(User user) {
        try (Connection conn = connect()) {
            Customer customer = (Customer) user;
            String customerSql = "INSERT INTO Customer(username, password, email) VALUES (?, ?, ?) RETURNING customer_id";
            try (PreparedStatement customerStatement = conn.prepareStatement(customerSql)) {
                customerStatement.setString(1, customer.getUsername());
                customerStatement.setString(2, customer.getPassword());  // Corrected to use getPassword()
                customerStatement.setString(3, customer.getEmail());

                // Execute the statement and get the last inserted customer_id
                try (ResultSet resultSet = customerStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int customerId = resultSet.getInt("customer_id");

                        // If the user is a Staff, insert into the Staff table
                        if (user instanceof Staff) {
                            Staff staff = (Staff) user;
                            String staffSql = "INSERT INTO Staff(customer_id, discount) VALUES (?, ?)";
                            try (PreparedStatement staffStatement = conn.prepareStatement(staffSql)) {
                                staffStatement.setInt(1, customerId);
                                staffStatement.setDouble(2, staff.getDiscount());
                                staffStatement.executeUpdate();
                            }
                        }

                        System.out.println("Customer created successfully.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error creating customer: " + e.getMessage());
        }
    }

    public double calculateTotal(int customer_id) {
        try (Connection conn = connect()) {
            String calculateTotal = "SELECT SUM(c.quantity * p.price) AS total FROM Cart c INNER JOIN Product p ON c.product_id = p.product_id WHERE c.customer_id = ? GROUP BY c.customer_id;";
            try (PreparedStatement calculateTotalStatement = conn.prepareStatement(calculateTotal)) {
                calculateTotalStatement.setInt(1, customer_id);
                try (ResultSet resultSet = calculateTotalStatement.executeQuery()) {
                    if (resultSet.next()) {
                        double totalPrice = resultSet.getDouble("total");
                        return totalPrice;
                    } else {
                        return 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total: " + e.getMessage());
        }
        return -1;
    }
}