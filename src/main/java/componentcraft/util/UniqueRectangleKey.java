package componentcraft.util;

import java.awt.*;

public class UniqueRectangleKey {
    private final Rectangle rectangle;

    public UniqueRectangleKey(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }
}