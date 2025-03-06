package ch.epfl.rechor.journey;

import ch.epfl.rechor.Preconditions;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record Journey(List<Leg> legs) {
    public Journey {
        Preconditions.checkArgument(!legs.isEmpty());
        legs = List.copyOf(legs);
        for (int i = 1; i < legs.size() - 1; i++) {
            Leg curLeg = legs.get(i);
            Leg nextLeg = legs.get(i + 1);
            Preconditions.checkArgument((curLeg.arrTime().isBefore(nextLeg.depTime())) || curLeg.arrTime().equals(nextLeg.depTime()));
            Preconditions.checkArgument(curLeg.arrStop().name().equals(nextLeg.depStop().name()));
            Preconditions.checkArgument((curLeg instanceof Leg.Foot && nextLeg instanceof Leg.Transport) || (curLeg instanceof Leg.Transport && nextLeg instanceof Leg.Foot));
        }

    }

    public Stop depStop() {
        return this.legs.getFirst().depStop();
    }

    public Stop arrStop() {
        return this.legs.getLast().arrStop();
    }

    public LocalDateTime depTime() {
        return this.legs.getFirst().depTime();
    }

    public LocalDateTime arrTime() {
        return this.legs.getLast().arrTime();
    }

    public Duration duration() {
        return Duration.between(this.depTime(), this.arrTime());
    }

    public sealed interface Leg {
        Stop depStop();

        LocalDateTime depTime();

        Stop arrStop();

        LocalDateTime arrTime();

        List<IntermediateStop> intermediateStops();

        default Duration duration() {
            return Duration.between(depTime(), arrTime());
        }

        record IntermediateStop(Stop stop, LocalDateTime arrTime, LocalDateTime depTime) {
            public IntermediateStop {
                Objects.requireNonNull(stop, "L'arrêt ne doit pas être null !");
                Preconditions.checkArgument(arrTime.isBefore(depTime) || depTime.equals(arrTime));
            }

        }

        record Transport(Stop depStop, LocalDateTime depTime, Stop arrStop, LocalDateTime arrTime,
                         List<IntermediateStop> intermediateStops, Vehicle vehicle, String route,
                         String destination) implements Leg {
            public Transport {
                Objects.requireNonNull(depStop);
                Objects.requireNonNull(depTime);
                Objects.requireNonNull(arrStop);
                Objects.requireNonNull(arrTime);
                Objects.requireNonNull(vehicle);
                intermediateStops = List.copyOf(intermediateStops);
                Preconditions.checkArgument(depTime.isBefore(arrTime) || depTime.equals(arrTime));
                route = Objects.requireNonNull(route);
                destination = Objects.requireNonNull(destination);
            }
        }

        record Foot(Stop depStop, LocalDateTime depTime, Stop arrStop, LocalDateTime arrTime) implements Leg {
            public Foot {
                Objects.requireNonNull(depStop);
                Objects.requireNonNull(depTime);
                Objects.requireNonNull(arrStop);
                Objects.requireNonNull(arrTime);
                Preconditions.checkArgument(depTime.isBefore(arrTime) || depTime.equals(arrTime));
            }

            public List<IntermediateStop> intermediateStops() {
                return List.of();
            }

            public boolean isTransfer() {
                return depStop.name().equals(arrStop.name());
            }

        }
    }
}
