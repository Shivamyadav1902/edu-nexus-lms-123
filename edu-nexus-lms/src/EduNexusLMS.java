import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * EduNexusLMS - Command Line Interface Application
 *
 * This program simulates the core features of the EduNexus Learning Management System
 * using a simple text-based interface in Java.
 *
 * Features include:
 * - Role-based Login (Student, Teacher, Admin)
 * - Dashboard with stats
 * - AI Tutor (simulated)
 * - Lesson Architect (simulated)
 */

// --- Data Models ---

class User {
    private final int id;
    private final String name;
    private final String role; // "student", "teacher", "admin"

    public User(int id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public String getName() { return name; }
    public String getRole() { return role; }

    @Override
    public String toString() {
        return name + " (" + role.toUpperCase() + ")";
    }
}

class Course {
    private final int id;
    private final String title;
    private final String code;
    private final String instructor;
    private final int progress;

    public Course(int id, String title, String code, String instructor, int progress) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.instructor = instructor;
        this.progress = progress;
    }

    public String getTitle() { return title; }
    public String getCode() { return code; }
    public String getInstructor() { return instructor; }
    public int getProgress() { return progress; }
}

class Assignment {
    private final int id;
    private final String title;
    private final String dueDate;
    private final String status;

    public Assignment(int id, String title, String dueDate, String status) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getTitle() { return title; }
    public String getDueDate() { return dueDate; }
    public String getStatus() { return status; }
}

public class EduNexusLMS {

    // --- Static Data (Mocking the database) ---
    private static final Map<String, User> MOCK_USERS = new HashMap<>();
    private static final List<Course> MOCK_COURSES = new ArrayList<>();
    private static final List<Assignment> MOCK_ASSIGNMENTS = new ArrayList<>();

    static {
        // Initialize Mock Users
        // These are the login credentials for testing
        MOCK_USERS.put("admin@edu.com", new User(1, "Admin User", "admin"));
        MOCK_USERS.put("sarah@edu.com", new User(2, "Sarah Teacher", "teacher"));
        MOCK_USERS.put("john@edu.com", new User(3, "John Student", "student"));

        // Initialize Mock Courses
        MOCK_COURSES.add(new Course(1, "Advanced Mathematics", "MATH301", "Sarah Teacher", 75));
        MOCK_COURSES.add(new Course(2, "World History", "HIST202", "Mr. Anderson", 40));
        MOCK_COURSES.add(new Course(3, "Physics I", "PHYS101", "Dr. Cooper", 90));

        // Initialize Mock Assignments
        MOCK_ASSIGNMENTS.add(new Assignment(1, "Calculus Quiz", "2023-11-25", "pending"));
        MOCK_ASSIGNMENTS.add(new Assignment(2, "Linear Algebra Paper", "2023-11-28", "submitted"));
        MOCK_ASSIGNMENTS.add(new Assignment(3, "WWII Essay", "2023-12-01", "pending"));
    }

    // --- Application State ---
    private User currentUser = null;
    private String currentView = "dashboard";
    // The Scanner instance for handling user input
    private final Scanner scanner = new Scanner(System.in); 

    public static void main(String[] args) {
        EduNexusLMS app = new EduNexusLMS();
        app.start();
    }

    public void start() {
        System.out.println("=========================================");
        System.out.println("  Welcome to EduNexus LMS (CLI Version)");
        System.out.println("=========================================");
        login();
    }

    private void login() {
        while (currentUser == null) {
            System.out.print("\nEnter Email (e.g., john@edu.com, sarah@edu.com): ");
            String email = scanner.nextLine().trim().toLowerCase();

            if (MOCK_USERS.containsKey(email)) {
                currentUser = MOCK_USERS.get(email);
                System.out.println("\n[SUCCESS] Login successful! Welcome, " + currentUser.getName() + " (" + currentUser.getRole().toUpperCase() + ")");
            } else {
                System.out.println("[ERROR] Invalid email. Please try again.");
            }
        }
        showMenu();
    }

