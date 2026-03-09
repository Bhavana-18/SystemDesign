import java.util.*;

public class AccessService {

    private final Map<String, List<DocumentAccess>> accessMap = new HashMap<>();

    public void grantAccess(User user, Document document, Set<Permissions> type){
        List<DocumentAccess> documentAccessList = accessMap.getOrDefault(document.getDocumentId(), new ArrayList<>());
        documentAccessList.add(new DocumentAccess(user,document, type));
        accessMap.put(document.getDocumentId(),documentAccessList);

    }

    public boolean hasAccess(User user, Document document, Permissions type){
        List<DocumentAccess> documentAccessList = accessMap.get(document.getDocumentId());
        if(documentAccessList == null)
            return  false;
        for(DocumentAccess da: documentAccessList){
            if(da.getUser() == user){
                if(da.getPermissions().contains(type))
                    return  true;
            }
        }
        return  false;
    }
}
