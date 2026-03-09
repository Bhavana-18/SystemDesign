import java.util.Set;

public class DocumentAccess {
    User user;
    Document document;
    Set<Permissions> permissions;

    DocumentAccess(User user, Document document, Set<Permissions> permissions){
        this.user = user;
        this.document = document;
        this.permissions = permissions;
    }

    public Set<Permissions> getPermissions() {
        return permissions;
    }

    public boolean hasPermissions(Permissions permission){
        return permissions.contains(permission);
    }

    public User getUser() {
        return user;
    }


}
