package TopoSort;

import java.util.*;

public class PackageBuilder {

    // Sample graph: key is package, value is list of dependencies
    private static final Map<String, List<String>> graph = new HashMap<>();

    // Reversed graph: dependency -> list of packages that depend on it
    private static final Map<String, List<String>> reverseGraph = new HashMap<>();

    public static List<String> getPackageBuildOrder(String targetPkg) {
        Set<String> visited = new HashSet<>();
        Set<String> relevantPackages = new HashSet<>();

        // Step 1: Get all dependencies of target package (DFS)
        collectDependencies(targetPkg, visited, relevantPackages);

        // Step 2: Compute indegree map for subgraph
        Map<String, Integer> indegree = new HashMap<>();
        for (String pkg : relevantPackages) {
            for (String dep : graph.getOrDefault(pkg, new ArrayList<>())) {
                reverseGraph.computeIfAbsent(dep, k -> new ArrayList<>()).add(pkg);
                indegree.put(pkg, indegree.getOrDefault(pkg, 0) + 1);
            }
        }

        // Step 3: Kahn's algorithm
        Queue<String> queue = new LinkedList<>();
        for (String pkg : relevantPackages) {
            if (indegree.getOrDefault(pkg, 0) == 0) {
                queue.offer(pkg);
            }
        }

        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);
            for (String dependent : reverseGraph.getOrDefault(current, new ArrayList<>())) {
                indegree.put(dependent, indegree.get(dependent) - 1);
                if (indegree.get(dependent) == 0) {
                    queue.offer(dependent);
                }
            }
        }

        return result;
    }

    private static void collectDependencies(String pkg, Set<String> visited, Set<String> result) {
        if (!visited.add(pkg)) return;
        for (String dep : graph.getOrDefault(pkg, new ArrayList<>())) {
            collectDependencies(dep, visited, result);
        }
        result.add(pkg);
    }

    public static void main(String[] args) {
        // Example
        graph.put("A", Arrays.asList("B", "C"));
        graph.put("B", Arrays.asList("D"));
        graph.put("C", Arrays.asList("D", "E"));
        graph.put("D", Arrays.asList());
        graph.put("E", Arrays.asList());

        List<String> buildOrder = getPackageBuildOrder("A");
        System.out.println("Build Order: " + buildOrder);
    }
}
