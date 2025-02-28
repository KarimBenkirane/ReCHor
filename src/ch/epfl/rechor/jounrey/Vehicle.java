package ch.epfl.rechor.jounrey;

import java.util.List;

public enum Vehicle {
    TRAM,
    METRO,
    TRAIN,
    BUS,
    FERRY,
    AERIAL_LIFT,
    FUNICULAR;

    public static final List<Vehicle> ALL = List.of(Vehicle.values());
}
