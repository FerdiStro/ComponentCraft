package componentcraft.components;

import componentcraft.events.componentObserver.ComponentObserver;
import componentcraft.events.componentObserver.ComponentObserverType;
import componentcraft.events.OnEvent;
import componentcraft.events.OnEventType;
import componentcraft.util.Coordinates;
import componentcraft.util.log.LogMassagesBuilder;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;


@Getter
public abstract class AbstractComponent {

    protected static final Logger logger = LogManager.getLogger();

    @Setter
    @Getter
    private boolean visible = true;


    @Getter
    private final String name;


    private  int x;
    private  int y;
    private Coordinates coordinates;

    @Setter
    private Dimension dimension = new Dimension(0,0);

    /**
     * Shadow can be set if shadow shout displayed
     */
    @Getter
    @Setter
    private Shadow shadow = null;



    public AbstractComponent( Coordinates coordinates, String name) {
        logger.debug(LogMassagesBuilder.initMessage(this.getClass()));
        this.x = coordinates.getX();
        this.y = coordinates.getY();
        this.coordinates = coordinates;
        this.name = name;
    }

    public AbstractComponent(Coordinates coordinates, Dimension dimension, String name) {
        logger.debug(LogMassagesBuilder.initMessage(this.getClass()));
        this.x = coordinates.getX();
        this.y = coordinates.getY();
        this.coordinates = coordinates;
        this.dimension =  dimension;
        this.name = name;
    }

    public void draw(Graphics2D g2d) {
        if(shadow != null){
            if(shadow.isEnable()){
                shadow.draw(g2d, this);
            }
        }
        g2d.setBackground(Color.black);
        g2d.fillRect(coordinates.getX(), coordinates.getY(), (int) dimension.getWidth(), (int) dimension.getHeight());
    }

    public void setX(int x){
        coordinates.setX(x);
        this.x = x;
    }

    public void setY(int y){
        coordinates.setY(y);
        this.y = y;
    }

    public void clickEvent(MouseEvent e){
        if(this.componentClickListener != null){
            componentClickListener.onEvent();
        }else {
            logger.error("Component ClickListener is null");
        }
    }

    public void clickMethode(OnEventType onEventType){
        if(this.componentClickListener != null){
            this.componentClickListener.onEvent(onEventType);

        }else {
            logger.error("Component ClickListener is null");
        }
    }



    public void drawShadow(Graphics2D g2d, Shadow shadow){

        Color berforeColor = g2d.getColor();

        int shadowX = getX() + shadow.getOffsetX();
        int shadowY = getY() + shadow.getOffsetY();
        int transparency = shadow.getTransparency();

        g2d.setColor(new Color(berforeColor.getRed(), berforeColor.getGreen(), berforeColor.getBlue(),  transparency));

        g2d.fillRect(shadowX, shadowY ,getDimension().width, getDimension().height);
        g2d.setColor(berforeColor);
    }

    private OnEvent componentClickListener;

    public void addClickListener(OnEvent componentClickListener){
        this.componentClickListener = componentClickListener;
    }


    public void setCoordinates(Coordinates coordinates){
        this.x = coordinates.getX();
        this.y = coordinates.getY();
        this.coordinates = coordinates;
    }

    public void setCoordinates(int x, int y){
        this.x = x;
        this.y = y;
        this.coordinates =   new Coordinates(x, y);
    }

    public void setRepositionAndSize(int x, int y, int width, int height ){
        setCoordinates(x, y);
        setDimension(new Dimension(width, height));
    }


    private final List<ComponentObserver> componentObserverList =  new ArrayList<>();

    public void addObserver(ComponentObserver componentObserver){
        componentObserverList.add(componentObserver);
    }

    public void removeObserver(ComponentObserver componentObserver){
        componentObserverList.remove(componentObserver);
    }

    public void updateAllObserver(){
        componentObserverList.forEach(ComponentObserver::update);
    }

    public void updateAllObserver(ComponentObserverType onEventType){
     componentObserverList.forEach(observer -> observer.update(onEventType));
    }

    public void resizeDimensions(Dimension width, Dimension height){
    }



}
