import java.util.ArrayList;
import java.util.List;

public class Directory {
    String name;
    List<File> files;
    List<Directory> directoryList;

    Directory(String name){
        this.name = name;
        this.files = new ArrayList<>();
        this.directoryList = new ArrayList<>();
    }

    public List<Directory> getDirectoryList() {
        return directoryList;
    }

    public List<File> getFiles() {
        return files;
    }

    public String getName() {
        return name;
    }

    public void addFile(File file){
        files.add(file);
    }

    public void addSubDirectory(Directory directory){
        directoryList.add(directory);
    }
}
