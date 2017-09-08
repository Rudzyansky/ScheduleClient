package ru.falseteam.schedule.serializable;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable {
    public boolean exists;
    public int id;
    public String name;
    public int atList;
    public int vkId;
    public String vkToken;
    public Groups permissions;
    public Timestamp register;
    public Timestamp lastAuth;
    public int sdkVersion;
    public String appVersion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (exists != user.exists) return false;
        if (id != user.id) return false;
        if (atList != user.atList) return false;
        if (vkId != user.vkId) return false;
        if (sdkVersion != user.sdkVersion) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (vkToken != null ? !vkToken.equals(user.vkToken) : user.vkToken != null) return false;
        if (permissions != user.permissions) return false;
        if (register != null ? !register.equals(user.register) : user.register != null)
            return false;
        if (lastAuth != null ? !lastAuth.equals(user.lastAuth) : user.lastAuth != null)
            return false;
        return appVersion != null ? appVersion.equals(user.appVersion) : user.appVersion == null;

    }

    @Override
    public int hashCode() {
        int result = (exists ? 1 : 0);
        result = 31 * result + id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + atList;
        result = 31 * result + vkId;
        result = 31 * result + (vkToken != null ? vkToken.hashCode() : 0);
        result = 31 * result + (permissions != null ? permissions.hashCode() : 0);
        result = 31 * result + (register != null ? register.hashCode() : 0);
        result = 31 * result + (lastAuth != null ? lastAuth.hashCode() : 0);
        result = 31 * result + sdkVersion;
        result = 31 * result + (appVersion != null ? appVersion.hashCode() : 0);
        return result;
    }

    private User() {
    }

    public static class Factory {
        public static User getDefault() {
            User user = new User();
            user.exists = false;
            user.permissions = Groups.guest;
            user.register = new Timestamp(System.currentTimeMillis());
            user.lastAuth = new Timestamp(0);
            return user;
        }
    }
}
