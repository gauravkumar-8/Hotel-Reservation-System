
import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://localhost:3306/learning";
    private static final String username = "root";
    private static final String password = "root";

    public static void main(String args[]) {
        System.out.println("==============================================");
        System.out.println("         *** Welcome to the ***");
        System.out.println("   *** Hotel Management Reservation System ***");
        System.out.println("----------------------------------------------");
        System.out.println(" Simplifying bookings and enhancing guest");
        System.out.println("             experiences!");
        System.out.println("==============================================");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("______HOTEL MANAGEMENT SYSTEM________");
                System.out.println("1.Reserve a room ");
                System.out.println("2.View Reservation ");
                System.out.println("3.Get Room Number ");
                System.out.println("4.Update Reservation ");
                System.out.println("5.Delete Reservation ");
                System.out.println("6.For Erase all Data ");
                System.out.println("0.exit");
                System.out.println("Choose an option..");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> reserveRoom(connection, scanner);
                    case 2 -> viewReservation(connection);
                    case 3 -> getRoomNumber(connection, scanner);
                    case 4 -> updateReservation(connection, scanner);
                    case 5 -> deleteReservation(connection, scanner);
                    case 6 -> eraseTable(connection, scanner);
                    case 0 -> {
                        exit();
                        return;
                    }
                    default -> System.out.println("Invaild choice! Try again");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void reserveRoom(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter guest name:");
            scanner.nextLine();
            String guestName;
            do {
                guestName = scanner.nextLine();
                if (guestName.matches("[a-z A-Z]+")) {
                    break;
                } else
                    System.out.println("Please enter a Valid Name!");
            }
            while (true);
            System.out.println("Enter room number: ");

            int roomNumber = scanner.nextInt();
            System.out.println("Enter contact Number: ");
            String contactNumber;
            do {
                contactNumber = scanner.next();

                if (contactNumber.matches("[0-9]{10}")) {
                    break;
                } else
                    System.out.println("Please enter a Valid contact Number(enter must be 10 digits): ");
            }
            while (true);

            String sql = "Insert into reservations(guest_name,room_number,contact_number) Value('" + guestName + "'," + roomNumber + ",'" + contactNumber + "')";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
/*pstmt.setString(1,guestName);
pstmt.setInt(2,roomNumber);
pstmt.setString(3,contactNumber);*/
                int affectedRow = pstmt.executeUpdate();
                if (affectedRow > 0)
                    System.out.println("Reservation successful!");
                else
                    System.out.println("Reservation failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void viewReservation(Connection connection) throws Exception {
        String sql = "SELECT reservation_id,guest_name,room_number,contact_number," + "reservation_date From reservations";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n____________Current Reservations____________");
            System.out.println("*---------------*--------------*---------------*----------------------*-----------------------*");
            System.out.println("| ReservationID | Guest        | Room Number   |Contact Number        | Time                  |");
            System.out.println("*---------------*--------------*---------------*----------------------*-----------------------*");
            while (rs.next()) {
                int reservationId = rs.getInt("reservation_id");
                String guestName = rs.getString("guest_name");
                int roomNumber = rs.getInt("room_number");
                String contactNumber = rs.getString("contact_number");
                String reservationDate = rs.getTimestamp("reservation_date").toString();
                System.out.printf("|%-14d | %-12s | %-13d | %-20s | %-21s |\n", reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }
            System.out.println("*---------------*--------------*---------------*----------------------*-----------------------*");
        }
    }

    private static void getRoomNumber(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter reservation ID: ");
            int reservationId = scanner.nextInt();
            String sql = "SELECT room_number,guest_name FROM reservations WHERE reservation_id = ? ";

/*scanner.nextLine();
System.out.println("Enter guest name: ");
String guestName=scanner.nextLine();
String sql="SELECT room_number FROM reservations WHERE reservation_id = ? AND guest_name = ?";*/   //this code is used to get the name of the customer and provide his roomNumber...


            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, reservationId);
//pstmt.setString(2,guestName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int roomNumber = rs.getInt("room_number");
                    String guestName = rs.getString("guest_name");
                   System.out.println("Room number for Reservation ID: " + reservationId + " belongs to guest: " + guestName + ", with room number: " + roomNumber);
                } else {
                    System.out.println("Reservation not found!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateReservation(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter reservation ID to update: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine();
            if (!reservationExists(connection, reservationId)) {
                System.out.println(" Reservation not found.");
                return;
            }
            System.out.println("Enter new guest name: ");
            String newGuestName;
            do {
                newGuestName = scanner.nextLine();
                if (newGuestName.matches("[a-z A-Z]+")) {
                    break;
                } else
                    System.out.println("Please enter a Valid Name!");
            }
            while (true);
            System.out.println("Enter new room Number: ");
            int newRoomNumber = scanner.nextInt();
            System.out.println("Enter new contact number: ");
            String newContactNumber;
            do {
                newContactNumber = scanner.next();

                if (newContactNumber.matches("[0-9]{10}")) {
                    break;
                } else
                    System.out.println("Please enter a Valid contact Number(enter must be 10 digits): ");
            }
            while (true);

            String sql = "UPDATE reservations SET guest_name = ?, room_number = ?, contact_number = ? " + " WHERE reservation_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, newGuestName);
                pstmt.setInt(2, newRoomNumber);
                pstmt.setString(3, newContactNumber);
                pstmt.setInt(4, reservationId);

                int affectRows = pstmt.executeUpdate();
                if (affectRows > 0) {
                    System.out.println("Reservation update successfully! ");
                } else {
                    System.out.println("Reservation update failed. ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter reservation ID to delete: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine();
            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found.");
                return;
            }
            String sql = "DELETE FROM reservations WHERE reservation_id=?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, reservationId);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Reservation deleted successfully! ");
                } else {
                    System.out.println("Reservation deletion failed.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private static boolean eraseTable(Connection connection, Scanner scanner) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("TRUNCATE TABLE reservations");
            System.out.println("Data erased successfully");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

    private static boolean reservationExists(Connection connection, int reservationId) {
        String sql = "SELECT reservation_id FROM reservations WHERE reservation_id =?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void exit() throws Exception {
        System.out.println("________Exiting System__________________");
        for (int i = 3; i > 0; i--) {
            System.out.println("Closing in " + i + "......");
            Thread.sleep(500);
        }
        System.out.println("Thank you for using Hotel Reservation System!");
    }
}

