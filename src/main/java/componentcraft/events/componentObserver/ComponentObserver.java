package componentcraft.events.componentObserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface ComponentObserver {

    /**
     *  This methode triggers when component updates
     * {@return void}
     */
    void update();

    Logger logger = LogManager.getLogger();

    /**
     *  @param eventType  {@code ComponentObserverType}.  Enum which gives information about the event type.
     *  This methode triggers when component updates and give specifically the update type
     */
    default void update(ComponentObserverType eventType){
        logger.debug("onEvent with String not implemented");

    }
}