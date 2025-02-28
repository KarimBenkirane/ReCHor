package ch.epfl.rechor;

import ch.epfl.rechor.jounrey.Journey;
import ch.epfl.rechor.jounrey.Stop;

import java.time.Duration;
import java.time.LocalDateTime;

public final class FormatterFr {
    private FormatterFr() {
    }

    public static String formatDuration(Duration duration) {
        Preconditions.checkArgument(duration != null);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        if (hours == 0) {
            return String.format("%d min", minutes);
        } else {
            return String.format("%d h %d min", hours, minutes);
        }
    }

    public static String formatTime(LocalDateTime dateTime) {
        Preconditions.checkArgument(dateTime != null);
        int hour = dateTime.getHour();
        int minute = dateTime.getMinute();
        return String.format("%dh%02d", hour, minute);
    }

    public static String formatPlatformName(Stop stop) {
        Preconditions.checkArgument(stop != null);
        String platformName = stop.platformName();
        if (platformName == null || platformName.isEmpty()) {
            return "";
        } else if (Character.isDigit(platformName.charAt(0))) {
            return "voie " + platformName;
        } else {
            return "quai " + platformName;
        }
    }

    public static String formatLeg(Journey.Leg.Foot footLeg) {
        Preconditions.checkArgument(footLeg != null);
        LocalDateTime depTime = footLeg.depTime();
        LocalDateTime arrTime = footLeg.arrTime();
        Duration difference = Duration.between(depTime, arrTime);
        if (footLeg.depStop().name().equals(footLeg.arrStop().name())) {
            return "changement (" + formatDuration(difference) + ")";
        } else {
            return "trajet à pied (" + formatDuration(difference) + ")";
        }
    }

    public static String formatLeg(Journey.Leg.Transport leg) {
        Preconditions.checkArgument(leg != null);
        String depStopName = leg.depStop().name();
        String arrStopName = leg.arrStop().name();
        LocalDateTime depTime = leg.depTime();
        LocalDateTime arrTime = leg.arrTime();
        String formattedDepPlatformName = formatPlatformName(leg.depStop());
        String formattedArrPlatformName = formatPlatformName(leg.arrStop());

        StringBuilder sb = new StringBuilder();
        sb.append(formatTime(depTime)).append(" ").append(depStopName);
        if (!formattedDepPlatformName.isEmpty()) {
            sb.append(" (").append(formattedDepPlatformName).append(")");
        }
        sb.append(" → ").append(arrStopName).append(" (").append("arr. ").append(formatTime(arrTime));
        if (!formattedArrPlatformName.isEmpty()) {
            sb.append(" ").append(formattedArrPlatformName);
        }
        sb.append(")");
        return sb.toString();
    }

    public static String formatRouteDestination(Journey.Leg.Transport transportLeg) {
        return transportLeg.route() + " Direction " + transportLeg.destination();
    }
}
