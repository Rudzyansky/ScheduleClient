package ru.falseteam.schedule.serializable;

import java.io.Serializable;

/**
 * Created by Prog on 01.11.16.
 */
public class User implements Serializable {
    public boolean exists;
    public String name;
    public String vkToken;
    public int vkId;
    public Groups group;

    private User() {
    }

    public static class Factory {
        public static User getDefault() {
            User user = new User();
            user.exists = false;
            user.group = Groups.guest;
            return user;
        }
    }
}
