public class Main {
    public static void main(String[] args){
        App connection = new App();
        //Customer user = new Customer("VladPerchik", "qwerty1", 1, "a@gmail.com");
        boolean a = connection.login("VladPerchik", "qwerty1");
        System.out.println(a);
    }
}



//import java.util.Scanner;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class Main {
//    public static List<Customer> customers = new ArrayList<>();
//    public static List<Staff> staffs = new ArrayList<>();
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        boolean exit = false;
//
//        Customer customer1 = new Customer("customer4", "1111", "001", "test@mail.ru");
//        Customer customer2 = new Customer("customer2", "2222", "002", "test1@astanait.edu.kz");
//        Customer customer3 = new Customer("customer3", "3333", "003", "test2@gmail.com");
//
////        Staff staff1 = new Staff("staff", "1111", "004", "006", "employer1@gmail.com");
////        Staff staff2 = new Staff("staff", "2222", "005", "007", "employer2@gmail.com");
//
//        Customer curCustomer = null;
//        Staff curStaff = null;
//
//        customers.add(customer1);
//        customers.add(customer2);
//        customers.add(customer3);
//
//       staffs.add(staff1);
//    staffs.add(staff2);
//
////        Phone phone = staff1.createPhone("Iphone X", 599.99, 10, 6.0, "ABC", "X1", "IOS");
////
////        Dryer dryer = staff2.createDryer("Smart Dryer", 299.99, 5, true, "XYZ", 1500.0);
//
//        while (!exit) {
//
//            if(curCustomer != null) {
//                System.out.println("1. Buy product");
//                System.out.println("2. Display cart");
//                System.out.println("3. turn on phone and dryer");
//                System.out.println("4. Exit");
//
//                int val = scanner.nextInt();
//                switch (val) {
//                    case 1:
//                        System.out.println("Enter the quantity: ");
//                        int quantity = scanner.nextInt();
//                        curCustomer.buyProduct(phone, quantity);
//                        System.out.println("You've bought " + quantity +( quantity == 1 ? " phone" : " phones"));
//                        break;
//                    case 2:
//                        curCustomer.displayCart();
//                        break;
//                    case 3:
//                        phone.turnOn();
//                        dryer.turnOn();
//                    case 4:
//                        exit = true;
//                        break;
//                    default:
//                        System.out.println("Invalid choice. Please enter a valid option.");
//                }
//            }
//
//            System.out.println("1. Login User");
//            System.out.println("2. Exit");
//            System.out.print("Enter your choice: ");
//
//            int choice = scanner.nextInt();
//            scanner.nextLine();
//
//            switch (choice) {
//                case 1:
//                    System.out.println("1. Login Customer");
//                    System.out.println("2. Login Staff");
//                    int val = scanner.nextInt();
//                    System.out.println("Enter username: ");
//                    String userName = scanner.next();
//                    System.out.println("Enter password: ");
//                    String passWord = scanner.next();
//
//                    if(val == 1) {
//                        curCustomer = loginCustomer(userName, passWord);
//                    } else  {
//                        curStaff = loginStaff(userName, passWord);
//                    }
//
//                    break;
//                case 2:
//                    exit = true;
//                    break;
//
//                default:
//                    System.out.println("Invalid choice. Please enter a valid option.");
//            }
//        }
//    }
//
//    private static Customer loginCustomer (String username, String password) {
//        for(Customer customer : customers) {
//
//            if (username.equals(customer.getUsername())) {
//                if (password.equals(customer.getPassword())) {
//                    return customer;
//                }
//            }
//        }
//        System.out.println("User not found");
//        return null;
//    }
//    private static Staff loginStaff (String username, String password) {
//        for(Staff staff : staffs) {
//            if (username.equals(staff.getUsername())) {
//                if (password.equals(staff.getPassword())) {
//                    return staff;
//                }
//            }
//        }
//        System.out.println("User not found");
//        return null;
//    }
//
//
//
//
//
//}