    private void showMenu() {
        // Loop continuously until the user logs out
        while (true) {
            System.out.println("\n\n=========================================");
            System.out.println(" Current User: " + currentUser);
            System.out.println(" Current View: " + currentView.toUpperCase().replace("-", " "));
            System.out.println("=========================================");

            // Render the current view content
            switch (currentView) {
                case "dashboard":
                    showDashboard();
                    break;
                case "courses":
                    showCourses();
                    break;
                case "assignments":
                    showAssignments();
                    break;
                case "ai-tutor":
                    showAITutor();
                    break;
                case "lesson-architect":
                    showLessonArchitect();
                    break;
                case "logout":
                    handleLogout();
                    return; // Exit the menu loop after logout
                default:
                    System.out.println("[INFO] Unknown view. Returning to Dashboard.");
                    currentView = "dashboard";
            }

            // Show navigation options
            System.out.println("\n--- NAVIGATION ---");
            System.out.println("1: Dashboard | 2: Courses | 3: Assignments");
            if (currentUser.getRole().equals("student")) {
                System.out.println("4: AI Tutor");
            } else if (currentUser.getRole().equals("teacher")) {
                System.out.println("4: Lesson Architect");
            }
            System.out.println("0: Logout");
            System.out.print("Enter option number or view name (e.g., 4 or 'assignments'): ");

            // Read user input
            String input = scanner.nextLine().trim();
            handleInput(input);
        }
    }

    private void handleInput(String input) {
        switch (input.toLowerCase()) {
            case "1":
            case "dashboard":
                currentView = "dashboard";
                break;
            case "2":
            case "courses":
                currentView = "courses";
                break;
            case "3":
            case "assignments":
                currentView = "assignments";
                break;
            case "4":
                if (currentUser.getRole().equals("student")) {
                    currentView = "ai-tutor";
                } else if (currentUser.getRole().equals("teacher")) {
                    currentView = "lesson-architect";
                } else {
                    System.out.println("[ERROR] Option 4 is not available for your role.");
                }
                break;
            case "0":
            case "logout":
                currentView = "logout";
                break;
            default:
                System.out.println("[ERROR] Invalid option. Please try again.");
        }
    }

    private void showDashboard() {
        System.out.println("\n--- DASHBOARD SUMMARY ---");
        String role = currentUser.getRole();

        if (role.equals("student")) {
            System.out.println("  Hello, " + currentUser.getName().split(" ")[0] + "! You have 2 assignments due soon.");
            long completed = MOCK_COURSES.stream().filter(c -> c.getProgress() == 100).count();
            System.out.println("  - Courses Enrolled: " + MOCK_COURSES.size());
            System.out.println("  - Courses Completed: " + completed);
            System.out.println("  - Pending Assignments: " + MOCK_ASSIGNMENTS.stream().filter(a -> a.getStatus().equals("pending")).count());
        } else if (role.equals("teacher")) {
            System.out.println("  Teacher Dashboard: Focus on grading and planning.");
            System.out.println("  - Total Courses Taught: " + MOCK_COURSES.stream().filter(c -> c.getInstructor().equals(currentUser.getName())).count());
            System.out.println("  - Pending Grading: 14 submissions");
        } else if (role.equals("admin")) {
            System.out.println("  Admin Dashboard: System Health Overview.");
            System.out.println("  - Active Courses: " + MOCK_COURSES.size());
            System.out.println("  - Total Users: " + MOCK_USERS.size());
            System.out.println("  - System Status: Operational (99% Uptime)");
        }
    }

    private void showCourses() {
        System.out.println("\n--- COURSES LIST ---");
        for (Course course : MOCK_COURSES) {
            System.out.printf("  [%s] %s (Instructor: %s)\n",
                course.getCode(), course.getTitle(), course.getInstructor());
            if (currentUser.getRole().equals("student")) {
                System.out.printf("     - Progress: %d%%\n", course.getProgress());
            }
        }
    }

    private void showAssignments() {
        System.out.println("\n--- ASSIGNMENTS ---");
        System.out.printf("%-25s %-15s %-10s\n", "TITLE", "DUE DATE", "STATUS");
        System.out.println("--------------------------------------------------");
        for (Assignment a : MOCK_ASSIGNMENTS) {
            System.out.printf("%-25s %-15s %-10s\n", a.getTitle(), a.getDueDate(), a.getStatus().toUpperCase());
        }
    }

