
public class Product {
    private final int productId;
    private final String productName;
    private final double price;
    private int quantity;

    protected App app = new App();

    public Product(int productId, String productName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductId() { return productId; }

    public double getProductPrice() {
        return price;
    }

    public void reduceQuantity(int newQuantity) {
        this.setProductQuantity(this.getProductQuantity() - newQuantity);
        app.updateQuantity(this.getProductId(), newQuantity);
    }

    public void createProduct() {
        app.createProduct(this);
    }

    public int getProductQuantity() {
        return this.quantity;
    }

    public void setProductQuantity(int newValue) {
        this.quantity = newValue;
    }

    public void turnOn() {}
}

class Device extends Product {
    private final double screenSize;
    private final String os;
    public Device (int productId, String productName, double price, int quantity, double screenSize, String os) {
        super(productId, productName, price, quantity);
        this.screenSize = screenSize;
        this.os = os;
    }

    public double getScreenSize(){
        return screenSize;
    }

    public String getOs(){
        return os;
    }

    public void turnOn() {
        System.out.println("Screensaver has appeared");
    }

}

class Domestic extends Product {
    private final boolean canConnectToWiFi;

    public Domestic (int productId, String productName, double price, int quantity, boolean canConnectToWiFi) {
        super(productId, productName, price, quantity);
        this.canConnectToWiFi = canConnectToWiFi;
    }

    public boolean getCanConnect(){
        return canConnectToWiFi;
    }

    public void turnOn() {
        System.out.println("Indicator lit up");
    }
}