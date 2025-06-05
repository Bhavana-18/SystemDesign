import java.util.Date;

public class File {

    String name;
    Long size;
    String extension;
    Date createdDate;

    File(String name, Long size, Date createdDate, String extension){
        this.name = name;
        this.size = size;
        this.extension = extension;
        this.createdDate = createdDate;
    }

    public String getName() {
        return name;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Long getSize() {
        return size;
    }

    public String getExtension() {
        return extension;
    }
}
