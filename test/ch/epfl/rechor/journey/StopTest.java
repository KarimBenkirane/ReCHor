package ch.epfl.rechor.journey;

import ch.epfl.rechor.jounrey.Stop;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class StopTest {
    @Test
    void stopConstructorThrowsWhenNameIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new Stop(null, "", 0, 0);
        });
    }

    @Test
    void stopConstructorDoesNotThrowWhenPlatformNameIsNull() {
        new Stop("Arrêt", null, 0, 0);
    }

    @Test
    void stopConstructorWorksWithExtremeLonLat() {
        new Stop("Arrêt", "", -180d, -90d);
        new Stop("Arrêt", "", +180d, +90d);
    }

    @Test
    void stopConstructorThrowsWhenLongitudeIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Stop("Arrêt", "", Math.nextUp(180d), 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Stop("Arrêt", "", Math.nextDown(-180d), 0);
        });
    }

    @Test
    void stopConstructorThrowsWhenLatitudeIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Stop("Arrêt", "", 0, Math.nextUp(90d));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Stop("Arrêt", "", 0, Math.nextDown(-90d));
        });
    }
}