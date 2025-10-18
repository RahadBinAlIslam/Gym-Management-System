package model;

public abstract class Member {
    protected String id;
    protected String name;
    protected double baseFee;
    protected double performanceRating;

    public Member(String id, String name, double baseFee, double performanceRating) {
        this.id = id;
        this.name = name;
        this.baseFee = baseFee;
        this.performanceRating = performanceRating;
    }

    public abstract double calculateFee();
    public abstract String getType();
    public double getTrainerFee() { return 0.0; }

    public String toCSV() {
        return String.format("%s,%s,%s,%.2f,%.2f,%.2f",
                id, name, getType(), baseFee, performanceRating, getTrainerFee());
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getBaseFee() { return baseFee; }
    public double getPerformanceRating() { return performanceRating; }

    public void setName(String name) { this.name = name; }
    public void setBaseFee(double baseFee) { this.baseFee = baseFee; }
    public void setPerformanceRating(double performanceRating) { this.performanceRating = performanceRating; }

    @Override
    public String toString() {
        return String.format("%s | %s | Type: %s | Perf: %.1f | Fee: $%.2f",
                id, name, getType(), performanceRating, calculateFee());
    }
}
