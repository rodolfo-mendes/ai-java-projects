package ai.rodolfomendes.travel.chat;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class Dates {
    private Dates() {
    }

    /**
     * Return the current data in the format: Monday, September 15, 2025
     */
    public static String currentDateFormatted() {
        return DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")
            .withZone(ZoneId.systemDefault())
            .format(Instant.now());
    }
}
