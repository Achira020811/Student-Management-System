package Task_03;

import java.util.Scanner;
import java.io.*;

public class Task03 {
    private static final int MAX_STUDENTS = 100;
    private static Student[] students = new Student[MAX_STUDENTS];
    private static int studentCount = 0;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initialize();
        int choice;
        do {
            displayMenu();
            choice = getIntInput("Enter your choice: ");
            processChoice(choice);
        } while (choice != 0);
        scanner.close();
    }

    // Initializes the students array
    private static void initialize() {
        for (int i = 0; i < MAX_STUDENTS; i++) {
            students[i] = null;
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
        System.out.println("8. Manage student modules");
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
            case 8:
                manageStudentModules();
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
                students[studentCount] = new Student(studentId, "");
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
                students[i] = students[i + 1];
            }
            students[studentCount - 1] = null;
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
            System.out.println("Student found: " + students[index]);
        } else {
            System.out.println("Student not found.");
        }
    }

    // Stores student details to a file
    private static void storeStudentDetails() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("students.txt"))) {
            for (int i = 0; i < studentCount; i++) {
                writer.println(students[i].toFileString());
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
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    students[studentCount] = new Student(parts[0], parts[1]);
                    if (parts.length >= 5) {
                        students[studentCount].setModuleMarks(
                                Integer.parseInt(parts[2]),
                                Integer.parseInt(parts[3]),
                                Integer.parseInt(parts[4])
                        );
                    }
                    studentCount++;
                }
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

        Student[] sortedStudents = new Student[studentCount];
        System.arraycopy(students, 0, sortedStudents, 0, studentCount);
        bubbleSort(sortedStudents);

        System.out.println("Sorted list of students:");
        for (int i = 0; i < studentCount; i++) {
            System.out.println((i + 1) + ". " + sortedStudents[i]);
        }
    }

    // Bubble sort algorithm to sort students by name
    private static void bubbleSort(Student[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                int comparison = compareNames(arr[j].getName(), arr[j + 1].getName());
                if (comparison > 0) {
                    Student temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    // Compares two student names
    private static int compareNames(String name1, String name2) {
        int result = String.CASE_INSENSITIVE_ORDER.compare(name1, name2);
        if (result == 0) {
            result = name1.compareTo(name2);
        }
        return result;
    }

    // Manages student modules
    private static void manageStudentModules() {
        System.out.println("\n--- Manage Student Modules ---");
        System.out.println("a. Add student name");
        System.out.println("b. Add module marks");
        System.out.println("c. Generate system summary");
        System.out.println("d. Generate complete report");

        String choice = getStringInput("Enter your choice (a/b/c/d): ");

        switch (choice.toLowerCase()) {
            case "a":
                addStudentName();
                break;
            case "b":
                addModuleMarks();
                break;
            case "c":
                generateSystemSummary();
                break;
            case "d":
                generateCompleteReport();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    // Adds or updates a student's name
    private static void addStudentName() {
        String id = getStringInput("Enter student ID: ");
        int index = findStudentIndex(id);
        if (index == -1) {
            System.out.println("Error: Student not found.");
            return;
        }

        String name = getStringInput("Enter new name for student: ");
        students[index].setName(name);
        System.out.println("Student name updated successfully.");
    }

    // Adds module marks for a student
    private static void addModuleMarks() {
        String id = getStringInput("Enter student ID: ");
        int index = findStudentIndex(id);
        if (index == -1) {
            System.out.println("Error: Student not found.");
            return;
        }

        int mark1 = getIntInput("Enter mark for Module 1: ");
        int mark2 = getIntInput("Enter mark for Module 2: ");
        int mark3 = getIntInput("Enter mark for Module 3: ");

        students[index].setModuleMarks(mark1, mark2, mark3);
        System.out.println("Module marks added successfully.");
    }

    // Generates a summary of the student management system
    private static void generateSystemSummary() {
        int totalStudents = studentCount;
        int[] passCountPerModule = new int[3];

        for (int i = 0; i < studentCount; i++) {
            for (int j = 0; j < 3; j++) {
                if (students[i].getModuleMark(j) > 40) {
                    passCountPerModule[j]++;
                }
            }
        }

        System.out.println("\n--- System Summary ---");
        System.out.println("Total student registrations: " + totalStudents);
        System.out.println("Students who scored more than 40 marks:");
        System.out.println("Module 1: " + passCountPerModule[0]);
        System.out.println("Module 2: " + passCountPerModule[1]);
        System.out.println("Module 3: " + passCountPerModule[2]);
    }

    // Generates a complete report of students
    private static void generateCompleteReport() {
        if (studentCount == 0) {
            System.out.println("No students registered.");
            return;
        }

        Student[] sortedStudents = new Student[studentCount];
        System.arraycopy(students, 0, sortedStudents, 0, studentCount);
        bubbleSortByAverage(sortedStudents);

        System.out.println("\n--- Complete Student Report ---");
        System.out.printf("%-10s %-20s %-10s %-10s %-10s %-10s %-10s %-10s\n",
                "ID", "Name", "Module 1", "Module 2", "Module 3", "Total", "Average", "Grade");
        System.out.println("-------------------------------------------------------------------------------------");

        for (Student s : sortedStudents) {
            int total = s.getModuleMark(0) + s.getModuleMark(1) + s.getModuleMark(2);
            double average = total / 3.0;
            System.out.printf("%-10s %-20s %-10d %-10d %-10d %-10d %-10.2f %-10s\n",
                    s.getId(), s.getName(), s.getModuleMark(0), s.getModuleMark(1), s.getModuleMark(2),
                    total, average, s.getGrade());
        }
    }

    // Bubble sort algorithm to sort students by average marks
    private static void bubbleSortByAverage(Student[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j].getAverage() < arr[j + 1].getAverage()) {
                    Student temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    // Finds the index of a student by ID
    private static int findStudentIndex(String studentId) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId().equals(studentId)) {
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

// Student class with ID, name, and modules
class Student {
    private String id;
    private String name;
    private Module[] modules;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.modules = new Module[3];
        for (int i = 0; i < 3; i++) {
            modules[i] = new Module();
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setModuleMarks(int mark1, int mark2, int mark3) {
        modules[0].setMark(mark1);
        modules[1].setMark(mark2);
        modules[2].setMark(mark3);
    }

    public int getModuleMark(int index) {
        return modules[index].getMark();
    }

    public double getAverage() {
        return (modules[0].getMark() + modules[1].getMark() + modules[2].getMark()) / 3.0;
    }

    public String getGrade() {
        double average = getAverage();
        if (average >= 80) return "Distinction";
        if (average >= 70) return "Merit";
        if (average >= 40) return "Pass";
        return "Fail";
    }

    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Module 1: %d, Module 2: %d, Module 3: %d, Grade: %s",
                id, name, modules[0].getMark(), modules[1].getMark(), modules[2].getMark(), getGrade());
    }

    public String toFileString() {
        return String.format("%s,%s,%d,%d,%d,%s",
                id, name, modules[0].getMark(), modules[1].getMark(), modules[2].getMark(), getGrade());
    }
}

// Module class with a mark
class Module {
    private int mark;

    public Module() {
        this.mark = 0;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        if (mark >= 0 && mark <= 100) {
            this.mark = mark;
        } else {
            System.out.println("Invalid mark. Mark should be between 0 and 100.");
        }
    }
}
