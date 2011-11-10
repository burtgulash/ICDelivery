import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static constant.Times.*;

public class TimeConverter {
    public static String ascTime(int time) {
        int days   = time / DAY.time() + 1;
        int hours  = (time % DAY.time()) / MINUTES_IN_HOUR.time();
        int mins   = (time % DAY.time()) % MINUTES_IN_HOUR.time();;
        return String.format("Day %1d  %02d:%02d", days, hours, mins);
    }

    public static int toMinutes(int currentTime, String time) {
        int NIL = -1;
        Pattern timePattern = Pattern.compile(
                                      "\\+?(?:(\\d+):)?(?:(\\d+):)?(\\d+)");
        Matcher m = timePattern.matcher(time);

        if (!m.matches()) // error
            return NIL;

        int result = 0;
        if (time.startsWith("+"))
            result = currentTime;

        try {
            if (m.group(1) != null && m.group(2) != null)
                result += DAY.time() * Integer.parseInt(m.group(1));
            else if (m.group(1) != null)
                result += MINUTES_IN_HOUR.time() * Integer.parseInt(m.group(1));
            else if (m.group(2) != null)
                result += MINUTES_IN_HOUR.time() * Integer.parseInt(m.group(2));

            // minutes must be always present
            result += Integer.parseInt(m.group(3));
        } catch (NumberFormatException ex) {
            System.err.println();
            return NIL;
        }
        return result;
    }
}
