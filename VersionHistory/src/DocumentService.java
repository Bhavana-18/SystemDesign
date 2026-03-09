import javax.print.Doc;
import java.nio.file.AccessDeniedException;
import java.util.*;


public class DocumentService {
    AccessService accessService;
    DocumentService(AccessService accessService){
        this.accessService = accessService;
    }
    private Map<String, Document> documentMap = new HashMap<>();
    private Map<String, List<DocumentVersion>> versionMap = new HashMap<>();

    public Document createDocument(User createdBy, String content, String title){
        Document document = new Document(UUID.randomUUID().toString(), title, createdBy);
        document.setActiveVersion("v1");
        documentMap.put(document.getDocumentId(), document);
        DocumentVersion documentVersion = new DocumentVersion("v1", document,content,createdBy);
        versionMap.put(document.getDocumentId(),List.of(documentVersion));
        accessService.grantAccess(createdBy, document, Set.of(Permissions.READ,Permissions.WRITE,Permissions.ADMIN));
        return document;

    }

    public Document updateDocument(String documentId, User user, String content){

        Document document = documentMap.get(documentId);
        if(!accessService.hasAccess(user,document , Permissions.WRITE)){
           System.out.println("Access has been denied to" + user+ "for" + documentId );
        }
       List<DocumentVersion> list = versionMap.getOrDefault(documentId, new ArrayList<>());
        String version = "v" + list.size();
        list.add(new DocumentVersion(version, document, content,user));
        versionMap.put(documentId, list);
        document.setActiveVersion(version);
        documentMap.put(documentId, document);
        return document;
    }

    public boolean deleteDocument(User user , String documentId){
        Document document = documentMap.get(documentId);
        if(accessService.hasAccess(user,document,Permissions.ADMIN )){
            document.setDeleted(true);
            documentMap.put(documentId, document);
            return  true;
        }
        return false;
    }

    public String goToParticularVersion(User user, String documentId, String version){
        Document document = documentMap.get(documentId);
        if(accessService.hasAccess(user, document, Permissions.READ)) {
            List<DocumentVersion> documentVersions = versionMap.getOrDefault(documentId, new ArrayList<>());
            for (DocumentVersion dv : documentVersions) {
                if (dv.getVersionId().equals(version)) {
                    return dv.getContent();
                }
            }
        }
        return null;
    }

    public void changeVersion(User user, Document doc, String versionId) {
        if (!accessService.hasAccess(user, doc, Permissions.READ)) {
            System.out.println("Access denied: " + user.getUserId() + " cannot view document " + doc.title);
            return;
        }

        List<DocumentVersion> versions = versionMap.get(doc.documentId);
        Optional<DocumentVersion> version = versions.stream()
                .filter(v -> v.versionId.equals(versionId))
                .findFirst();

        if (version.isPresent()) {
            System.out.println("Switched to version " + versionId + " of document " + doc.title);
            doc.setActiveVersion(versionId);
            documentMap.put(doc.getDocumentId(), doc);
        } else {
            System.out.println("Version not found: " + versionId);
        }
    }
}
