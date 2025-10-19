package model;

public class RegularMember extends Member {
    public RegularMember(String id, String name, double baseFee, double performanceRating) {
        super(id, name, baseFee, performanceRating);
    }

    @Override
    public double calculateFee() {
        return baseFee;
    }

    @Override
    public String getType() {
        return "REGULAR";
    }
}
