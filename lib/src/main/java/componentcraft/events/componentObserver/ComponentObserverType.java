package componentcraft.events.componentObserver;

import com.google.common.primitives.Primitives;
import lombok.Getter;

import java.util.Map;

public class ComponentObserverType {


    public static  final int BUTTON_STATE_CHANGED = 100;

    public static final int REPAINT = 0;

    public static final int RESIZE = 1;

    @Getter
    private int event;

    @Getter
    private Source source;

    public ComponentObserverType(Source source, int event) {
        this.event = event;
        this.source = source;
    }

    public<T> T getSource(Class<T> t){
        Map<Integer, Object> information = source.getInformation();
        Object o = information.get(event);

        if(o != null){
            try{
                return Primitives.wrap(t).cast(o);
            }catch (Exception e){
                //ignore
            }
        }
        return null;
    }


}



