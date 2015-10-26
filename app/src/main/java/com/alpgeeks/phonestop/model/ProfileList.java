package com.alpgeeks.phonestop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akki on 10/25/2015.
 */
public class ProfileList {
    private List<Profile> userProfiles;
    private ProfileList() {
        userProfiles = new ArrayList<>();
    }

    private static ProfileList INSTANCE;
    public static ProfileList getInstance() {
        if(INSTANCE == null) INSTANCE = new ProfileList();
        return INSTANCE;
    }

    public void addProfile(Profile profile) {
        userProfiles.add(profile);
    }

    public void loadProfileList() {

    }

    // temp method, study custom adapter
    public List<String> getUserProfiles() {
        List<String> userProfilesString = new ArrayList<>();
        for(Profile userProfile:userProfiles)
            userProfilesString.add(userProfile.getProfileName());
        return userProfilesString;
    }
}
