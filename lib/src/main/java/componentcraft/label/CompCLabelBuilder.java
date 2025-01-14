package componentcraft.label;

import componentcraft.components.AbstractComponent;
import componentcraft.events.componentObserver.ComponentObserver;
import componentcraft.events.componentObserver.ComponentObserverType;
import componentcraft.events.componentObserver.Source;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompCLabelBuilder {

    /**
     * ComponentList of all AbstractComponents which renders in CompCLabel;
     */
    private final List<AbstractComponent> componentList = new ArrayList<>();

    /**
     *
     * @param component is AbstractComponent. Is added to the componentList and rendered in CompCLabel
     * @return CompCLabelBuilder
     */
    public CompCLabelBuilder addComponent(AbstractComponent component) {
        componentList.add(component);
        return this;
    }

    /**
     * Default MouseAdapter which notify every AbstractComponent with the {@code clickEvent(e)}-methode
     */
    private MouseAdapter customMouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            for(AbstractComponent component : componentList){
                component.clickEvent(e);
            }
        }
    };;

    /**
     *
     * @param customMouseAdapter is MuseAdapter. Is used to set own custom MouseAdapter
     * @return CompCLabelBuilder
     */
    public CompCLabelBuilder setCustomMouseAdapter(MouseAdapter customMouseAdapter) {
        this.customMouseAdapter = customMouseAdapter;
        return this;
    }

    /**
     *
     * @param defaultMouseAdapter boolean. Set default MouseAdapter to true;
     * @return CompCLabelBuilder
     */
    public CompCLabelBuilder activateMouseAdapter(boolean defaultMouseAdapter) {
        this.defaultMouseAdapter = defaultMouseAdapter;
        return this;
    }

    /**
     * Default MouseAdapter default false
     */
    private boolean defaultMouseAdapter = false;

    /**
     *
     * @param activateComponentListener is boolean. Set default ComponentObserver to true;
     * @return CompCLabelBuilder
     */
    public CompCLabelBuilder activateComponentListener(boolean activateComponentListener) {
        this.activateComponentListener = activateComponentListener;
        return this;
    }

    /**
     * Default ComponentObserver default false
     */
    private boolean activateComponentListener = false;

    /**
     *
     * @param componentObserver is ComponentObserver. Add Custom ComponentObserver and set activateComponentListener to true.
     * @return
     */
    public CompCLabelBuilder addComponentObserver(ComponentObserver componentObserver) {
        this.activateComponentListener(true);
        this.componentObserver = componentObserver;
        return this;
    }

    /**
     * Default ComponentObserver which implements {@code frame.repaint}, when frame != null
     */
    private ComponentObserver componentObserver =  new ComponentObserver() {
        @Override
        public void update() {}

        private Dimension beforeSize = null;

        @Override
        public void update(ComponentObserverType eventType){
            if(eventType.getEvent() == ComponentObserverType.RESIZE){
                Dimension newDimension = eventType.getSource(Dimension.class);
                if(beforeSize!= null){
                    beforeSize = newDimension;
                }
                if(newDimension != null){
                    componentList.forEach(component -> component.resizeDimensions(beforeSize, newDimension));
                }
            }
           if(updateFrame != null &&  eventType.getEvent()  == ComponentObserverType.REPAINT){
               updateFrame.repaint();
           }
        }
    };

    /**
     * JFrame which used to update the image.
     */
    private JFrame updateFrame = null;

    /**
     *
     * @param jFrame is JFrame. Added so each time when {@code ComponentObserverType.REPAINT} event is sent.
     * @return
     */
    public CompCLabelBuilder addRepaintListener(JFrame jFrame) {
        this.activateComponentListener(true);
        this.updateFrame = jFrame;
        return this;
    }

    /**
     * Default ComponentAdapter which added to an JFrame and sent ComponentObserverType.RESIZE event to every Component
     */
    private  ComponentAdapter componentAdapter = new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            Map<Integer, Object> information =  new HashMap<>();
            Dimension size = resizeFrame.getSize();
            information.put(ComponentObserverType.RESIZE, size);
            componentObserver.update(new ComponentObserverType(new Source("resize_from_label", this.getClass(),information ), ComponentObserverType.RESIZE));
        }
    };

    /**
     * resizeFrame where the ComponentAdapter is added.
     */
    private JFrame resizeFrame = null;


    /**
     * @param componentAdapter is ComponentAdapter.  Is used to set own custom ComponentAdapter. Don't forget to add to JFrame.
     * @return CompCLabelBuilder
     */
    private CompCLabelBuilder setComponentAdapter(ComponentAdapter componentAdapter) {
        this.componentAdapter = componentAdapter;
        return this;
    }

    /**
     * @param jFrame is JFame. Which add Default Listener to the jFrame
     * @return CompCLabelBuilder
     */
    public CompCLabelBuilder addResizeListener(JFrame jFrame) {
       resizeFrame = jFrame;
       jFrame.addComponentListener(componentAdapter);
        return this;
    }

    /**
     * Draw-interface which is called before the component draw
     */
    Draw beforeDraw = null;

    /**
     * @param beforeDraw is Draw. Draw-interface which is called before the component draw. Can be added here.
     * @return CompCLabelBuilder;
     */
    public CompCLabelBuilder addBeforeDrawListener(Draw beforeDraw) {
        this.activateComponentListener(true);
        return this;
    }

    /**
     * Draw-interface which is called after the component draw
     */
    Draw afterDraw = null;

    /**
     *
     * @param afterDraw is Draw. Draw-interface which is called after the component draw. Can be added here.
     * @return CompCLabelBuilder;
     */
    public CompCLabelBuilder addAfterDrawListeners(Draw afterDraw) {
        this.afterDraw = afterDraw;
        return this;
    }


    /**
     * Builds the CompCLabel-class
     * @return CompCLabel
     */
    public CompCLabel build() {
        CompCLabel compCLabel = new CompCLabel();

        componentList.forEach(compCLabel::addComponent);

        if(defaultMouseAdapter) {
            compCLabel.setMouseAdapter(customMouseAdapter);
        }

        if(activateComponentListener) {
            compCLabel.setComponentObserver(componentObserver);
        }
    return compCLabel;
    }
}
