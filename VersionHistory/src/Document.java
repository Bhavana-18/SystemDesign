import javax.print.Doc;
import java.util.Date;

public class Document {
    String documentId;
    String title;
    User createdBy;
    Date createdAt;
    boolean isDeleted;
    String activeVersion;
    Document(String documentId , String title, User createdBy){
        this.documentId = documentId;
        this.title = title;
        this.createdBy = createdBy;
        this.createdAt = new Date();
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setActiveVersion(String activeVersion) {
        this.activeVersion = activeVersion;
    }
    public String getActiveVersion(){
        return  activeVersion;
    }
}
