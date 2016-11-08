package ru.falseteam.schedule.serializable;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable {
    public int id;
    public boolean exists;
    public String name;
    public String vkToken;
    public int vkId;
    public Groups group;
    public Timestamp register;
    public Timestamp lastAuth;
    public int sdkVersion;
    public String appVersion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (exists != user.exists) return false;
        if (vkId != user.vkId) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (vkToken != null ? !vkToken.equals(user.vkToken) : user.vkToken != null) return false;
        return group == user.group;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (exists ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (vkToken != null ? vkToken.hashCode() : 0);
        result = 31 * result + vkId;
        result = 31 * result + (group != null ? group.hashCode() : 0);
        return result;
    }

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
