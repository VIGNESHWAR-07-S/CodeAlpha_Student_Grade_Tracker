import java.io.*;
import java.util.*;

// Room Model
class Room implements Serializable {
    private int roomNumber;
    private String category; // Standard, Deluxe, Suite
    private double price;
    private boolean isBooked;

    public Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.isBooked = false;
    }

    public int getRoomNumber() { return roomNumber; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }

    @Override
    public String toString() {
        return "Room " + roomNumber + " [" + category + "] - Rs. " + price + " | Status: " + (isBooked ? "Booked" : "Available");
    }
}

// Reservation Model
class Reservation implements Serializable {
    private String bookingId;
    private String guestName;
    private Room room;
    private boolean isPaid;

    public Reservation(String bookingId, String guestName, Room room, boolean isPaid) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.room = room;
        this.isPaid = isPaid;
    }

    public String getBookingId() { return bookingId; }
    public Room getRoom() { return room; }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId + " | Guest: " + guestName + " | Room: " + room.getRoomNumber() + " (" + room.getCategory() + ") | Payment: " + (isPaid ? "SUCCESS" : "PENDING");
    }
}

// Main System Class
public class HotelReservationSystem {
    private static final String FILE_NAME = "hotel_data.dat";
    private List<Room> rooms = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public HotelReservationSystem() {
        loadData();
        if (rooms.isEmpty()) {
            initializeDefaultRooms();
        }
    }

    private void initializeDefaultRooms() {
        rooms.add(new Room(101, "Standard", 1500.0));
        rooms.add(new Room(102, "Standard", 1500.0));
        rooms.add(new Room(201, "Deluxe", 3000.0));
        rooms.add(new Room(202, "Deluxe", 3000.0));
        rooms.add(new Room(301, "Suite", 5500.0));
        saveData();
    }

    // 1. Search Available Rooms
    private void searchRooms() {
        System.out.println("\n--- Available Rooms ---");
        boolean found = false;
        for (Room room : rooms) {
            if (!room.isBooked()) {
                System.out.println(room);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No rooms available right now.");
        }
    }

    // 2. Book Room & Payment Simulation
    private void makeReservation() {
        searchRooms();
        System.out.print("\nEnter Room Number to Book: ");
        int roomNum = scanner.nextInt();
        scanner.nextLine(); // clear buffer

        Room selectedRoom = null;
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNum && !r.isBooked()) {
                selectedRoom = r;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Invalid Room Number or Room is already booked!");
            return;
        }

        System.out.print("Enter Guest Name: ");
        String name = scanner.nextLine();

        // Payment Simulation
        System.out.println("\n--- Payment Gateway ---");
        System.out.println("Total Amount to Pay: Rs. " + selectedRoom.getPrice());
        System.out.print("Enter Payment Amount: Rs. ");
        double payment = scanner.nextDouble();

        if (payment >= selectedRoom.getPrice()) {
            selectedRoom.setBooked(true);
            String bookingId = "BK" + (1000 + new Random().nextInt(9000));
            Reservation res = new Reservation(bookingId, name, selectedRoom, true);
            reservations.add(res);
            saveData();

            System.out.println("\n[SUCCESS] Payment Successful! Booking Confirmed.");
            System.out.println(res);
        } else {
            System.out.println("\n[FAILED] Insufficient Amount. Booking Cancelled!");
        }
    }

    // 3. Cancel Reservation
    private void cancelReservation() {
        System.out.print("\nEnter Booking ID to Cancel: ");
        String bId = scanner.next();

        Reservation targetRes = null;
        for (Reservation r : reservations) {
            if (r.getBookingId().equalsIgnoreCase(bId)) {
                targetRes = r;
                break;
            }
        }

        if (targetRes != null) {
            targetRes.getRoom().setBooked(false);
            reservations.remove(targetRes);
            saveData();
            System.out.println("[SUCCESS] Reservation " + bId + " cancelled successfully!");
        } else {
            System.out.println("[ERROR] Booking ID not found.");
        }
    }

    // 4. View All Bookings
    private void viewBookings() {
        System.out.println("\n--- Current Booking Details ---");
        if (reservations.isEmpty()) {
            System.out.println("No active bookings found.");
        } else {
            for (Reservation r : reservations) {
                System.out.println(r);
            }
        }
    }

    // File I/O Persistence
    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(rooms);
            oos.writeObject(reservations);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            rooms = (List<Room>) ois.readObject();
            reservations = (List<Reservation>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Error loading data. Starting fresh.");
        }
    }

    public void start() {
        while (true) {
            System.out.println("\n===== HOTEL RESERVATION SYSTEM =====");
            System.out.println("1. Search Available Rooms");
            System.out.println("2. Make Reservation");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View Booking Details");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> searchRooms();
                case 2 -> makeReservation();
                case 3 -> cancelReservation();
                case 4 -> viewBookings();
                case 5 -> {
                    System.out.println("Thank you for using our system!");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    public static void main(String[] args) {
        new HotelReservationSystem().start();
    }
}