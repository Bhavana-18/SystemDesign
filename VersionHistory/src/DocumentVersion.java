import javax.print.Doc;
import java.util.Date;

public class DocumentVersion {
    String versionId;
    Document document;
    String content;
    int versionNumber;
    User updatedBy;
    Date updatedAt;
    DocumentVersion(String versionId, Document document, String content, User updatedBy){
        this.versionId = versionId;
        this.document = document;
        this.content = content;
        this.versionNumber = versionNumber;
        this.updatedBy = updatedBy;
        this.updatedAt = new Date();
    }

    public String getVersionId(){
        return  versionId;
    }

    public Document getDocument() {
        return document;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public String getContent(){
        return  content;
    }
}
