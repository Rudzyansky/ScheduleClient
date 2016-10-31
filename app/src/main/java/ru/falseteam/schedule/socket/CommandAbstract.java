package ru.falseteam.schedule.socket;

public abstract class CommandAbstract implements CommandInterface {
    private String name;

    public CommandAbstract(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
