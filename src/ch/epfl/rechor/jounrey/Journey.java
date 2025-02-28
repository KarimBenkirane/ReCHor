package ch.epfl.rechor.jounrey;

import ch.epfl.rechor.Preconditions;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record Journey(List<Leg> legs) {
    public Journey {
        Preconditions.checkArgument(!legs.isEmpty());
        legs = List.copyOf(legs);
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

        public record IntermediateStop(Stop stop, LocalDateTime arrTime, LocalDateTime depTime) {
            public IntermediateStop {
                stop = Objects.requireNonNull(stop, "L'arrêt ne doit pas être null !");
                Preconditions.checkArgument(depTime.isBefore(arrTime));
            }

        }

        public record Transport(Stop depStop, LocalDateTime depTime, Stop arrStop, LocalDateTime arrTime,
                                List<IntermediateStop> intermediateStops, Vehicle vehicle, String route,
                                String destination) implements Leg {
            public Transport {
                depStop = Objects.requireNonNull(depStop);
                depTime = Objects.requireNonNull(depTime);
                arrStop = Objects.requireNonNull(arrStop);
                arrTime = Objects.requireNonNull(arrTime);
                vehicle = Objects.requireNonNull(vehicle);
                Preconditions.checkArgument(depTime.isBefore(arrTime));
                intermediateStops = List.copyOf(intermediateStops);
                route = Objects.requireNonNull(route);
                destination = Objects.requireNonNull(destination);
            }
        }

        public record Foot(Stop depStop, LocalDateTime depTime, Stop arrStop, LocalDateTime arrTime) implements Leg {
            public Foot {
                depStop = Objects.requireNonNull(depStop);
                depTime = Objects.requireNonNull(depTime);
                arrStop = Objects.requireNonNull(arrStop);
                arrTime = Objects.requireNonNull(arrTime);
                Preconditions.checkArgument(depTime.isBefore(arrTime));
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
