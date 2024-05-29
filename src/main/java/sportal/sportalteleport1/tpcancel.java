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

public class tpcancel implements CommandExecutor { // Cancel pending request command

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        Player requester = (Player) sender;

        for (Map.Entry<UUID, AbstractMap.SimpleEntry<UUID, Boolean>> entry : teleportsCacheData.pendingTeleports.entrySet()) {

            if (entry.getKey().equals(requester.getUniqueId())) { // Find a teleport request to cancel
                Player target = Bukkit.getPlayer(entry.getValue().getKey());

                requester.playSound(requester, Sound.BLOCK_ANVIL_LAND, SoundCategory.MASTER, 1, 1);
                Objects.requireNonNull(target).playSound(target, Sound.BLOCK_ANVIL_LAND, SoundCategory.MASTER, 1, 1);
                target.sendMessage(">> " + ChatColor.GOLD + requester.getName() + ChatColor.WHITE + " canceled their teleport request.");
                requester.sendMessage(">> Teleport request to " + ChatColor.GOLD + target.getName() + ChatColor.WHITE + " canceled.");

                teleportsCacheData.pendingTeleports.remove(requester.getUniqueId()); // Remove the request from the requests cache
                teleportsCacheData.teleportTimesCache.remove(requester.getUniqueId()); // Remove the request from the times cache
                return true;

            }

        }

        requester.sendMessage(">> "+ChatColor.RED+" You have no pending teleport requests!");
        return false;

    }

}
