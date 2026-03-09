package Service;

import Entity.Group;

import java.util.*;

public class GroupService {
    private final Map<String, Group> groups = new HashMap<>();

    public void createGroup(String groupId, String name, List<String> memberIds) {
        if (groupId == null || groupId.isBlank()) throw new IllegalArgumentException("invalid groupId");
        if (groups.containsKey(groupId)) throw new IllegalArgumentException("group exists: " + groupId);
        groups.put(groupId, new Group(groupId, name, memberIds));
    }

    public void addMember(String groupId, String userId) {
        group(groupId).addMember(userId);
    }

    public Group group(String groupId) {
        Group g = groups.get(groupId);
        if (g == null) throw new IllegalArgumentException("unknown group: " + groupId);
        return g;
    }
}