package componentcraft.components.builder;

import componentcraft.components.AbstractComponent;
import componentcraft.util.Coordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ComponentBuilder{


    private static final Logger log = LogManager.getLogger(ComponentBuilder.class);
    private Coordinates coordinates;
    private Dimension dimension;

    private  List<Object> customConstructor =  new ArrayList();
    private boolean onlyUseCustomConstructor;


    public ComponentBuilder addCustomConstructor(Object customConstructor) {
        this.customConstructor.add(customConstructor);
        return this;
    }

    public ComponentBuilder setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }
    public ComponentBuilder setDimension(Dimension dimension) {
        this.dimension = dimension;
        return this;
    }

    public <T extends AbstractComponent> T build (Class<T> tClass) {
        Constructor<?>[] constructors = tClass.getConstructors();

        Object[] constructorArgs = new Object[ComponentBuilder.class.getMethods().length];
        for (int i = 0; i < constructors.length; i++) {
            constructorArgs[i] = coordinates;
        }

        Object[] parameterAsArray = getConstructorArray();

        T t = null;
        for(Constructor<?> constructor : constructors) {
            try {
                t = (T) constructor.newInstance(parameterAsArray);
            } catch (Exception e) {}
        }
        if(t == null){
            log.error(tClass.getName() + " can't any constructor which matches variables");
            return null;
        }
        return t;
    }

    private Object[] getConstructorArray() {
        Field[] fields = this.getClass().getDeclaredFields();
        Object[] parameterArray = new Object[fields.length - 2];

        for (int i = 1; i < fields.length - 2; i++) {
            fields[i].setAccessible(true);
            try {
                parameterArray[i-1] = fields[i].get(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if(!customConstructor.isEmpty()){
            int cC = 0;
            for(int i = parameterArray.length - customConstructor.size() ; i != parameterArray.length; i++){
                parameterArray[i] = customConstructor.get(cC);
                cC++;
            }
        }

        return parameterArray;
    }


}
