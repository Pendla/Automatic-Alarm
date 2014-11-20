package com.pendla.googlecalendaralarm.clock;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;

/**
 * Created by Simon on 2014-11-19.
 */
public class ClockModel {

    private Event currentAlarmEvent;
    private DateTime currentAlarmTime;
    private Context context;

    //TODO Get a list of these from shared preferences. Making it possible to setup what calendars
    //     you want to sync with the alarm.
    private final String calendarId = "ctk640g093l8bfomtvsmjf0ujs@group.calendar.google.com";

    public ClockModel(Context context){
        this.context = context;
        currentAlarmTime = null;
    }

    /**
     * Skips over the event that currently has specified the time for the alarm and sets the alarm
     * according to the next event in the calendar.
     * @param calendarService The GoogleCalender service to use while getting events.
     * @return The time that the alarm was set to, null if it wasn't set.
     */
    public DateTime skipCurrentEvent(Calendar calendarService){
        boolean foundCurrentEvent = false;

        //Loop through all events that can be found.
        for(Event event : getEvents(calendarService)){
            if(foundCurrentEvent){
                DateTime twoHoursBeforeEvent = new DateTime(event.getStart().getDateTime().getValue()).minusHours(2);
                currentAlarmTime = twoHoursBeforeEvent;
                setAlarm(twoHoursBeforeEvent);
                currentAlarmEvent = event;
                return twoHoursBeforeEvent;
            } else if(event.equals(currentAlarmEvent)){
                foundCurrentEvent = true;
            }
        }

        return null;
    }

    /**
     * Method that syncs the alarm on the device with the Google calendar that is specified by the
     * calendar service and the calenderId.
     * @param calendarService The GoogleCalender service to use while getting events.
     * @return The time that the alarm was set to, null if it wasn't set.
     */
    public DateTime syncAlarmWithCalendar(Calendar calendarService){
        List<Event> eventList = getEvents(calendarService);
        if(eventList.size() > 0) {
            Event event = eventList.get(0);
            currentAlarmEvent = event;
            DateTime twoHoursBeforeEvent = new DateTime(event.getStart().getDateTime().getValue()).minusHours(2);
            currentAlarmTime = twoHoursBeforeEvent;
            setAlarm(twoHoursBeforeEvent);
            return twoHoursBeforeEvent;
        }

        return null;
    }

    /**
     * Sets an alarm on the phone.
     * @param dateTime The time to set the alarm.
     */
    public void setAlarm(DateTime dateTime){
        Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, dateTime.getHourOfDay());
        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, dateTime.getMinuteOfHour());
        alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        alarmIntent.putExtra(AlarmClock.EXTRA_VIBRATE, true);
        context.startActivity(alarmIntent);
    }

    /**
     * Get the next alarm that is going to be fired from the phone.
     * @return The next alarm that is going to be fired from the phone.
     */
    public DateTime getAlarmTime(){
        return currentAlarmTime;
    }

    /**
     * Helper method that retrieves all events in the calendar with the specified ID, from 00:00:00
     * tommorrow to 23:59:59 tommorow.
     * @param calendarService The GoogleCalender service to use while getting events.
     * @return A List of events tommorow in the calendar with calenderId as ID.
     */
    private List<Event> getEvents(Calendar calendarService){
        DateTime today = DateTime.now();
        DateTime timeMin = new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth()+1, 00, 00, 00);
        DateTime timeMax = new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth()+1, 23, 59, 59);

        try {
            return calendarService.events().list(calendarId)
                    .setTimeMin(new com.google.api.client.util.DateTime(timeMin.getMillis()))
                    .setTimeMax(new com.google.api.client.util.DateTime(timeMax.getMillis()))
                    .execute().getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}