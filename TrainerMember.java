package model;

public class TrainerMember extends Member {
    private double trainerFee;

    public TrainerMember(String id, String name, double baseFee, double performanceRating, double trainerFee) {
        super(id, name, baseFee, performanceRating);
        this.trainerFee = trainerFee;
    }

    @Override
    public double calculateFee() {
        double total = baseFee + trainerFee;
        if (performanceRating >= 8.0) total *= 0.9; // 10% discount
        return total;
    }

    @Override
    public String getType() {
        return "TRAINER";
    }

    @Override
    public double getTrainerFee() {
        return trainerFee;
    }

    public void setTrainerFee(double trainerFee) {
        this.trainerFee = trainerFee;
    }
}
