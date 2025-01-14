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

    private  int x;
    private  int y;
    private Coordinates coordinates;

    @Setter
    private Dimension dimension = new Dimension(0,0);



    public AbstractComponent( Coordinates coordinates) {
        logger.debug(LogMassagesBuilder.initMessage(this.getClass()));
        this.x = coordinates.getX();
        this.y = coordinates.getY();
        this.coordinates = coordinates;
    }

    public AbstractComponent(Coordinates coordinates, Dimension dimension) {
        logger.debug(LogMassagesBuilder.initMessage(this.getClass()));
        this.x = coordinates.getX();
        this.y = coordinates.getY();
        this.coordinates = coordinates;
        this.dimension =  dimension;
    }

    public void draw(Graphics2D g2d) {
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
