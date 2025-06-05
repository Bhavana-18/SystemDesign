public class NameSearchCriteria implements  SearchCriteria {
    String name;

    NameSearchCriteria (String name){
        this.name = name;
    }

    @Override
    public boolean matches(File file){
        return file.getName().equals(name);
    }
}
