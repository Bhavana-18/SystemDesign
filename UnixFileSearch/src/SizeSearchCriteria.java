public class SizeSearchCriteria implements  SearchCriteria {

    Long minSize;

    public SizeSearchCriteria(Long minSize){
        this.minSize = minSize;
    }

    @Override
    public boolean matches(File file){
        return file.getSize()>= minSize;
    }
}
