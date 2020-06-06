package gui;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WindowStatesDepot {

    private static File file = new File(System.getProperty("user.home") + "/WindowSettings.dat");
    private static List<ISaveable> saveableList = new ArrayList<>();

    public static void save() {
        try (ObjectOutputStream stream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            HashMap<String, WindowState> frames = new HashMap<>();
            for (var w : saveableList) {
                frames.put(w.getTitle(), w.getWinSettings());
            }
            stream.writeObject(frames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load () {
        if (file.exists()) {
            try (ObjectInputStream stream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                HashMap<String, WindowState> frames = (HashMap) stream.readObject();

                    for (ISaveable f : saveableList) {
                        f.loadWinSettings(frames.get(f.getTitle()));
                    }
                } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void add(ISaveable saveable){
        saveableList.add(saveable);
    }
}
