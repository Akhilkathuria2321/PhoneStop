package com.alpgeeks.phonestop.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Stores the time and app details corresponding to a PhoneStop profile.
 * Time - Duration/Timings of the day during which phonestop is on.
 *        Also, the days of week that enable the phonestop thing.
 * Apps - The apps which
 */
public class Profile {
    private String profileName;
    private List<Day> phoneStopDays = new ArrayList<>();
    private List<String> timeSlots = new ArrayList<>();
    private List<String> apps = new ArrayList<>();

    public Profile(String name) {
        profileName = name;
    }

    public String getProfileName() {
        return profileName;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Days : ");
        for(Day day:phoneStopDays)
            builder.append(day);

        builder.append("Time Slots : ");
        for(String timeSlot:timeSlots)
            builder.append(timeSlot);

        return profileName + " :: " +builder.toString();
    }

    public void addDay(Day day) {
        phoneStopDays.add(day);
    }

    public void addSlot(String timeSlot) {
        timeSlots.add(timeSlot);
    }

    public void setProfile(String profileName) {
        this.profileName = profileName;
    }
}