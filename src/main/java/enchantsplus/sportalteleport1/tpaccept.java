package enchantsplus.sportalteleport1;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class tpaccept implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player target = (Player) sender;


        for (Map.Entry<UUID, AbstractMap.SimpleEntry<UUID, Boolean>> entry : teleportsCacheData.getPendingTeleportCache().entrySet()) {
            AbstractMap.SimpleEntry<UUID, Boolean> pair = entry.getValue();

            if (pair.getKey().equals(target.getUniqueId())) {

                if (pair.getValue().equals(false)) {
                    Player requester = Bukkit.getPlayer(entry.getKey());

                    target.sendMessage(">> Teleport request " + ChatColor.GREEN + "accepted.");
                    Objects.requireNonNull(requester).sendMessage(">> Your teleport request was " + ChatColor.GREEN + "accepted.");

                    requester.playSound(requester, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 0);
                    requester.teleport(target.getLocation());
                    teleportsCacheData.getPendingTeleportCache().remove(entry.getKey());
                    return true;
                } else {
                    Player requester = Bukkit.getPlayer(entry.getKey());

                    target.sendMessage(">> Teleport request " + ChatColor.GREEN + "accepted.");
                    Objects.requireNonNull(requester).sendMessage(">> Your teleport request was " + ChatColor.GREEN + "accepted.");

                    requester.playSound(requester, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 0);
                    target.teleport(requester.getLocation());
                    teleportsCacheData.getPendingTeleportCache().remove(entry.getKey());
                }

            }

        }

        target.sendMessage(">> "+ChatColor.RED+" You have no pending teleport requests!");
        return false;

    }

}
