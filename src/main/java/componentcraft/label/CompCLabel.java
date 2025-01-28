package componentcraft.label;

import componentcraft.components.AbstractComponent;
import componentcraft.events.componentObserver.ComponentObserver;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseAdapter;
import java.util.*;
import java.util.List;

public class CompCLabel extends JLabel {

    /**
     * Object to describe the first render of the label
     */
    private FirstRender firstRender =  new FirstRender();
    /**
     * List of all the rendered components
     */
    private List<AbstractComponent> componentList = new ArrayList<>();

    protected CompCLabel(){
        super();
    }

    /**
     * BackgroundColor which can be null or a Color to set the JFrame-Background-color
     */
    @Setter
    private Color backgroundColor = Color.WHITE;


    protected void addComponent(AbstractComponent component){
        this.componentList.add(component);
    }

    /**
     * Draw-Interface which is called before the component draw.
     */
    private Draw beforeDraw;

    /**
     * @param beforeDraw is Draw. Add the before Draw-Interface.
     */
    public void addBeforeDraw(Draw beforeDraw){
        this.beforeDraw = beforeDraw;
    }

    /**
     * Draw-Interface which is called after the component draw.
     */
    private Draw afterDraw;

    /**
     *
     * @param afterDraw is Draw. Add the after Draw-Interface
     */
    public void addAfterDraw(Draw afterDraw){
        this.afterDraw = afterDraw;
    }



    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if(backgroundColor != null){
            Color beforeColor = g2.getColor();
            g2.setColor(backgroundColor);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(beforeColor);
        }



        if(beforeDraw != null){
            beforeDraw.draw(g2);
        }

        for(AbstractComponent component : componentList){
            if(component.isVisible()){
                component.draw(g2);
            }
        }

        if(afterDraw != null){
            afterDraw.draw(g2);
        }

        firstRender.firstRenderDone();
    }
    /**
     ComponentObserver can be changed to custom or use the default which set by CompCLabelBuilder
     */
    private ComponentObserver componentObserver;

    public void setComponentObserver(ComponentObserver componentObserver){
        this.componentObserver = componentObserver;
        componentList.forEach(comp -> comp.addObserver(componentObserver));
    }


    /**
        MouseAdapter can be changed to custom or use the default which set by CompCLabelBuilder
     */
    private MouseAdapter mouseAdapter;

    public void setMouseAdapter(MouseAdapter mouseAdapter){
        this.mouseAdapter = mouseAdapter;
        addMouseListener(this.mouseAdapter);
    }

    /**
     * ComponentAdapter can be changed to custom or use the default which set by CompCLabelBuilder
     */
    private ComponentAdapter componentAdapter;

    public void setComponentAdapter(ComponentAdapter componentAdapter, JFrame frame){
        this.componentAdapter = componentAdapter;
        frame.addComponentListener(componentAdapter);
    }





}
