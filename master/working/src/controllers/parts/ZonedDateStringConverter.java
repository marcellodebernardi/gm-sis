package controllers.parts;

import javafx.util.StringConverter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Marcello De Bernardi
 */
public class ZonedDateStringConverter extends StringConverter<ZonedDateTime> {
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override public String toString(ZonedDateTime object) {
        return object.format(dateFormatter);
    }

    @Override public ZonedDateTime fromString(String string) {
        return ZonedDateTime.parse(string, dateFormatter);
    }
}
