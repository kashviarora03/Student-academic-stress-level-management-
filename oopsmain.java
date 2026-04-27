import java.util.*;
import java.io.*;

// Base Class
class Person {
    private String name;
    private int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }

    public String getRole() {
        return "Person";
    }
}

// Student Class
class Student extends Person {
    private int studyHours;
    private int sleepHours;
    private int assignments;
    private String date;

    Student(String name, int age, int studyHours, int sleepHours, int assignments, String date) {
        super(name, age);
        this.studyHours = studyHours;
        this.sleepHours = sleepHours;
        this.assignments = assignments;
        this.date = date;
    }

    public int getStudyHours() { return studyHours; }
    public int getSleepHours() { return sleepHours; }
    public int getAssignments() { return assignments; }
    public String getDate() { return date; }

    @Override
    public String getRole() {
        return "Student";
    }
}

// Stress Calculator
class StressCalculator {

    public int calculateScore(Student s) {
        return (s.getAssignments() * 10) +
               (10 - s.getSleepHours()) * 5 +
               (s.getStudyHours() * 2);
    }

    public int calculateScore(Student s, int extraStress) {
        return calculateScore(s) + extraStress;
    }

    public String getStressLevel(int score) {
        if (score > 60) return "High";
        else if (score > 30) return "Medium";
        else return "Low";
    }
}

// Suggestion System
class SuggestionSystem {

    public String giveSuggestion(String level) {
        if (level.equals("High"))
            return "You are at risk of burnout. Take rest and reduce workload.";
        else if (level.equals("Medium"))
            return "Maintain better balance between study and rest.";
        else
            return "You are managing stress well. Keep it up!";
    }
}

// Database
class StudentDatabase {
    private ArrayList<Student> data;
    private final String FILE_NAME = "student_data.txt";

    StudentDatabase() {
        data = new ArrayList<>();
        loadFromFile();
    }

    public void addStudent(Student s) {
        data.add(s);
        saveToFile(s);
    }

    public ArrayList<Student> getAllStudents() {
        return data;
    }

    // SAVE
    private void saveToFile(Student s) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {

            bw.write(s.getName() + "," +
                     s.getAge() + "," +
                     s.getStudyHours() + "," +
                     s.getSleepHours() + "," +
                     s.getAssignments() + "," +
                     s.getDate());

            bw.newLine();

        } catch (IOException e) {
            System.out.println("Error saving data!");
        }
    }

    // LOAD
    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 6) {
                    String name = parts[0];
                    int age = Integer.parseInt(parts[1]);
                    int study = Integer.parseInt(parts[2]);
                    int sleep = Integer.parseInt(parts[3]);
                    int assign = Integer.parseInt(parts[4]);
                    String date = parts[5];

                    data.add(new Student(name, age, study, sleep, assign, date));
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading data!");
        }
    }

    public void displayAll(StressCalculator sc) {
        System.out.println("\n--- All Students ---");
        for (Student s : data) {
            int score = sc.calculateScore(s);
            String level = sc.getStressLevel(score);

            System.out.println(s.getName() + " (" + s.getDate() + ") → Score: "
                    + score + " (" + level + ")");
        }
    }
}

// Statistics
class Statistics {

    public void showStats(ArrayList<Student> data, StressCalculator sc) {

        if (data.size() == 0) {
            System.out.println("No data available.");
            return;
        }

        int total = 0, high = 0, medium = 0, low = 0;

        Student maxStudent = data.get(0);
        int maxScore = sc.calculateScore(maxStudent);

        for (Student s : data) {
            int score = sc.calculateScore(s);
            total += score;

            if (score > 60) high++;
            else if (score > 30) medium++;
            else low++;

            if (score > maxScore) {
                maxScore = score;
                maxStudent = s;
            }
        }

        double avg = (double) total / data.size();

        System.out.println("\n--- Statistics ---");
        System.out.println("Average Stress Score: " + avg);
        System.out.println("High: " + high + " | Medium: " + medium + " | Low: " + low);
        System.out.println("Most Stressed Student: " + maxStudent.getName());
    }
}

// MAIN
public class oopsmain {

    static Student takeStudentInput(Scanner input) {

        System.out.print("Enter Name: ");
        String name = input.nextLine();

        System.out.print("Enter Age: ");
        int age = input.nextInt();

        System.out.print("Study Hours: ");
        int study = input.nextInt();

        System.out.print("Sleep Hours: ");
        int sleep = input.nextInt();

        System.out.print("Assignments Pending: ");
        int assign = input.nextInt();

        input.nextLine();

        System.out.print("Enter Date (DD-MM-YYYY): ");
        String date = input.nextLine();

        return new Student(name, age, study, sleep, assign, date);
    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        StressCalculator sc = new StressCalculator();
        SuggestionSystem ss = new SuggestionSystem();
        StudentDatabase db = new StudentDatabase();
        Statistics stats = new Statistics();

        int choice;

        do {
            System.out.println("\n===== Student Stress Management System =====");
            System.out.println("1. View All Students");
            System.out.println("2. Add & Check Stress");
            System.out.println("3. View Statistics");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {

                case 1:
                    db.displayAll(sc);
                    break;

                case 2:
                    Student user = takeStudentInput(input);

                    int score = sc.calculateScore(user, 5);
                    String level = sc.getStressLevel(score);
                    String suggestion = ss.giveSuggestion(level);

                    db.addStudent(user);

                    System.out.println("\nStress Score: " + score);
                    System.out.println("Stress Level: " + level);
                    System.out.println("Suggestion: " + suggestion);
                    break;

                case 3:
                    stats.showStats(db.getAllStudents(), sc);
                    break;

                case 4:
                    System.out.println("Exiting program...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 4);

        input.close();
    }
}