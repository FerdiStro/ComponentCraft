package component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import componentcraft.components.AbstractComponent;
import componentcraft.components.Button;
import componentcraft.components.Shadow;
import componentcraft.components.builder.ComponentBuilder;
import componentcraft.components.menu.MultipleComponentMenuHorizontal;
import componentcraft.label.CompCLabel;
import componentcraft.label.CompCLabelBuilder;
import componentcraft.util.Coordinates;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public CompCLabel returnCompCLabel(JFrame frame) {


        componentcraft.components.Button button = new ComponentBuilder()
                .addCustomConstructor("test1")
                .setCoordinates(new Coordinates(50, 100))
                .setDimension(new Dimension(100, 200))
                .build(componentcraft.components.Button.class);

        componentcraft.components.Button button3 = new ComponentBuilder()
                .addCustomConstructor("test2")
                .setCoordinates(new Coordinates(50, 100))
                .setDimension(new Dimension(100, 200))
                .build(componentcraft.components.Button.class);

        componentcraft.components.Button button2 =  new Button(new Coordinates(200, 100), new Dimension(100, 200), "test2");
        button2.setFancy(true);
        button2.setStateButton();

        button.setShadow(new Shadow(5, 5, 200));
        button.setFancy(true);
        button.setStateButton();


        List<AbstractComponent> componentList = new ArrayList<>();

        componentList.add(button);
        componentList.add(button2);
        componentList.add(button3);

        MultipleComponentMenuHorizontal menuHorizontal = new MultipleComponentMenuHorizontal(
                new Coordinates(0, 0),
                new Dimension( 500, 100),
                componentList
        );

        menuHorizontal.setBackgroundColor(Color.RED);
        menuHorizontal.setShadow(new Shadow(5, 5, 50));


        CompCLabelBuilder compCLabelBuilder = new CompCLabelBuilder()
                .addComponent(menuHorizontal)
//                .addComponent(button2)
                .addResizeListener(frame)
                .addRepaintListener(frame)
                .addToFrame(frame)
                .setBackgroundColor(null)
                .activateMouseAdapter(true);





        return compCLabelBuilder.build();
    }


}

