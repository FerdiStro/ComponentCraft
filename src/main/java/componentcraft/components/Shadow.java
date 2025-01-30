package componentcraft.components;

import lombok.Getter;

import java.awt.*;

public class Shadow {
    @Getter
    private boolean isEnable;
    @Getter
    private final int offsetX;
    @Getter
    private final int offsetY;
    @Getter
    private final int transparency;


    public Shadow(int offsetX, int offsetY, int transparency) {
        this.isEnable = true;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.transparency =  transparency;
    }

    public void draw(Graphics2D g , AbstractComponent component){
        component.drawShadow(g, this);
    }

}
