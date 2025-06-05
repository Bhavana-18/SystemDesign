import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        Directory root = new Directory("root");

        File file1 = new File("resume.doc", 1200L, new Date(), "doc");
        File file2 = new File("photo.jpg", 4000L, new Date(), "jpg");
        File file3 = new File("notes.txt", 500L, new Date(), "txt");

        root.addFile(file1);
        root.addFile(file2);

        Directory subDir = new Directory("docs");
        subDir.addFile(file3);
        root.addSubDirectory(subDir);

        FileSystem fs = new FileSystem(root);

        SearchCriteria criteria = new ExtensionSearchCriteria("txt");
        List<File> result = fs.search(criteria);

        for (File f : result) {
            System.out.println("Found file: " + f.getName());
        }
    }
}