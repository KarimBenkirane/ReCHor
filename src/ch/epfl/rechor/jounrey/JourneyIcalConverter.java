package ch.epfl.rechor.jounrey;

import ch.epfl.rechor.FormatterFr;
import ch.epfl.rechor.IcalBuilder;

import java.time.LocalDateTime;
import java.util.StringJoiner;
import java.util.UUID;

public class JourneyIcalConverter {
    private final IcalBuilder icalBuilder = new IcalBuilder();
    private final StringJoiner sj = new StringJoiner("\n");

    private JourneyIcalConverter() {
    }

    public String toIcalendar(Journey journey) {
        this.icalBuilder.begin(IcalBuilder.Component.VCALENDAR);
        this.icalBuilder.add(IcalBuilder.Name.PRODID, "ReCHor");
        this.icalBuilder.begin(IcalBuilder.Component.VEVENT);
        this.icalBuilder.add(IcalBuilder.Name.UID, UUID.randomUUID().toString());
        this.icalBuilder.add(IcalBuilder.Name.DTSTAMP, LocalDateTime.now());
        this.icalBuilder.add(IcalBuilder.Name.DTSTART, journey.depTime());
        this.icalBuilder.add(IcalBuilder.Name.DTEND, journey.arrTime());
        this.icalBuilder.add(IcalBuilder.Name.SUMMARY, "");

        for (Journey.Leg leg : journey.legs()) {
            switch (leg) {
                case Journey.Leg.Transport t -> this.sj.add(FormatterFr.formatLeg(t));
                case Journey.Leg.Foot f -> this.sj.add(FormatterFr.formatLeg(f));
            }
        }

        this.icalBuilder.add(IcalBuilder.Name.DESCRIPTION, this.sj.toString());
        this.icalBuilder.end();
        this.icalBuilder.end();
        return this.icalBuilder.build();


    }
}
