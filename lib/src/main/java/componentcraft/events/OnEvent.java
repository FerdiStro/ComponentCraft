package componentcraft.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public interface OnEvent {
    Logger logger = LogManager.getLogger();

    /**
     * @param eventType {@code OnEventType}. Enum which gives information about the event type
     */
    default void onEvent(OnEventType eventType) {
        logger.debug("onEvent with String not implemented");
    }
    /**
     *  Can be implemented to an Component update. For example as a click listener.
     * */
    void onEvent();
}
