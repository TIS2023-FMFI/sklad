package app;

import Entity.Position;

public class RelocateProduct {
    Position relocateFrom;
    Position relocateTo;

    /** skontroluje, ci na danu poziciu sa moze preskladnit tovar**/
    public boolean checkPositonIsEmpty(Position p){return false;};
    public boolean changePosition(Position relocateFrom, Position relocateTo){return false;}

}
