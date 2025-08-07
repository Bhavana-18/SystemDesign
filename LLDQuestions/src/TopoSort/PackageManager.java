package TopoSort;

import java.util.*;

interface Installable {
    void install();
    void uninstall();
}

class Package implements Installable {
    private final String name;
    private final List<Package> dependencies;
    private boolean installed;

    public Package(String name) {
        this.name = name;
        this.dependencies = new ArrayList<>();
        this.installed = false;
    }

    public void addDependency(Package pkg) {
        dependencies.add(pkg);
    }

    public List<Package> getDependencies() {
        return dependencies;
    }

    public String getName() {
        return name;
    }

    public boolean isInstalled() {
        return installed;
    }

    @Override
    public void install() {
        if (!installed) {
            System.out.println("Installing package: " + name);
            installed = true;
        }
    }

    @Override
    public void uninstall() {
        if (installed) {
            System.out.println("Uninstalling package: " + name);
            installed = false;
        }
    }
}

class PackageManager {
    private final Map<String, Package> packageMap = new HashMap<>();
    Map<String, List<String>> adjList = new HashMap<>();

    public Package getOrCreatePackage(String name) {
        return packageMap.computeIfAbsent(name, Package::new);
    }

    public void addDependency(String pkgName, String dependencyName) {
        Package pkg = getOrCreatePackage(pkgName);
        Package dep = getOrCreatePackage(dependencyName);
        pkg.addDependency(dep);
    }

    public void installPackage(String name) {
        Package pkg = packageMap.get(name);
        if (pkg == null) return;
        Set<String> visited = new HashSet<>();
        if (!resolveAndInstall(pkg, visited, new HashSet<>())) {
            System.out.println("Circular dependency detected. Installation aborted.");
        }
    }

    private boolean resolveAndInstall(Package pkg, Set<String> visited, Set<String> recursionStack) {
        if (recursionStack.contains(pkg.getName())) return false;
        if (visited.contains(pkg.getName())) return true;

        recursionStack.add(pkg.getName());
        for (Package dep : pkg.getDependencies()) {
            if (!resolveAndInstall(dep, visited, recursionStack)) return false;
        }
        pkg.install();
        visited.add(pkg.getName());
        recursionStack.remove(pkg.getName());
        return true;
    }

    public void uninstallPackage(String name) {
        Package pkg = packageMap.get(name);
        if (pkg != null && pkg.isInstalled()) {
            Set<String> dependents = findDependents(name);
            if (dependents.isEmpty()) {
                pkg.uninstall();
            } else {
                System.out.println("Cannot uninstall " + name + " as it is required by " + dependents);
            }
        }
    }

    private Set<String> findDependents(String name) {
        Set<String> dependents = new HashSet<>();
        for (Package pkg : packageMap.values()) {
            for (Package dep : pkg.getDependencies()) {
                if (dep.getName().equals(name) && pkg.isInstalled()) {
                    dependents.add(pkg.getName());
                }
            }
        }
        return dependents;
    }

    public void upgradePackage(String name) {
        System.out.println("Upgrading package: " + name);
        uninstallPackage(name);
        installPackage(name);
    }

    public void downgradePackage(String name) {
        System.out.println("Downgrading package: " + name);
        uninstallPackage(name);
        // Add logic here for specific downgrade version if required
        installPackage(name);
    }
}

