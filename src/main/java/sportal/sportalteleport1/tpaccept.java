package sportal.sportalteleport1;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.AbstractMap;
import java.util.Objects;

public class tpaccept implements CommandExecutor { // Accept request command
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player target = (Player) sender;

        for (Map.Entry<UUID, AbstractMap.SimpleEntry<UUID, Boolean>> entry : teleportsCacheData.pendingTeleports.entrySet()) {
            AbstractMap.SimpleEntry<UUID, Boolean> pair = entry.getValue();

            if (pair.getKey().equals(target.getUniqueId())) { // Find a request that the sender owns
                Player requester = Bukkit.getPlayer(entry.getKey());
                BukkitRunnable task = new BukkitRunnable() {
                    @Override
                    public void run() { // Define a task to be deferred by Cooldown later
                        if (pair.getValue().equals(false)) {
                            assert requester != null;
                            requester.teleport(target.getLocation());
                            teleportsCacheData.pendingTeleports.remove(entry.getKey());
                        } else {
                            assert requester != null;
                            target.teleport(requester.getLocation());
                            teleportsCacheData.pendingTeleports.remove(entry.getKey());
                        }
                    }
                };

                target.sendMessage(">> Teleport request " + ChatColor.GREEN + "accepted.");
                Objects.requireNonNull(requester).sendMessage(">> Your teleport request was " + ChatColor.GREEN + "accepted.");

                requester.playSound(requester, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 0);
                target.playSound(target, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 0);

                if (teleportsCacheData.ConfigData.get("Delay") > 0) { // Check if the delay config is set
                    requester.sendMessage(">> Teleporting in " + teleportsCacheData.ConfigData.get("Delay") + " seconds.");
                    Objects.requireNonNull(target).sendMessage(">> Teleporting in " + teleportsCacheData.ConfigData.get("Delay") + " seconds");
                    SPortal_Teleport_1.getPlugin(SPortal_Teleport_1.class).delay(task, teleportsCacheData.ConfigData.get("Delay")); // Defer the teleport task to run in the config's defined delay
                } else { // Do not defer if the config is not set
                    requester.sendMessage(">> Teleporting in " + teleportsCacheData.ConfigData.get("Delay") + " seconds.");
                    Objects.requireNonNull(target).sendMessage(">> Teleporting in " + teleportsCacheData.ConfigData.get("Delay") + " seconds");
                    task.run();
                }
                return true;
            }
        }

        // If we have finished the loop and not returned true yet, then there are no requests owned by the sender, so warn them as such
        target.sendMessage(">> " + ChatColor.RED + " You have no pending teleport requests!");
        return false;
    }
}
