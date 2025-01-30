package componentcraft.components.menu.folderview;

import componentcraft.components.AbstractComponent;
import componentcraft.events.componentObserver.ComponentObserverType;
import componentcraft.events.componentObserver.Source;
import componentcraft.util.Coordinates;
import componentcraft.util.DrawStringUtil;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class FolderView extends AbstractComponent {

    private final String url;

    private final Folder folder;


    BufferedImage bufferedImage;


    public FolderView(Coordinates coordinates, String url) {
        super(coordinates, UUID.randomUUID().toString());
        this.url = url;
        folder = Folder.getFolder(url);

        init();
    }

    public FolderView(Coordinates coordinates, Dimension dimension, String url) {
        super(coordinates, dimension, UUID.randomUUID().toString());
        this.url = url;
        folder = Folder.getFolder(url);


        init();
    }


    public void init() {
        File file = new File("src/main/resources/fodlerView/folder.png");
        try {
            bufferedImage = ImageIO.read(file);
        } catch (IOException e) {
            logger.error("error while reading iamge for FolderView", e);
        }
    }

    @Setter
    private Color backgroundColor = Color.LIGHT_GRAY;

    @Setter
    private Color stringColor = Color.BLACK;

    @Setter
    private int spanWith = 10;

    @Setter
    private int yPuffer = 3;

    @Setter
    private int folderPuffer = 2;

    @Setter
    private boolean overflowHidden = true;

    @Override
    public void draw(Graphics2D g2d) {
        super.drawShadow(g2d, getShadow());

        g2d.setColor(backgroundColor);
        g2d.fillRect(getX(), getY(), (int) getDimension().getWidth(), (int) getDimension().getHeight());

        Color beforeColor = g2d.getColor();
        g2d.setColor(stringColor);


        drawFolder(g2d, folder, getX(), getY() + g2d.getFont().getSize(), 10, g2d.getFont().getSize());


        g2d.setColor(beforeColor);

    }

    Map<Rectangle, Folder> clickFoldersList = new HashMap<>();
    Map<Rectangle, componentcraft.components.menu.folderview.File> clickFilesList = new HashMap<>();

    public int drawFolder(Graphics2D g2d, Folder folder, int x, int y, int indent, int lineHeight) {

        g2d.drawImage(bufferedImage, x, y - lineHeight, g2d.getFont().getSize(), g2d.getFont().getSize(), null);

        DrawStringUtil.drawStringWithMaxWidth(g2d, folder.getName(), x + g2d.getFont().getSize() + folderPuffer, y, x + getDimension().width, false);
        clickFoldersList.put(new Rectangle(x, y - lineHeight, getDimension().width, g2d.getFont().getSize()), folder);


        int newY = y + lineHeight + yPuffer;

        if (folder.isOpen()) {
            if (folder.getFiles() != null) {
                for (componentcraft.components.menu.folderview.File file : folder.getFiles()) {
                    DrawStringUtil.drawStringWithMaxWidth(g2d, file.getName(), x + indent, newY, getDimension().width - indent, false);
                    clickFilesList.put(new Rectangle(x + indent, newY - lineHeight, getDimension().width - indent, g2d.getFont().getSize()), file);
                    newY += lineHeight + yPuffer;
                }
            }
            if (folder.getUnderFolder() != null) {
                for (Folder subFolder : folder.getUnderFolder()) {
                    newY = drawFolder(g2d, subFolder, x + indent, newY, indent, lineHeight);
                }
            }


        }
        return newY;
    }


    @Getter
    FolderTriggerObserver folderTriggerObserver = null;

    public void addFolderTriggerObserver(FolderTriggerObserver folderTriggerObserver) {
        this.folderTriggerObserver = folderTriggerObserver;
    }

    public void updateFolderTriggerEvent(FolderTriggerEvent folderTriggerEvent, String path) {
        if(folderTriggerObserver != null){
            folderTriggerObserver.trigger(folderTriggerEvent, path);
        }
    }

    @Override
    public void clickEvent(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        Rectangle rectToggle = new Rectangle(getX(), getY(), getDimension().width, getDimension().height);

        if (rectToggle.contains(mouseX, mouseY)) {


            for (Rectangle rectangle : clickFoldersList.keySet()) {
                if (rectangle.contains(mouseX, mouseY)) {
                    Folder folderClicked = clickFoldersList.get(rectangle);
                    folderClicked.toggleOpen();

                    updateAllObserver(new ComponentObserverType(new Source(getName(), this.getClass(), null), ComponentObserverType.REPAINT));
                    updateAllObserver(new ComponentObserverType(new Source(getName(), this.getClass(), null), ComponentObserverType.FOLDER_VIEW_TOGGLED));
                    updateFolderTriggerEvent(FolderTriggerEvent.FOLDER_SELECT_FOLDER, folderClicked.getPath());

                }
            }

            for (Rectangle rectangle : clickFilesList.keySet()) {
                if (rectangle.contains(mouseX, mouseY)) {
                    componentcraft.components.menu.folderview.File file = clickFilesList.get(rectangle);

                    updateFolderTriggerEvent(FolderTriggerEvent.FOLDER_SELECT_FILE, file.getPath());

                }
            }


            if (getComponentClickListener() != null) {
                getComponentClickListener().onEvent();
            }
        }
    }


}
