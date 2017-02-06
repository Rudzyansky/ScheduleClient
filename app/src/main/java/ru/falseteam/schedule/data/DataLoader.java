package ru.falseteam.schedule.data;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ru.falseteam.vframe.redraw.Redrawer;

public class DataLoader {
    public static void init(Context context) {
        StaticData.init(context);
        MainData.init(context);
        VkData.init(context);

        Redrawer.redraw();
    }

    /**
     * @param path path to file.
     * @param <T>  binary file cast to T
     * @return {T} if successfully read binary file of {null} if not.
     */
    @SuppressWarnings("unchecked")
    static <T> T loadFromBinaryFile(String path) {
        T data;
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(path));
            data = (T) stream.readObject();
            stream.close();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return data;
    }


    /**
     * @param data data to save
     * @param path path to save
     * @param <T>  type of data
     * @throws {@link RuntimeException} if can not save file.
     */
    @SuppressWarnings("JavaDoc")
    static <T> void saveToBinaryFile(T data, String path) {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(path));
            stream.writeObject(data);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
