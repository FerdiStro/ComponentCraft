package componentcraft.components.menu.folderview;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static componentcraft.events.OnEvent.logger;

@Getter
@Setter
public class Folder {

    private String name;
    private String path;

    private boolean open = true;

    private Folder[] underFolder;
    private List<componentcraft.components.menu.folderview.File> files;


    public void toggleOpen() {
        this.open = !this.open;
    }


    public static Folder getFolder(String url) {
        File[] filesTop = new File(url).listFiles();

        if (filesTop == null || filesTop.length == 0) {
            logger.error("File not found URL: " + url  );
        }

        Folder folderMethode = new Folder();

        folderMethode = getFolder(filesTop);
        folderMethode.setName(url.substring(url.lastIndexOf("/") + 1));
        return folderMethode;
    }

    private static Folder getFolder(File[] files) {
        Folder currentFolder = new Folder();


        if (files == null || files.length == 0) {
            return currentFolder;
        }

        List<componentcraft.components.menu.folderview.File> fileList = new ArrayList<>();
        List<Folder> subFolderList = new ArrayList<>();

        for (File file : files) {
            if (file.isFile()) {
                fileList.add(new componentcraft.components.menu.folderview.File(file.getAbsolutePath(), file.getName()));
            } else if (file.isDirectory()) {
                File[] nestedFiles = file.listFiles();
                Folder nestedFolder = getFolder(nestedFiles);
                nestedFolder.setPath(file.getAbsolutePath());
                nestedFolder.setName(file.getName());
                subFolderList.add(nestedFolder);
            }
        }

        Folder[] subFolders = subFolderList.toArray(new Folder[0]);

        currentFolder.setUnderFolder(subFolders);
        currentFolder.setFiles(fileList);

        return currentFolder;
    }

}

