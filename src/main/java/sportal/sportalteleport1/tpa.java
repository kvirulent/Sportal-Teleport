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

import java.time.Instant;
import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;

public class tpa implements CommandExecutor { // Teleport request command

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        Player requester = (Player) sender;

        if (args.length == 0) { // Check if the command has clean arguments
            requester.sendMessage(">> " + ChatColor.RED+ "You must specify a player to teleport to!");
            return false;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) { // Check if the command has clean arguments
            requester.sendMessage(">> "+ChatColor.RED+"Player \"" + args[0] + "\" was not found!");
            return false;
        }

        if (target == requester) { // Check if the command has clean arguments
            requester.sendMessage(">>"+ChatColor.RED+" You cannot teleport to yourself!");
            return false;
        }

        if (teleportsCacheData.ConfigData.get("MaxPendingRequests") > 0) { // Check if the player hasn't exceeded MaxPendingRequests
            int iterator = 0;
            for (Map.Entry<UUID, AbstractMap.SimpleEntry<UUID, Boolean>> entry : teleportsCacheData.pendingTeleports.entrySet()) {
                if (entry.getKey().equals(requester.getUniqueId())) {
                    iterator += 1;
                }
            }

            if (iterator > teleportsCacheData.ConfigData.get("MaxPendingRequests")) {
                requester.playSound(requester, Sound.BLOCK_ANVIL_LAND, SoundCategory.MASTER, 1, 1);
                requester.sendMessage(">>" + ChatColor.RED + " You have exceeded the maximum allowed of pending requests. Cancel a request to make another.");
                return false;
            }
        }

        for (Map.Entry<UUID, Instant> entry : teleportsCacheData.teleportTimesCache.entrySet()) { // Check if the player's request cooldown has expired
            if (entry.getKey() != requester.getUniqueId()) {
                continue;
            }
            if (Instant.now().compareTo(entry.getValue()) < teleportsCacheData.ConfigData.get("Cooldown")) {
                requester.playSound(requester, Sound.BLOCK_ANVIL_LAND, SoundCategory.MASTER, 1, 1);
                requester.sendMessage(">>" + ChatColor.RED + " You must wait " + teleportsCacheData.ConfigData.get("Cooldown") + " before requesting to teleport again!");
                return false;
            }
        }

        target.playSound(target, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 0);
        requester.sendMessage(">> Sent a request to teleport to " + ChatColor.GOLD + target.getName());
        requester.sendMessage(">> You can use " + ChatColor.RED + "/tpcancel " + ChatColor.WHITE + " to cancel your request.");
        target.sendMessage(">> " + ChatColor.GOLD + requester.getName() + ChatColor.WHITE + " requested to teleport to you.");
        target.sendMessage(">> You can use " + ChatColor.GREEN + "/tpaccept" + ChatColor.WHITE + " to accept the request.");
        target.sendMessage(">> You can use " + ChatColor.RED + "/tpdeny" + ChatColor.WHITE + " to deny the request.");

        AbstractMap.SimpleEntry<UUID, Boolean> request = new AbstractMap.SimpleEntry<>(target.getUniqueId(), false); // Construct a request object
        teleportsCacheData.pendingTeleports.put(requester.getUniqueId(), request); // Insert the request object into the requests cache
        teleportsCacheData.teleportTimesCache.put(requester.getUniqueId(), Instant.now()); // Insert a new value into the TimesCache

        return true;
    }

}
