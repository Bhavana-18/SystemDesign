package FileSystem;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    String name;
    boolean isFile;
    StringBuilder content;

    Map<String, TrieNode> children;
    TrieNode parent;

    TrieNode(){
        this.parent = null;
        this.children = new HashMap<>();
        isFile = false;
        this.content = new StringBuilder();

    }
    TrieNode(String name , boolean isFile, TrieNode parent){
        this.name = name;
        this.isFile = isFile;
        children = new HashMap<>();
        this.parent = parent;
        this.content = new StringBuilder();
    }

    public TrieNode insert(String filePath, boolean isFile){
        TrieNode node = this;

        String[] parts = filePath.split("/");

        for(String part : parts){
            if(!node.children.containsKey(part)){
                node.children.put(part, new TrieNode(part, false, node));
            }

            node = node.children.get(part);
        }

        if(isFile){
            node.isFile = isFile;
            node.name = parts[parts.length -1];
        }
        return node;
    }

    public TrieNode getParent() {
        return parent;
    }

    public Map<String, TrieNode> getChildren() {
        return children;
    }

    public TrieNode search(String filePath){
        TrieNode node = this;
        String[] parts = filePath .split("/");

        for(String part : parts){
            if(!node.children.containsKey(part))
                return null;

            node = node.children.get(part);
        }

        return  node;
    }

    public String getContent(){
        return this.content.toString();
    }
}
