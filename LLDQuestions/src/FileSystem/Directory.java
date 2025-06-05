package FileSystem;

import java.util.ArrayList;
import java.util.List;

public class Directory {
    String directoryName;
    List<File> files;
    List<Directory> subDirectoryList;
    Directory(String directoryName ){
        files = new ArrayList<>();
        subDirectoryList = new ArrayList<>();
    }

    public void addFile(File file){
        files.add(file);
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public void addSubDirectory(Directory directory){
        subDirectoryList.add(directory);
    }
}
