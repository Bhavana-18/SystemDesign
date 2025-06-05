package FileSystem;

import java.util.ArrayList;
import java.util.List;

public class FileSystem {
    TrieNode root;
    TrieNode currentDirectory;

    FileSystem(){
        this.root = new TrieNode();
        root.parent = root;
        currentDirectory = root;
    }

    public void mkdir(String filePath){
        root.insert(filePath,false);
    }

    public List<String> ls(String filePath){
        List<String> result = new ArrayList<>();
        TrieNode node = root.search(filePath);
        if(node.isFile){
            result.add(node.name);
            return  result;
        }

        result.addAll(node.children.keySet());
        return  result;
    }

    public String readContentFromFile(String filePath){
        TrieNode node = root.search(filePath);
        if(node != null && node.isFile)
            return node.getContent();
        return "";
    }

    public void addContentToFile(String filePath, String content){
        TrieNode node = root.insert(filePath,true);
        node.content.append(content);
    }

    public void cd(String path){
        TrieNode temp = path.startsWith("/")? root : currentDirectory;
        String[] parts = path.split("/");

        for(String part : parts){
            if(part.isEmpty() || part.equals(".")) continue;
            if(part.equals("..")){
                if(temp.parent != null) temp = temp.parent;
            } else{
              if(!temp.children.containsKey(part))
                  temp.children.put(part , new TrieNode(part, false, temp));
              temp = temp.children.get(part);

            }
        }
        currentDirectory = temp;
    }
}
