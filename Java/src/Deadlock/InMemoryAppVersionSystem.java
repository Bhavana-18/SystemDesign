package Deadlock;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryAppVersionSystem {

    // --- Models ---
    static class AppVersion implements Comparable<AppVersion> {
        final String versionId;    // semantic like "1.0.0"
        final String minOS;        // e.g., "Android 12"
        final String fileKey;      // stored in memory
        final Date uploadedAt;

        public AppVersion(String versionId, String minOS, String fileKey) {
            this.versionId = versionId;
            this.minOS = minOS;
            this.fileKey = fileKey;
            this.uploadedAt = new Date();
        }

        // Simple semantic compare
        private List<Integer> parse() {
            String[] parts = versionId.split("\\.");
            List<Integer> nums = new ArrayList<>();
            for (String p : parts) {
                try { nums.add(Integer.parseInt(p)); } catch (Exception e) { nums.add(0); }
            }
            while (nums.size() < 3) nums.add(0);
            return nums;
        }

        @Override
        public int compareTo(AppVersion o) {
            List<Integer> a = this.parse();
            List<Integer> b = o.parse();
            for (int i = 0; i < 3; i++) {
                int c = Integer.compare(a.get(i), b.get(i));
                if (c != 0) return c;
            }
            return 0;
        }

        @Override
        public String toString() { return versionId; }
    }

    static class Device {
        final String deviceId;
        final String osVersion; // simple string
        String installedVersionId; // null if none

        public Device(String deviceId, String osVersion) {
            this.deviceId = deviceId;
            this.osVersion = osVersion;
            this.installedVersionId = null;
        }

        @Override
        public String toString() {
            return deviceId + " (OS=" + osVersion + ", installed=" + installedVersionId + ")";
        }
    }

    enum RolloutType { FULL, BETA, PERCENTAGE }

    static class RolloutConfig {
        final RolloutType type;
        final Set<String> betaDevices;
        final double percentage; // 0..100

        private RolloutConfig(RolloutType type, Set<String> betaDevices, double percentage) {
            this.type = type;
            this.betaDevices = betaDevices != null ? new HashSet<>(betaDevices) : new HashSet<>();
            this.percentage = percentage;
        }

        static RolloutConfig full() { return new RolloutConfig(RolloutType.FULL, null, 100); }
        static RolloutConfig beta(Set<String> devices) { return new RolloutConfig(RolloutType.BETA, devices, 0); }
        static RolloutConfig percentage(double pct) { return new RolloutConfig(RolloutType.PERCENTAGE, null, pct); }
    }

    // --- In-memory storage / capabilities ---
    static class Storage {
        // fileKey -> content bytes (simulate uploadFile/getFile)
        final Map<String, byte[]> blob = new ConcurrentHashMap<>();

        void uploadFile(String key, byte[] content) {
            blob.put(key, content);
        }

        byte[] getFile(String key) {
            return blob.get(key);
        }

        // installApp: simply mark device installed version
        boolean installApp(Device device, AppVersion version) {
            System.out.println("installApp: Installing " + version.versionId + " on " + device.deviceId);
            device.installedVersionId = version.versionId;
            return true;
        }

        // updateApp: apply diff (stub) and set version
        boolean updateApp(Device device, String targetVersionId) {
            System.out.println("updateApp: Updating " + device.deviceId + " to " + targetVersionId);
            device.installedVersionId = targetVersionId;
            return true;
        }

        // createDiffPack: stub returns a byte[] representing diff pack
        byte[] createDiffPack(AppVersion from, AppVersion to) {
            String diff = "DIFF[" + from.versionId + "->" + to.versionId + "]";
            return diff.getBytes();
        }
    }

    // --- Core system ---
    static class VersionRepo {
        // appName -> sorted set of versions
        final Map<String, NavigableSet<AppVersion>> versions = new ConcurrentHashMap<>();

        void uploadNewVersion(String appName, AppVersion version) {
            versions.computeIfAbsent(appName, k -> new TreeSet<>());
            versions.get(appName).add(version);
        }

        Optional<AppVersion> getLatest(String appName) {
            NavigableSet<AppVersion> set = versions.get(appName);
            if (set == null || set.isEmpty()) return Optional.empty();
            return Optional.of(set.last());
        }

        Optional<AppVersion> getById(String appName, String versionId) {
            return versions.getOrDefault(appName, new TreeSet<>())
                    .stream().filter(v -> v.versionId.equals(versionId)).findFirst();
        }

        List<AppVersion> listAll(String appName) {
            return new ArrayList<>(versions.getOrDefault(appName, new TreeSet<>()));
        }
    }

    static class DiffCache {
        // key: appName + from->to
        final Map<String, byte[]> cache = new ConcurrentHashMap<>();

        String makeKey(String appName, String from, String to) {
            return appName + ":" + from + "->" + to;
        }

        Optional<byte[]> get(String appName, String from, String to) {
            return Optional.ofNullable(cache.get(makeKey(appName, from, to)));
        }

        void put(String appName, String from, String to, byte[] diff) {
            cache.put(makeKey(appName, from, to), diff);
        }
    }

    static class RolloutManager {
        // appName -> versionId -> targeted device IDs
        final Map<String, Map<String, Set<String>>> assignments = new ConcurrentHashMap<>();
        final Random rand = new Random();

        void releaseVersion(String appName, AppVersion version, RolloutConfig cfg, Collection<Device> allDevices) {
            assignments.computeIfAbsent(appName, k -> new HashMap<>());
            Map<String, Set<String>> perVersion = assignments.get(appName);
            Set<String> targets = new HashSet<>();

            if (cfg.type == RolloutType.FULL) {
                for (Device d : allDevices) targets.add(d.deviceId);
            } else if (cfg.type == RolloutType.BETA) {
                targets.addAll(cfg.betaDevices);
            } else if (cfg.type == RolloutType.PERCENTAGE) {
                List<Device> list = new ArrayList<>(allDevices);
                int count = (int) Math.ceil(cfg.percentage / 100.0 * list.size());
                Collections.shuffle(list, rand);
                for (int i = 0; i < Math.min(count, list.size()); i++) {
                    targets.add(list.get(i).deviceId);
                }
            }
            perVersion.put(version.versionId, targets);
            System.out.printf("Released %s of app %s to %s devices: %s%n",
                    version.versionId, appName, targets.size(), targets);
        }

        boolean isTargeted(String appName, String versionId, String deviceId) {
            return assignments.getOrDefault(appName, Map.of())
                    .getOrDefault(versionId, Set.of())
                    .contains(deviceId);
        }
    }

    // Orchestrator combining everything
    static class AppVersionManager {
        final VersionRepo repo = new VersionRepo();
        final DiffCache diffCache = new DiffCache();
        final RolloutManager rollout = new RolloutManager();
        final Storage storage;

        AppVersionManager(Storage storage) {
            this.storage = storage;
        }

        // uploadNewVersion: stores version metadata + binary
        void uploadNewVersion(String appName, AppVersion version, byte[] fileBytes) {
            storage.uploadFile(version.fileKey, fileBytes);
            repo.uploadNewVersion(appName, version);
            System.out.println("uploadNewVersion: " + version.versionId + " for app " + appName);
        }

        // createUpdatePatch: get or build diff between two versions
        byte[] createUpdatePatch(String appName, String fromVersionId, String toVersionId) {
            Optional<byte[]> cached = diffCache.get(appName, fromVersionId, toVersionId);
            if (cached.isPresent()) return cached.get();

            Optional<AppVersion> fromV = repo.getById(appName, fromVersionId);
            Optional<AppVersion> toV = repo.getById(appName, toVersionId);
            if (fromV.isEmpty() || toV.isEmpty())
                throw new IllegalArgumentException("Versions not found for diff");

            byte[] diff = storage.createDiffPack(fromV.get(), toV.get());
            diffCache.put(appName, fromVersionId, toVersionId, diff);
            System.out.printf("createUpdatePatch: diff created %s -> %s%n", fromVersionId, toVersionId);
            return diff;
        }

        // releaseVersion with rollout config
        void releaseVersion(String appName, AppVersion version, RolloutConfig cfg, Collection<Device> devices) {
            rollout.releaseVersion(appName, version, cfg, devices);
        }

        // isAppVersionSupported checks device.osVersion >= version.minOS lexically
        boolean isAppVersionSupported(Device device, AppVersion version) {
            return device.osVersion.compareTo(version.minOS) >= 0;
        }

        // checkForInstall: returns true if fresh install is possible (compatibility + rollout)
        boolean checkForInstall(Device device, String appName) {
            Optional<AppVersion> latest = repo.getLatest(appName);
            if (latest.isEmpty()) return false;
            AppVersion v = latest.get();
            return isAppVersionSupported(device, v) && rollout.isTargeted(appName, v.versionId, device.deviceId);
        }

        // checkForUpdates: returns target versionId if update exists and device is targeted
        Optional<String> checkForUpdates(Device device, String appName) {
            if (device.installedVersionId == null) return Optional.empty();
            Optional<AppVersion> latestOpt = repo.getLatest(appName);
            if (latestOpt.isEmpty()) return Optional.empty();
            AppVersion latest = latestOpt.get();
            if (latest.versionId.equals(device.installedVersionId)) return Optional.empty();
            if (!rollout.isTargeted(appName, latest.versionId, device.deviceId)) return Optional.empty();
            if (!isAppVersionSupported(device, latest)) return Optional.empty();
            return Optional.of(latest.versionId);
        }

        // executeTask: orchestrates install/update
        void executeTask(Device device, String appName) {
            Optional<AppVersion> latestOpt = repo.getLatest(appName);
            if (latestOpt.isEmpty()) {
                System.out.println("No version for app " + appName);
                return;
            }
            AppVersion latest = latestOpt.get();

            if (device.installedVersionId == null) {
                if (!checkForInstall(device, appName)) {
                    System.out.println(device.deviceId + ": no fresh install eligible");
                    return;
                }
                storage.installApp(device, latest);
            } else {
                Optional<String> toUpdate = checkForUpdates(device, appName);
                if (toUpdate.isEmpty()) {
                    System.out.println(device.deviceId + ": no update needed or not eligible");
                    return;
                }
                String targetVersionId = toUpdate.get();
                // create diff (stub) and apply update
                createUpdatePatch(appName, device.installedVersionId, targetVersionId);
                storage.updateApp(device, targetVersionId);
            }
        }
    }

    // --- Demo main ---
    public static void main(String[] args) {
        Storage storage = new Storage();
        AppVersionManager manager = new AppVersionManager(storage);
        String app = "PhonePe";

        // Define versions
        AppVersion v1 = new AppVersion("1.0.0", "Android 11", "file_v1");
        AppVersion v2 = new AppVersion("1.1.0", "Android 11", "file_v2");
        AppVersion v3 = new AppVersion("2.0.0", "Android 12", "file_v3");

        // Upload versions (binary content simulated)
        manager.uploadNewVersion(app, v1, "binary-v1".getBytes());
        manager.uploadNewVersion(app, v2, "binary-v2".getBytes());
        manager.uploadNewVersion(app, v3, "binary-v3".getBytes());

        // Devices
        Device d1 = new Device("device1", "Android 12"); // full support
        Device d2 = new Device("device2", "Android 11"); // cannot support 2.0.0 if minOS is 12
        Device d3 = new Device("device3", "Android 13");

        List<Device> all = List.of(d1, d2, d3);

        // Release v3 to 50% rollout
        manager.releaseVersion(app, v3, RolloutConfig.percentage(50), all);
        System.out.println("\n-- first pass (percentage rollout) --");
        for (Device d : all) manager.executeTask(d, app);

        // Promote to full rollout
        manager.releaseVersion(app, v3, RolloutConfig.full(), all);
        System.out.println("\n-- second pass (full rollout) --");
        for (Device d : all) manager.executeTask(d, app);

        // Show final state
        System.out.println("\nDevice states:");
        for (Device d : all) System.out.println("  " + d);
    }
}