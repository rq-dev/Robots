package gui;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SaveableFrame extends JFrame implements ISaveable{

    protected WindowState windowState;

    public SaveableFrame(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowIconified(WindowEvent e) {
                windowState.setIconified(true);
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                windowState.setIconified(false);
                if (windowState.getMaximized())
                    setExtendedState(MAXIMIZED_BOTH);
            }
            });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                windowState.setDimension(e.getComponent().getSize());
                if (getExtendedState() == JFrame.MAXIMIZED_BOTH)
                    windowState.setMaximized(true);
                else
                    windowState.setMaximized(false);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                windowState.setLocation(e.getComponent().getLocation());
            }
        });
    }

    @Override
    public void loadWinSettings(WindowState windowState) {
        if(windowState.getIconified())
            setExtendedState(ICONIFIED);
        else if (windowState.getMaximized())
            setExtendedState(MAXIMIZED_BOTH);
        setSize(windowState.getDimension());
        setLocation(windowState.getLocation());
        this.windowState = windowState;
    }

    @Override
    public WindowState getWinSettings(){
        return  windowState;
    }
}
