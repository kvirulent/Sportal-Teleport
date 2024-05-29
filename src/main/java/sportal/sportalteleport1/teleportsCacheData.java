package sportal.sportalteleport1;

import java.time.Instant;
import java.util.UUID;
import java.util.Map;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Hashtable;

public class teleportsCacheData {
    public static final Map<UUID, AbstractMap.SimpleEntry<UUID, Boolean>> pendingTeleports = new HashMap<>(); // Stores teleport requests. First UUID is the requester UUID, second UUID is the target UUID, Boolean defines if the request should teleport requester to target or target to requester
    public static final Map<UUID, Instant> teleportTimesCache = new HashMap<>(); // Stores the times associated with teleport requests
    public static Hashtable<String, Integer> ConfigData = new Hashtable<>(); // Stores ConfigData
    public static void setConfig(int cooldown, int delay, int MaxPendingRequests, int RequestDecayTime) { // Setter function for ConfigData
        ConfigData.put("Cooldown", cooldown);
        ConfigData.put("Delay", delay);
        ConfigData.put("MaxPendingRequests", MaxPendingRequests);
        ConfigData.put("RequestDecayTime", RequestDecayTime);
    }
}
