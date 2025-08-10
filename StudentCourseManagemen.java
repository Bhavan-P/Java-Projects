import java.io.*;
import java.util.*;

class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    int id;
    String name;
    List<String> courses;

    Student(int id, String name) {
        this.id = id;
        this.name = name;
        this.courses = new ArrayList<>();
    }

    void enrollCourse(String courseName) {
        if (!courses.contains(courseName)) {
            courses.add(courseName);
            System.out.println(name + " enrolled in " + courseName);
        } else {
            System.out.println("Already enrolled in this course.");
        }
    }

    void displayCourses() {
        System.out.println("Courses for " + name + ": " + courses);
    }
}

public class StudentCourseManagement {
    static final String FILE_NAME = "students.dat";
    static Map<Integer, Student> students = new HashMap<>();

    public static void main(String[] args) {
        loadData();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Student Course Management System ===");
            System.out.println("1. Add Student");
            System.out.println("2. Enroll in Course");
            System.out.println("3. View Student Courses");
            System.out.println("4. View All Students");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Student ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Student Name: ");
                    String name = sc.nextLine();
                    students.put(id, new Student(id, name));
                    saveData();
                    System.out.println("Student added successfully.");
                    break;

                case 2:
                    System.out.print("Enter Student ID: ");
                    id = sc.nextInt();
                    sc.nextLine();
                    Student student = students.get(id);
                    if (student != null) {
                        System.out.print("Enter Course Name: ");
                        String course = sc.nextLine();
                        student.enrollCourse(course);
                        saveData();
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter Student ID: ");
                    id = sc.nextInt();
                    sc.nextLine();
                    student = students.get(id);
                    if (student != null) {
                        student.displayCourses();
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 4:
                    if (students.isEmpty()) {
                        System.out.println("No students found.");
                    } else {
                        for (Student s : students.values()) {
                            System.out.println("ID: " + s.id + ", Name: " + s.name + ", Courses: " + s.courses);
                        }
                    }
                    break;

                case 5:
                    System.out.println("Exiting... Data saved.");
                    saveData();
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 5);

        sc.close();
    }

    // Save student data to file
    static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load student data from file
    @SuppressWarnings("unchecked")
    static void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            students = (Map<Integer, Student>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File not found means first run — no students yet
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
