package ch.epfl.rechor.journey;

import ch.epfl.rechor.Preconditions;

import java.util.Objects;

public record Stop(String name, String platformName, double longitude, double latitude) {
    public Stop {
        name = Objects.requireNonNull(name, "Le nom ne doît pas être null !");
        Preconditions.checkArgument((latitude >= -90 && latitude <= 90) && (longitude >= -180 && longitude <= 180));
    }
}
