package componentcraft.components.menu;


import componentcraft.components.AbstractComponent;
import componentcraft.components.Button;
import componentcraft.components.Shadow;
import componentcraft.events.OnEvent;
import componentcraft.events.componentObserver.ComponentObserver;
import componentcraft.events.componentObserver.ComponentObserverType;
import componentcraft.events.componentObserver.Source;
import componentcraft.util.Coordinates;
import componentcraft.util.UniqueRectangleKey;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public class MultipleComponentMenuHorizontal extends AbstractComponent {


    private Map<UniqueRectangleKey, AbstractComponent> componentList =  new LinkedHashMap<>();


    @Setter
    private Color backgroundColor;

    public MultipleComponentMenuHorizontal(Coordinates coordinates, Dimension dimension, List<AbstractComponent> components) {
        super(coordinates, dimension, UUID.randomUUID().toString());
        for(AbstractComponent component : components){
            Rectangle rect = new Rectangle(component.getDimension());
            componentList.put(new UniqueRectangleKey(rect), component);
        }
    }

    @Override
    public void drawShadow(Graphics2D g2d, Shadow shadow){
        Color berforeColor = g2d.getColor();

        int transparency = shadow.getTransparency();
        int shadowX = shadow.getOffsetX();
        int shadowY = shadow.getOffsetY();

        g2d.setColor(new Color(berforeColor.getRed(), berforeColor.getGreen(), berforeColor.getBlue(),  transparency));

        g2d.fillRect(shadowX, shadowY ,getDimension().width, getDimension().height);
        g2d.setColor(berforeColor);
    }


    @Override
    public void clickEvent(MouseEvent e){

        rightScroll.clickMouse(e, () -> {
            scrollPost += scrollSteps;
            updateOnDraw = true;
            super.updateAllObserver();
        });

        leftScroll.clickMouse(e, () -> {
            scrollPost -= scrollSteps;
            updateOnDraw = true;
            super.updateAllObserver();
        });

        int x = e.getX();
        int y = e.getY();

        for(UniqueRectangleKey hitBox : componentList.keySet()){
            Rectangle rect = hitBox.getRectangle();
            if(rect != null){
                if(rect.contains(x- getX(), y - getY())){
                    AbstractComponent abstractComponent = componentList.get(hitBox);
                    abstractComponent.clickEvent(e);
                    updateOnDraw = true;
                    super.updateAllObserver();
                }
            }



        }
    }

    BufferedImage bufferedMenu = null;

    public void update(){


        BufferedImage bufferedImage =  new BufferedImage(getDimension().width + getShadow().getOffsetX() , getDimension().height + getShadow().getOffsetY(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = bufferedImage.createGraphics();

        Color beforeColor = g.getColor();




        int locX = 0;
        int locY = 0;

        int height =  getDimension().height;
        int width =  getDimension().width;

        int blankSpaceX = width / 8 ;
        int blankSpaceY = height/ 8 ;






        //Background
        if(backgroundColor != null){
            g.setColor(backgroundColor);
        }


        //Shadow
        if(getShadow() != null){
            if(getShadow().isEnable()){
                getShadow().draw(g, this);
            }
        }


        g.fillRect(locX,locY, width, height);


        AtomicReference<AbstractComponent> previous = new AtomicReference<>(null);
        Map<UniqueRectangleKey, AbstractComponent> buffer =  new LinkedHashMap<>();

        Map<Rectangle, Coordinates> beforeCoordinates = new HashMap<>();


        componentList.values().forEach( component -> {


            if(previous.get() == null){
                int maxHeight = Math.min(height - 2 * blankSpaceY, component.getDimension(). height);
                int space = Math.max(blankSpaceY, (height - maxHeight) /2);

                int tempX = locX + blankSpaceX;
                int tempY = locY  +  space;

                component.setCoordinates( tempX - scrollPost , tempY );
                component.setDimension(new Dimension((int) component.getDimension().getWidth(), maxHeight));


            }else{


                int maxHeight = Math.min(height - 2 * blankSpaceY, previous.get().getDimension(). height);
                int space = Math.max(blankSpaceY, (height - maxHeight) /2);


                int tempY = locY  +  space;
                int tempX = previous.get().getX() + previous.get().getDimension().width + blankSpaceY;

                Dimension tempDimension = new Dimension((int) component.getDimension().getWidth(), maxHeight);

                component.setCoordinates(tempX, tempY);
                component.setDimension(tempDimension);


            }

            Rectangle rectToggle = new Rectangle(component.getX(), component.getY() , component.getDimension().width,component.getDimension().height);
            buffer.put(new UniqueRectangleKey(rectToggle), component);


            int x = component.getX() + getX();
            int y = component.getY() + getY();

            beforeCoordinates.put(rectToggle, new Coordinates(x, y));

            component.draw(g);
            previous.set(component);
        });
        componentList = buffer;



        componentList.forEach((rectangle, component) -> {
            component.setCoordinates(beforeCoordinates.get(rectangle.getRectangle()));
        });



//        Logger.drawMiddle(g, this, Color.white);




        g.dispose();

        try {
            File outputfile = new File("saved_menu.png");
            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        this.bufferedMenu = bufferedImage;

        g.setColor(beforeColor);
        updateOnDraw = false;

        updateAllObserver();
        updateAllObserver(new ComponentObserverType(new Source(getName(), this.getClass(), null), ComponentObserverType.REPAINT));
        updateAllObserver(new ComponentObserverType(new Source(getName(), this.getClass(), null), ComponentObserverType.MULTIPLE_COMPONENTS_MENU_STATE_CHANGED));


    }


    boolean updateOnDraw = true;

    int scrollPost = 0 ;


    componentcraft.components.Button rightScroll = new componentcraft.components.Button(">");
    componentcraft.components.Button leftScroll =  new Button("<");

    @Setter
    @Getter
    private int buttonSize = Math.min(getDimension().width / 7 , getDimension().height / 7);

    @Getter
    @Setter
    private int scrollSteps = 30;

    @Getter
    @Setter
    private boolean disableScroll = false;

    @Override
    public void addClickListener(OnEvent componentClickListener){
        super.addClickListener(componentClickListener);
        rightScroll.addClickListener(componentClickListener);
        leftScroll.addClickListener(componentClickListener);
    }


    @Override
    public void draw(Graphics2D g2d) {

        if(updateOnDraw){
            update();
        }{
            g2d.drawImage(bufferedMenu, getX(), getY(), null);
            if(disableScroll){
                rightScroll.setRepositionAndSize(getX(), getY() + getDimension().height - buttonSize,buttonSize, buttonSize);
                rightScroll.draw(g2d);

                leftScroll.setRepositionAndSize(getX() + getDimension().width - buttonSize, getY() + getDimension().height - buttonSize,buttonSize, buttonSize);
                leftScroll.draw(g2d);
            }
        }
    }

    @Override
    public void addObserver(ComponentObserver componentObserver){
        componentList.values().forEach(abstractComponent -> abstractComponent.addObserver(componentObserver));
    }

}
