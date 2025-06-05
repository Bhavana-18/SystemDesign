public class ExtensionSearchCriteria implements SearchCriteria {

    String extension;
    ExtensionSearchCriteria (String extension){
        this.extension = extension;
    }

    @Override
    public boolean matches(File file){
        return file.getExtension().equals(extension);
    }
}
