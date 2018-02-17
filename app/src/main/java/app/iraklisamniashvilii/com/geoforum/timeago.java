package app.iraklisamniashvilii.com.geoforum;

import android.content.Context;

/**
 * Created by odina on 2/16/2018.
 */

public class timeago {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public String gettimeago(Long time, Context context) {


        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();


        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "ახლახანს";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "რამდენიმე წუთის წინ";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " წუთის წინ";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "1 საათის წინ";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " საათის წინ";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "გუშინ";
        } else {
            return diff / DAY_MILLIS + " დღის წინ";
        }

    }
}