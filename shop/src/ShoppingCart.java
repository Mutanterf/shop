import java.util.ArrayList;
import java.util.List;
class ShoppingCart {
    private App app = new App();
    private int customer_id;
    private List<Product> cart = new ArrayList<>();

    public void ShoppingCart(int customer_id) {
        this.cart = app.getUserProducts(customer_id);
        this.customer_id = customer_id;
    }
    public void calculateTotal() {
//        double total = app.calculateTotal(customer_id);
        if(cart.size() <= 0) {
            System.out.println("Yout cart is empty");
        }
        double total = 0.00;
        for(Product product: cart) {
            total += product.getProductPrice() * product.getProductQuantity();
        }
        System.out.println("Total of your cart: " + total);
    }

    public void addProduct() {

    }

    public void removeProduct() {

    }

    public void displayCart() {
//        List<Product> products = app.getUserProducts(customer_id);
        System.out.println("Shopping Cart:");
        for (Product product : cart) {
            System.out.printf("%s - Price: $%.2f, Quantity: %d%n",
                    product.getProductName(), product.getProductPrice(), product.getProductQuantity());
        }
        System.out.printf("Total: $%.2f%n" + app.calculateTotal(customer_id));
    }
}
