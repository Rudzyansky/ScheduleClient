package ru.falseteam.schedule.serializable;

import java.io.Serializable;

public class UserPresented implements Serializable {
    public User user;
    public int presented;
    public int notPresented;

    private UserPresented() {
    }

    public static class Factory {
        public static UserPresented getFromUser(User user) {
            UserPresented up = new UserPresented();
            up.user = user;
            up.presented = 0;
            up.notPresented = 0;
            return up;
        }
    }
}
