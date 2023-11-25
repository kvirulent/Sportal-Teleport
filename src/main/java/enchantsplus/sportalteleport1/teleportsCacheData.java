package enchantsplus.sportalteleport1;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class teleportsCacheData {
    private static final Map<UUID, AbstractMap.SimpleEntry<UUID, Boolean>> pendingTeleports = new HashMap<>();

    public static Map<UUID, AbstractMap.SimpleEntry<UUID, Boolean>> getPendingTeleportCache() {
        return pendingTeleports;
    }

}
