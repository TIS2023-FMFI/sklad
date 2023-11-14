package Strecno;

public class Customer {
    private String name;
    private Position[] reservedPositions;

    public Customer(String name, Position[] reservedPositions) {
        this.name = name;
        this.reservedPositions = reservedPositions;
    }

    public String getName() {
        return name;
    }

    public boolean hasReserved(Position position){
        return true;
    }
}
