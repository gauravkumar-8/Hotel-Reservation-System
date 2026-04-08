# 🏨 Hotel Reservation System

A **console-based Hotel Reservation System** developed using **Java and MySQL (JDBC)**.  
This project allows users to efficiently manage hotel bookings, including creating, viewing, updating, and deleting reservations.

---

## 📌 Project Overview

The Hotel Reservation System is designed to simplify hotel booking operations and provide a structured way to handle guest reservations.  
It uses **JDBC (Java Database Connectivity)** to interact with a MySQL database and ensures smooth data handling.

---

## ✨ Features

✔️ Reserve a Room  
✔️ View All Reservations  
✔️ Get Room Number by Reservation ID  
✔️ Update Reservation Details  
✔️ Delete Reservation  
✔️ Erase All Data (Admin Feature)  
✔️ Input Validation (Name & Contact Number)  
✔️ Clean Console-Based UI  

---

## 🛠️ Technologies Used

- **Java** (Core Java, OOP Concepts)
- **JDBC** (Database Connectivity)
- **MySQL** (Database Management)
- **SQL** (Queries & Data Handling)

---

## 🗄️ Database Details

**Database Name:** `learning`  
**Table Name:** `reservations`

### 📋 Table Structure

```sql
CREATE TABLE reservations (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
    guest_name VARCHAR(100),
    room_number INT,
    contact_number VARCHAR(15),
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
