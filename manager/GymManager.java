package manager;

import model.Member;
import util.FileHandler;
import java.util.*;

public class GymManager {
    private List<Member> members = new ArrayList<>();
    private List<Member> newMembers = new ArrayList<>();
    private int nextId = 1; // for generating unique IDs

    public void loadFromFile(String filename) {
        members = FileHandler.readMembers(filename);
        System.out.println("Loaded " + members.size() + " members from " + filename);

        // find max numeric ID from existing members
        int maxId = members.stream()
                .map(Member::getId)
                .filter(Objects::nonNull)
                .filter(id -> id.matches("M\\d+"))
                .mapToInt(id -> Integer.parseInt(id.substring(1)))
                .max()
                .orElse(0);
        nextId = maxId + 1;
    }

    // Generating auto unique id
    public String generateUniqueId() {
        return String.format("G%03d", nextId++);
    }

    public void addMember(Member m) {
        members.add(m);
        newMembers.add(m);
    }

    public void saveNewMembers(String filename) {
        if (newMembers.isEmpty()) {
            System.out.println("No new members to save.");
            return;
        }
        FileHandler.appendMembers(filename, newMembers);
        newMembers.clear();
    }

    public void saveAll(String filename) {
        FileHandler.overwriteMembers(filename, members);
    }

    public Member findById(String id) {
        for (Member m : members) {
            if (m.getId().equalsIgnoreCase(id)) return m;
        }
        return null;
    }

    public boolean deleteById(String id) {
        return members.removeIf(m -> m.getId().equalsIgnoreCase(id));
    }

    public List<Member> queryByName(String namePart) {
        List<Member> result = new ArrayList<>();
        for (Member m : members) {
            if (m.getName().toLowerCase().contains(namePart.toLowerCase())) result.add(m);
        }
        return result;
    }

    public List<Member> queryByPerformance(double minRating) {
        List<Member> result = new ArrayList<>();
        for (Member m : members) {
            if (m.getPerformanceRating() >= minRating) result.add(m);
        }
        return result;
    }

    public List<Member> getAll() {
        return new ArrayList<>(members);
    }
}
