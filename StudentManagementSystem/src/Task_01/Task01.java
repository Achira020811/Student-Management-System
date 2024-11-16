package Task_01;

import java.util.Scanner;
import java.io.*;

public class Task01 {
    private static final int MAX_STUDENTS = 100;
    private static String[] studentIds = new String[MAX_STUDENTS];
    private static int studentCount = 0;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initialise();
        int choice;
        do {
            displayMenu();
            choice = getIntInput("Enter your choice: ");
            processChoice(choice);
        } while (choice != 0);
        scanner.close();
    }
    // Initializes the students array
    private static void initialise() {
        for (int i = 0; i < MAX_STUDENTS; i++) {
            studentIds[i] = "";
        }
    }
    // Displays the main menu
    private static void displayMenu() {
        System.out.println("\n--- Student Management System ---");
        System.out.println("1. Check available seats");
        System.out.println("2. Register student");
        System.out.println("3. Delete student");
        System.out.println("4. Find student");
        System.out.println("5. Store student details into a file");
        System.out.println("6. Load student details from the file");
        System.out.println("7. View the list of students (sorted)");
        System.out.println("0. Exit");
    }
    // Processes the user's menu choice
    private static void processChoice(int choice) {
        switch (choice) {
            case 1:
                checkAvailableSeats();
                break;
            case 2:
                registerStudent();
                break;
            case 3:
                deleteStudent();
                break;
            case 4:
                findStudent();
                break;
            case 5:
                storeStudentDetails();
                break;
            case 6:
                loadStudentDetails();
                break;
            case 7:
                viewSortedStudentList();
                break;
            case 0:
                System.out.println("Exiting the program. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    // Checks and displays the number of available seats
    private static void checkAvailableSeats() {
        int availableSeats = MAX_STUDENTS - studentCount;
        System.out.println("Available seats: " + availableSeats);
    }
    // Registers a new student
    private static void registerStudent() {
        if (studentCount >= MAX_STUDENTS) {
            System.out.println("Error: Maximum student capacity reached.");
            return;
        }

        String studentId = getStringInput("Enter student ID (e.g., w1234567): ");
        if (isValidStudentId(studentId)) {
            if (findStudentIndex(studentId) == -1) {
                studentIds[studentCount] = studentId;
                studentCount++;
                System.out.println("Student registered successfully.");
            } else {
                System.out.println("Error: Student ID already exists.");
            }
        } else {
            System.out.println("Error: Invalid student ID format.");
        }
    }
    // Deletes a student by ID
    private static void deleteStudent() {
        String studentId = getStringInput("Enter student ID to delete: ");
        int index = findStudentIndex(studentId);
        if (index != -1) {
            for (int i = index; i < studentCount - 1; i++) {
                studentIds[i] = studentIds[i + 1];
            }
            studentIds[studentCount - 1] = "";
            studentCount--;
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Error: Student not found.");
        }
    }
    // Finds and displays a student by ID
    private static void findStudent() {
        String studentId = getStringInput("Enter student ID to find: ");
        int index = findStudentIndex(studentId);
        if (index != -1) {
            System.out.println("Student found at position: " + (index + 1));
        } else {
            System.out.println("Student not found.");
        }
    }
    // Stores student details to a file
    private static void storeStudentDetails() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("students.txt"))) {
            for (int i = 0; i < studentCount; i++) {
                writer.println(studentIds[i]);
            }
            System.out.println("Student details stored successfully.");
        } catch (IOException e) {
            System.out.println("Error storing student details: " + e.getMessage());
        }
    }
    // Loads student details from a file
    private static void loadStudentDetails() {
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            studentCount = 0;
            while ((line = reader.readLine()) != null && studentCount < MAX_STUDENTS) {
                studentIds[studentCount] = line.trim();
                studentCount++;
            }
            System.out.println("Student details loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading student details: " + e.getMessage());
        }
    }
    // Displays the sorted list of students
    private static void viewSortedStudentList() {
        if (studentCount == 0) {
            System.out.println("No students registered.");
            return;
        }

        String[] sortedIds = new String[studentCount];
        System.arraycopy(studentIds, 0, sortedIds, 0, studentCount);
        bubbleSort(sortedIds);

        System.out.println("Sorted list of students:");
        for (int i = 0; i < studentCount; i++) {
            System.out.println((i + 1) + ". " + sortedIds[i]);
        }
    }
    // Bubble sort algorithm to sort students by ID
    private static void bubbleSort(String[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    String temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
    // Finds the index of a student by ID
    private static int findStudentIndex(String studentId) {
        for (int i = 0; i < studentCount; i++) {
            if (studentIds[i].equals(studentId)) {
                return i;
            }
        }
        return -1;
    }
    // Validates the student ID format
    private static boolean isValidStudentId(String studentId) {
        return studentId.matches("w\\d{7}");
    }
    // Gets a string input from the user
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    // Gets an integer input from the user with validation
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}