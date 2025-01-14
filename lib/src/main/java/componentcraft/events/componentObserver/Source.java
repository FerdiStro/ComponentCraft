package componentcraft.events.componentObserver;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;



@AllArgsConstructor
@Getter
public class Source {
    private final String name;
    private final Class<?> aClass;

    private Map<Integer, Object> information;


}
