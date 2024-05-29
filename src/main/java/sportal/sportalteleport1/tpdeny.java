package sportal.sportalteleport1;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.Map;
import java.util.AbstractMap;
import java.util.Objects;

public class tpdeny implements CommandExecutor { // Deny pending request command

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player target = (Player) sender;

        for (Map.Entry<UUID, AbstractMap.SimpleEntry<UUID, Boolean>> entry : teleportsCacheData.pendingTeleports.entrySet()) {
            AbstractMap.SimpleEntry<UUID, Boolean> pair = entry.getValue();

            if (pair.getKey().equals(target.getUniqueId())) { // Find a request to the sender
                target.playSound(target, Sound.BLOCK_ANVIL_LAND, SoundCategory.MASTER, 1, 1);

                Player requester = Bukkit.getPlayer(entry.getKey());
                Objects.requireNonNull(requester).playSound(requester, Sound.BLOCK_ANVIL_LAND, SoundCategory.MASTER, 1, 1);

                target.sendMessage(">> Teleport request from " + ChatColor.GOLD + Objects.requireNonNull(requester).getName() + ChatColor.RED + " denied.");
                requester.sendMessage(">> Your teleport request to " + ChatColor.GOLD + target.getName() + ChatColor.RED + " denied.");

                teleportsCacheData.pendingTeleports.remove(entry.getKey()); // Remove the request from the request cache
                teleportsCacheData.teleportTimesCache.remove(requester.getUniqueId()); // Remove the request from the times cache
                return true;
            }
        }

        // If we have finished the loop and not returned true yet, then there are no requests to the sender, so warn them as such
        target.sendMessage(">> "+ChatColor.RED+"You have no pending teleport requests!");
        return false;
    }
}
