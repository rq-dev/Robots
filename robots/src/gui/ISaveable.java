package gui;

public interface ISaveable {

    void loadWinSettings(WindowState windowState);
    WindowState getWinSettings();
    String getTitle();
}
