package ru.falseteam.schedule.socket.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.data.Data;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.socket.CommandAbstract;

public class GetTemplates extends CommandAbstract {
    public static List<Template> templates;

    public GetTemplates() {
        super("get_templates");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map) {
        templates = (List<Template>) map.get("templates");
        save();
        Redrawer.redraw();
    }

    public static Map<String, Object> getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_templates");
        return map;
    }

    private void save() {
        try {
            File file = new File(Data.getContext().getApplicationInfo().dataDir + "/templates.bin");
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(templates);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void load() {
        try {
            File file = new File(Data.getContext().getApplicationInfo().dataDir + "/templates.bin");
            if (!file.exists()) return;
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            templates = (List<Template>) in.readObject();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ignore) {
        }
    }
}
