package util;

import model.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileHandler {

    //  Read members from CSV (skipping header)
    public static List<Member> readMembers(String filename) {
        List<Member> members = new ArrayList<>();
        Path path = Paths.get(filename);
        if (!Files.exists(path)) {
            System.out.println("File not found: " + filename);
            return members;
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Skip header
                if (firstLine && line.toLowerCase().startsWith("id,")) {
                    firstLine = false;
                    continue;
                }
                firstLine = false;

                String[] p = line.split(",");
                if (p.length < 5) continue;

                String id = p[0].trim();
                String name = p[1].trim();
                String type = p[2].trim();
                double baseFee = Double.parseDouble(p[3].trim());
                double perf = Double.parseDouble(p[4].trim());
                double trainerFee = (p.length > 5) ? Double.parseDouble(p[5].trim()) : 0.0;

                if ("TRAINER".equalsIgnoreCase(type))
                    members.add(new TrainerMember(id, name, baseFee, perf, trainerFee));
                else
                    members.add(new RegularMember(id, name, baseFee, perf));
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return members;
    }

    // Append new members to existing CSV (keeps old ones)
    public static void appendMembers(String filename, List<Member> members) {
        if (!filename.toLowerCase().endsWith(".csv")) {
            filename = filename + ".csv";
        }

        Path path = Paths.get(filename);
        boolean fileExists = Files.exists(path);
        boolean writeHeader = !fileExists || (fileExists && isFileEmpty(path));

        try (BufferedWriter bw = Files.newBufferedWriter(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            if (writeHeader) {
                bw.write("ID,Name,Type,BaseFee,PerformanceRating,TrainerFee");
                bw.newLine();
            }

            for (Member m : members) {
                bw.write(m.toCSV());
                bw.newLine();
            }

            System.out.println(" Appended " + members.size() + " record(s) to " + filename);
        } catch (IOException e) {
            System.err.println(" Error writing file: " + e.getMessage());
        }
    }

    //  Overwrite entire file (used for updates/deletes)
    public static void overwriteMembers(String filename, List<Member> members) {
        if (!filename.toLowerCase().endsWith(".csv")) {
            filename = filename + ".csv";
        }

        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(filename))) {
            bw.write("ID,Name,Type,BaseFee,PerformanceRating,TrainerFee");
            bw.newLine();
            for (Member m : members) {
                bw.write(m.toCSV());
                bw.newLine();
            }
            System.out.println(" Saved full list of " + members.size() + " records to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

    private static boolean isFileEmpty(Path path) {
        try {
            return Files.size(path) == 0;
        } catch (IOException e) {
            return true;
        }
    }
}
