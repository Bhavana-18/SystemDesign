import java.util.ArrayList;
import java.util.List;

public class FileSystem {
    Directory root;

    FileSystem(Directory root){
        this.root = root;
    }

    public List<File> search(SearchCriteria criteria){
        List<File> files = new ArrayList<>();
        searchRecursive(root, criteria, files);

        return files;
    }

    public void searchRecursive(Directory dir, SearchCriteria criteria, List<File> files){

        for(File file : dir.getFiles()){
            if(criteria.matches(file))
                files.add(file);
        }

        for(Directory subDir : dir.getDirectoryList()){
            searchRecursive(subDir, criteria, files);
        }

    }
}
