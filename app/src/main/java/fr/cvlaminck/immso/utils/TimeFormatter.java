package fr.cvlaminck.immso.utils;

import android.content.Context;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import fr.cvlaminck.immso.R;

@EBean
public class TimeFormatter {

    @RootContext
    protected Context context;

    private Object timeUnits[][] = null;

    private String timeUnitsSingluar[] = null;

    private String timeUnitsSmall[] = null;

    @AfterInject
    protected void afterInject() {
        timeUnits = new Object[][]{
                {60L, context.getString(R.string.timeformatter_seconds)},
                {60L, context.getString(R.string.timeformatter_minutes)},
                {24L, context.getString(R.string.timeformatter_hours)},
                {0L, context.getString(R.string.timeformatter_days)}
        };
        timeUnitsSingluar = new String[]{
                context.getString(R.string.timeformatter_second),
                context.getString(R.string.timeformatter_minute),
                context.getString(R.string.timeformatter_hour),
                context.getString(R.string.timeformatter_day)
        };
        timeUnitsSmall = new String[]{
                context.getString(R.string.timeformatter_seconds_small),
                context.getString(R.string.timeformatter_minutes_small),
                context.getString(R.string.timeformatter_hours_small),
                context.getString(R.string.timeformatter_days_small)
        };
    }

    public String format(long time, boolean small) {
        time /= 1000L;
        int unitToUse = 0;
        while (unitToUse < (timeUnits.length - 1) && time > ((Long) timeUnits[unitToUse][0]).longValue()) {
            time /= ((Long) timeUnits[unitToUse][0]).longValue();
            unitToUse++;
        }
        if (small)
            return Long.toString(time) + timeUnitsSmall[unitToUse];
        else {
            if (time == 1)
                return "one " + timeUnitsSingluar[unitToUse];
            else
                return Long.toString(time) + " " + timeUnits[unitToUse][1];
        }
    }

    public String format(long time) {
        return format(time, false);
    }

}
