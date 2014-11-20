package com.pendla.googlecalendaralarm.clock;

import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;

import org.joda.time.DateTime;
import org.joda.time.ReadableDuration;

import java.io.IOException;

/**
 * Created by Simon on 2014-11-19.
 */
public class ClockPresenter {

    public static final int REQUEST_CODE_PICK_ACCOUNT = 1000;

    private ClockModel clockModel;
    private ClockView clockView;

    public ClockPresenter(ClockView view){
        clockView = view;
        clockModel = new ClockModel(clockView);
    }

    public void skipEventClicked(){
        //TODO: Implementation of calling model, skipping the currently set event and receiving a new time.
        AsyncTask task = new AsyncTask() {

            private Calendar calendarService;

            @Override
            protected Object doInBackground(Object[] objects) {
                try {                                                      //TODO Implement shared preferences account here instead.
                    String authToken = GoogleAuthUtil.getToken(clockView, "peterssonaren@gmail.com", "oauth2:https://www.googleapis.com/auth/calendar");
                    GoogleCredential googleCredential = new GoogleCredential().setAccessToken(authToken);
                    calendarService = new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), googleCredential)
                            .setApplicationName("pendla-alarm-14")
                            .build();
                    clockModel.skipCurrentEvent(calendarService);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UserRecoverableAuthException e) {
                    clockView.startActivityForResult(e.getIntent(), 1002);
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                DateTime newAlarmTime = clockModel.getAlarmTime();
                clockView.setClockTime(newAlarmTime.getHourOfDay() + ":" + newAlarmTime.getMinuteOfHour());
            }
        };
        task.execute();
    }

    public void syncAlarmClicked(){
        AsyncTask task = new AsyncTask() {

            private Calendar calendarService;

            @Override
            protected Object doInBackground(Object[] objects) {
                try {                                                      //TODO Implement shared preferences account here instead.
                    String authToken = GoogleAuthUtil.getToken(clockView, "peterssonaren@gmail.com", "oauth2:https://www.googleapis.com/auth/calendar");
                    GoogleCredential googleCredential = new GoogleCredential().setAccessToken(authToken);
                    calendarService = new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), googleCredential)
                            .setApplicationName("pendla-alarm-14")
                            .build();
                    clockModel.syncAlarmWithCalendar(calendarService);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UserRecoverableAuthException e) {
                    clockView.startActivityForResult(e.getIntent(), 1002);
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                DateTime newAlarmTime = clockModel.getAlarmTime();
                clockView.setClockTime(newAlarmTime.getHourOfDay() + ":" + newAlarmTime.getMinuteOfHour());
            }
        };
        task.execute();
    }
}
