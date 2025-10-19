package app;

import manager.GymManager;
import model.Member;
import model.RegularMember;
import model.TrainerMember;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final GymManager gm = new GymManager();
    private static String currentFile = null;

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\n(Current file: " + (currentFile == null ? "None" : currentFile) + ")");
            showMenu();
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> loadRecords();
                case "2" -> addMember();
                case "3" -> updateMember();
                case "4" -> deleteMember();
                case "5" -> viewAndQueryMenu();
                case "6" -> {
                    System.out.println("Exiting... Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n===== Member Management System =====");
        System.out.println("1. Load records in a file");
        System.out.println("2. Add new member (auto ID, auto append)");
        System.out.println("3. Update member info (overwrite save)");
        System.out.println("4. Delete member (overwrite save)");
        System.out.println("5. View / Query member details");
        System.out.println("6. Exit");
        System.out.print("Please choose an option: ");
    }

    private static void loadRecords() {
        System.out.print("Enter filename to load (or press Enter for default): ");
        String fn = sc.nextLine().trim();
        if (fn.isEmpty()) fn = "member_data.csv";
        if (!fn.toLowerCase().endsWith(".csv")) fn = fn + ".csv";

        gm.loadFromFile(fn);
        currentFile = fn;
    }

    private static void addMember() {
        addMemberInteractive();

        if (currentFile == null) {
            System.out.print("Enter filename to save (e.g. member_data.csv): ");
            String fn = sc.nextLine().trim();
            if (fn.isEmpty()) fn = "member_data.csv";
            if (!fn.toLowerCase().endsWith(".csv")) fn = fn + ".csv";
            currentFile = fn;
        }

        gm.saveNewMembers(currentFile);
    }

    private static void updateMember() {
        System.out.print("Enter ID of member to update: ");
        String id = sc.nextLine().trim();
        Member m = gm.findById(id);
        if (m == null) {
            System.out.println(" Member not found.");
            return;
        }

        System.out.println("Current: " + m);
        try {
            System.out.print("New name (blank = no change): ");
            String name = sc.nextLine().trim();
            if (!name.isEmpty()) m.setName(name);

            System.out.print("New base fee (blank = no change): ");
            String bf = sc.nextLine().trim();
            if (!bf.isEmpty()) m.setBaseFee(Double.parseDouble(bf));

            System.out.print("New performance rating (blank = no change): ");
            String pr = sc.nextLine().trim();
            if (!pr.isEmpty()) m.setPerformanceRating(Double.parseDouble(pr));

            if (m instanceof TrainerMember tm) {
                System.out.print("New trainer fee (blank = no change): ");
                String tf = sc.nextLine().trim();
                if (!tf.isEmpty()) tm.setTrainerFee(Double.parseDouble(tf));
            }

            System.out.println(" Member updated.");
            if (currentFile != null) gm.saveAll(currentFile);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void deleteMember() {
        System.out.print("Enter ID of member to delete: ");
        String id = sc.nextLine().trim();
        boolean deleted = gm.deleteById(id);
        System.out.println(deleted ? " Member deleted." : " Member not found.");

        if (currentFile != null) gm.saveAll(currentFile);
    }

    private static void viewAndQueryMenu() {
        System.out.println("\n1. View all members");
        System.out.println("2. Query by ID");
        System.out.println("3. Query by name");
        System.out.println("4. Query by minimum performance rating");
        System.out.print("Choose: ");
        String c = sc.nextLine().trim();

        switch (c) {
            case "1" -> gm.getAll().forEach(System.out::println);
            case "2" -> {
                System.out.print("Enter ID: ");
                Member m = gm.findById(sc.nextLine().trim());
                System.out.println(m == null ? "Not found." : m);
            }
            case "3" -> {
                System.out.print("Enter name (or part): ");
                gm.queryByName(sc.nextLine().trim()).forEach(System.out::println);
            }
            case "4" -> {
                try {
                    System.out.print("Enter minimum rating: ");
                    double r = Double.parseDouble(sc.nextLine().trim());
                    gm.queryByPerformance(r).forEach(System.out::println);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid rating input.");
                }
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void addMemberInteractive() {
        try {
            //  auto-generate ID
            String id = gm.generateUniqueId();
            System.out.println("Generated Member ID: " + id);

            System.out.print("Enter full name: ");
            String name = sc.nextLine().trim();
            System.out.print("Enter base fee: ");
            double baseFee = Double.parseDouble(sc.nextLine().trim());
            System.out.print("Enter performance rating (0–10): ");
            double perf = Double.parseDouble(sc.nextLine().trim());
            System.out.print("Type (1) Regular (2) Trainer: ");
            String type = sc.nextLine().trim();

            if ("2".equals(type)) {
                System.out.print("Enter trainer fee: ");
                double trainerFee = Double.parseDouble(sc.nextLine().trim());
                gm.addMember(new TrainerMember(id, name, baseFee, perf, trainerFee));
            } else {
                gm.addMember(new RegularMember(id, name, baseFee, perf));
            }

            System.out.println("Member added successfully with ID: " + id);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number input.");
        }
    }
}
