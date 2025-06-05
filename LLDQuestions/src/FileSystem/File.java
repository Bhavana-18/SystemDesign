package FileSystem;

public class File {
    Byte[] content;
    String fileName;
    String extension;
    int size;

    public String getExtension() {
        return extension;
    }

    public Byte[] getContent() {
        return content;
    }

    public int getSize() {
        return size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setContent(Byte[] content) {
        this.content = content;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
