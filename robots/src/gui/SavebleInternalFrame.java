package gui;

import javax.swing.*;

public class SavebleInternalFrame extends JInternalFrame implements ISaveable {

    public SavebleInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }

    @Override
    public void loadWinSettings(WindowState windowState) {
        try {
            setIcon(windowState.getIconified());
            setMaximum(windowState.getMaximized());
        }
        catch (Exception e) {
            System.out.println("Exception!");
        }
        setLocation(windowState.getLocation());
        setSize(windowState.getDimension());
    }

    @Override
    public WindowState getWinSettings() {
        return new WindowState(isIcon(), isMaximum(), getSize(), getLocation(), getTitle());
    }

    @Override
    public String getTitle(){
        return  this.title;
    }
}