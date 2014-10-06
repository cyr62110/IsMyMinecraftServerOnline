package fr.cvlaminck.immso.utils;

import android.content.Context;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.Date;

import fr.cvlaminck.immso.R;

@EBean
public class TimeFormatter {

    @RootContext
    protected Context context;

    private Object timeUnits[][] = null;

    @AfterInject
    protected void afterInject() {
        timeUnits = new Object[][]{
            {60L, context.getString(R.string.timeformatter_seconds)},
            {60L, context.getString(R.string.timeformatter_minutes)},
            {24L, context.getString(R.string.timeformatter_hours)},
            {0L, context.getString(R.string.timeformatter_days)}
        };
    }

    public String format(long time) {
        time /= 1000L;
        int unitToUse = 0;
        while(unitToUse < (timeUnits.length - 1) && time > ((Long)timeUnits[unitToUse][0]).longValue()) {
            time /= ((Long)timeUnits[unitToUse][0]).longValue();
            unitToUse ++;
        }
        final String timeUnit = (String) timeUnits[unitToUse][1];
        if(time == 1)
            return "one " + timeUnit.substring(0, timeUnit.length() - 1);
        else
            return Long.toString(time) + " " + timeUnit;
    }

}
