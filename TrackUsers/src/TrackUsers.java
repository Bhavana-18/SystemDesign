import javax.sound.midi.Track;
import java.util.*;

public class TrackUsers {

    LinkedHashSet<String> uniqueUsers;
    HashSet<String> duplicates;

    TrackUsers(){
        uniqueUsers = new LinkedHashSet<>();
        duplicates = new HashSet<>();
    }

    public void addUsers(String user){
        if(duplicates.contains(user))
            return;

        if(!uniqueUsers.contains(user)){
            uniqueUsers.add(user);
        } else{
            uniqueUsers.remove(user);
            duplicates.add(user);
        }
    }

    public String getFirstUser(){
        return uniqueUsers.getFirst();
    }

    public List<String> getFirstNUniqueUsers(int n){
        List<String> result = new ArrayList<>();

        Iterator<String> it = uniqueUsers.iterator();

        while(it.hasNext() && result.size() < n){
            result.add(it.next());
        }

        return result;
    }
}