    private void showAITutor() {
        if (!currentUser.getRole().equals("student")) {
            System.out.println("[ACCESS DENIED] AI Tutor is only for students.");
            currentView = "dashboard";
            return;
        }

        System.out.println("\n--- NEXUS AI TUTOR ---");
        System.out.println("Available Modes: [1] Explain, [2] Quiz, [3] Study Plan");
        System.out.print("Select mode (1/2/3): ");
        String mode = scanner.nextLine().trim();

        System.out.print("Enter the topic: ");
        String topic = scanner.nextLine().trim();

        if (topic.isEmpty()) {
            System.out.println("[INFO] Topic cannot be empty.");
            return;
        }

        System.out.println("\n[AI] Thinking...");
        try {
            // Simulate API latency
            TimeUnit.SECONDS.sleep(2); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n--- AI RESPONSE ---");
        switch (mode) {
            case "1": // Explain
                System.out.println("Concept: " + topic);
                System.out.println("The AI provides a simple, clear explanation of **" + topic + "**.");
                System.out.println("* Key Takeaway 1: Important Fact.");
                System.out.println("* Key Takeaway 2: Simple Analogy.");
                break;
            case "2": // Quiz
                System.out.println("Quiz on: " + topic);
                System.out.println("1. What is the main characteristic of " + topic + "? (A/B/C/D)");
                System.out.println("2. Who discovered the core concept? (A/B/C/D)");
                System.out.println("[Answer Key] 1=B, 2=C.");
                break;
            case "3": // Study Plan
                System.out.println("3-Day Study Plan for: " + topic);
                System.out.println("Day 1: 1hr - Read Introduction; 30min - Define Key Terms.");
                System.out.println("Day 2: 1hr - Practice Problems/Review Examples; 30min - Take Notes.");
                System.out.println("Day 3: 1hr - Review all material; 30min - Self-Quiz.");
                break;
            default:
                System.out.println("[ERROR] Invalid mode selected.");
        }
        System.out.println("-------------------\n");
    }

    private void showLessonArchitect() {
        if (!currentUser.getRole().equals("teacher")) {
            System.out.println("[ACCESS DENIED] Lesson Architect is only for teachers.");
            currentView = "dashboard";
            return;
        }

        System.out.println("\n--- LESSON ARCHITECT (TEACHER TOOL) ---");
        System.out.print("Enter Lesson Topic: ");
        String topic = scanner.nextLine().trim();
        System.out.print("Enter Grade Level (e.g., 10th Grade): ");
        String grade = scanner.nextLine().trim();

        if (topic.isEmpty()) {
            System.out.println("[INFO] Topic cannot be empty.");
            return;
        }

        System.out.println("\n[AI] Drafting comprehensive lesson plan...");
        try {
            // Simulate API latency
            TimeUnit.SECONDS.sleep(3); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n--- GENERATED LESSON PLAN (" + grade + " on " + topic + ") ---");
        System.out.println("1. LEARNING OBJECTIVES:");
        System.out.println("   - Students will be able to define the core concepts of " + topic + ".");
        System.out.println("2. CREATIVE HOOK:");
        System.out.println("   - Start with a 5-minute video clip or a 'Two Truths and a Lie' game related to the topic.");
        System.out.println("3. MAIN BREAKDOWN (60 Minutes):");
        System.out.println("   - 10 min: Hook & Objective setting.");
        System.out.println("   - 30 min: Lecture & Guided Practice.");
        System.out.println("   - 20 min: Collaborative small group activity.");
        System.out.println("4. HOMEWORK ASSIGNMENT:");
        System.out.println("   - Write a 250-word journal entry applying the concept to a real-world scenario.");
        System.out.println("---------------------------------------------------\n");
    }

    private void handleLogout() {
        System.out.println("\n[LOGOUT] Thank you, " + currentUser.getName() + ". Logging out.");
        currentUser = null;
        // IMPORTANT: Close the Scanner to release resources and avoid warnings.
        scanner.close(); 
        System.out.println("=========================================");
        System.out.println("         Application Exited              ");
        System.out.println("=========================================");
    }
}

