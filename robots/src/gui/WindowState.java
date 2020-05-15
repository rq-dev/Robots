package gui;

import java.awt.*;
import java.io.Serializable;

public class WindowState implements Serializable {

    private Boolean isIconified;
    private Boolean isMaximized;
    private Dimension dimension;
    private Point location;
    private String name;

    public WindowState(Boolean isIconified, Boolean isMaximazed, Dimension dimension, Point location, String name) {
        this.isIconified = isIconified;
        this.isMaximized = isMaximazed;
        this.dimension = dimension;
        this.location = location;
        this.name = name;
    }

    public Boolean getIconified() {
        return isIconified;
    }

    public void setIconified(Boolean iconifield) {
        isIconified = iconifield;
    }

    public Boolean getMaximized() {
        return isMaximized;
    }

    public void setMaximized(Boolean maximized) {
        isMaximized = maximized;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
