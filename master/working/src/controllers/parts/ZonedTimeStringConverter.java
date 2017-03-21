package controllers.parts;

import javafx.util.StringConverter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Marcello De Bernardi
 */
public class ZonedTimeStringConverter extends StringConverter<ZonedDateTime> {
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override public String toString(ZonedDateTime object) {
        return object.format(dateFormatter);
    }

    @Override public ZonedDateTime fromString(String string) {
        return ZonedDateTime.parse(string, dateFormatter);
    }
}
