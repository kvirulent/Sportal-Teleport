package sportal.sportalteleport1;

// Bukkit stuff
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

// Java utils - mostly file handling and data structure types
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.AbstractMap;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Objects;

public class SPortal_Teleport_1 extends JavaPlugin {
    // Prunes requests that have existed for more than the time set in the config
    private static final TimerTask Prune = new TimerTask() {
        @Override
        public void run() {
            for (Map.Entry<UUID,  AbstractMap.SimpleEntry<UUID, Boolean>> request : teleportsCacheData.pendingTeleports.entrySet()) {
                for (Map.Entry<UUID, Instant> time : teleportsCacheData.teleportTimesCache.entrySet()) {
                    if (request.getKey() != time.getKey()) { continue; }
                    if (teleportsCacheData.ConfigData.get("RequestDecayTime") > Instant.now().compareTo(time.getValue())) {
                        teleportsCacheData.pendingTeleports.remove(request.getKey());
                    }
                }
            }
        }
    };

    @Override
    public void onEnable() {
        // Set up the YamlConfiguration object to read from the YML config file
        YamlConfiguration config = new YamlConfiguration();

        File ConfigData = new File(getDataFolder() + File.separator + "config.yml");
        if (!ConfigData.exists()) { // Check if the file presently exists
            boolean success = false;
            try { // If it doesn't, create a new file and define success = true
                success = ConfigData.createNewFile();
            } catch (IOException e) { // Catch IOException thrown by YamlConfiguration.createnewFile();
                Bukkit.getServer().getConsoleSender().sendMessage("! ERROR -> TELEPORT ! : FAILED TO CREATE NEW CONFIG FILE!");
                setEnabled(false); // Disable the plugin and print a warning
            }
            if (!success) { // If the file wasn't found, load defaults
                Bukkit.getServer().getConsoleSender().sendMessage("! ERROR -> TELEPORT ! : FAILED TO FETCH CONFIG DATA. IF THIS IS THE FIRST TIME THE SERVER HAS BEEN LAUNCHED, THE CONFIG FILE HAS BEEN CREATED AT " + getDataFolder() + File.separator + "config.yml. RESTART THE SERVER AFTER CONFIGURING THE PLUGIN.");
                config.set("Cooldown", 0);
                config.set("Delay", 0);
                config.set("MaxPendingRequests", 0);
                config.set("RequestDecayTime", 0);
                try { // Save the defaults to new config file
                    config.save(ConfigData);
                } catch (IOException e) { // Catch IOException thrown by YamlConfiguration.save()
                    Bukkit.getServer().getConsoleSender().sendMessage("! ERROR -> TELEPORT ! : FAILED TO INSERT DEFAULT VALUES INTO THE CONFIG FILE.");
                }
            }
        }

        // Deconstruct the YamlConfiguration to usable data
        config = YamlConfiguration.loadConfiguration(ConfigData);

        // Set the config data to the YamlConfiguration data
        teleportsCacheData.setConfig(config.getInt("Cooldown"), config.getInt("Delay"), config.getInt("MaxPendingRequests"), config.getInt("RequestDecayTime"));

        // Register commands with Bukkit
        Objects.requireNonNull(this.getCommand("tpa")).setExecutor(new tpa());
        Objects.requireNonNull(this.getCommand("tpaccept")).setExecutor(new tpaccept());
        Objects.requireNonNull(this.getCommand("tpdeny")).setExecutor(new tpdeny());
        Objects.requireNonNull(this.getCommand("tpahere")).setExecutor(new tpahere());
        Objects.requireNonNull(this.getCommand("tpcancel")).setExecutor(new tpcancel());

        // Create a recurring task to prune old requests if RequestDecayTime is not 0
        if (teleportsCacheData.ConfigData.get("RequestDecayTime") > 0) {
            Timer time = new Timer();
            time.schedule(Prune, 0, teleportsCacheData.ConfigData.get("RequestDecayTime") * 1000);
        }

    }

    // Defers tasks to be run later
    public void delay(BukkitRunnable task, int time) {
        task.runTaskLater(this, 20L * time);
    }
}
