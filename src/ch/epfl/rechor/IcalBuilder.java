package ch.epfl.rechor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

public final class IcalBuilder {
    private final StringBuilder sb = new StringBuilder();
    private static final String CRLF = "\r\n";


    private final DateTimeFormatter fmt = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR)
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .appendLiteral('T')
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .toFormatter();

    private final List<Component> components = new ArrayList<>();


    public IcalBuilder add(Name name, String value) {
        String fullLine = name + ":" + value;
        int index = 0;
        if (fullLine.length() > 75) {
            this.sb.append(fullLine.substring(index, 75)).append(CRLF);

            index = 75;
            while (index < fullLine.length()) {
                int end = Math.min(index + 74, fullLine.length());
                this.sb.append(" ").append(fullLine.substring(index, end)).append(CRLF);
                index += 74;
            }
        } else {
            this.sb.append(name).append(":").append(value).append(CRLF);
        }
        return this;
    }


    public IcalBuilder add(Name name, LocalDateTime dateTime) {
        this.sb.append(name).append(":").append(this.fmt.format(dateTime)).append(CRLF);
        return this;
    }

    public IcalBuilder begin(Component component) {
        this.sb.append("BEGIN:").append(component).append(CRLF);
        this.components.add(component);
        return this;
    }

    public IcalBuilder end() {
        Preconditions.checkArgument(!this.components.isEmpty());
        Component lastComponent = this.components.getLast();
        this.sb.append("END:").append(lastComponent.toString()).append(CRLF);
        this.components.removeLast();
        return this;
    }

    public String build() {
        Preconditions.checkArgument(this.components.isEmpty());
        return this.sb.toString();
    }

    public enum Component {
        VCALENDAR,
        VEVENT
    }

    public enum Name {
        BEGIN,
        END,
        PRODID,
        VERSION,
        UID,
        DTSTAMP,
        DTSTART,
        DTEND,
        SUMMARY,
        DESCRIPTION
    }
}
