package ch.epfl.rechor.journey;

import ch.epfl.rechor.FormatterFr;
import ch.epfl.rechor.IcalBuilder;

import java.time.LocalDateTime;
import java.util.StringJoiner;
import java.util.UUID;

public class JourneyIcalConverter {
    private static final IcalBuilder icalBuilder = new IcalBuilder();
    private static StringJoiner sj = new StringJoiner("\\n");

    private JourneyIcalConverter() {
    }

    public static String toIcalendar(Journey journey) {
        icalBuilder.begin(IcalBuilder.Component.VCALENDAR);
        icalBuilder.add(IcalBuilder.Name.VERSION, "2.0");
        icalBuilder.add(IcalBuilder.Name.PRODID, "ReCHor");
        icalBuilder.begin(IcalBuilder.Component.VEVENT);
        icalBuilder.add(IcalBuilder.Name.UID, UUID.randomUUID().toString());
        icalBuilder.add(IcalBuilder.Name.DTSTAMP, LocalDateTime.now());
        icalBuilder.add(IcalBuilder.Name.DTSTART, journey.depTime());
        icalBuilder.add(IcalBuilder.Name.DTEND, journey.arrTime());

        icalBuilder.add(IcalBuilder.Name.SUMMARY, journey.depStop().name() + " → " + journey.arrStop().name());

        for (Journey.Leg leg : journey.legs()) {
            switch (leg) {
                case Journey.Leg.Transport t -> sj.add(FormatterFr.formatLeg(t));
                case Journey.Leg.Foot f -> sj.add(FormatterFr.formatLeg(f));
            }
        }

        icalBuilder.add(IcalBuilder.Name.DESCRIPTION, sj.toString());
        icalBuilder.end();
        icalBuilder.end();
        return icalBuilder.build();


    }
}
