package model;

public class Performance {
    private String month;
    private boolean goalAchieved;

    public Performance(String month, boolean goalAchieved) {
        this.month = month;
        this.goalAchieved = goalAchieved;
    }

    public String getMonth() { return month; }
    public boolean isGoalAchieved() { return goalAchieved; }

    @Override
    public String toString() {
        return month + " - " + (goalAchieved ? "Achieved" : "Not Achieved");
    }
}