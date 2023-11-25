package enchantsplus.sportalteleport1;

import jdk.internal.net.http.common.Pair;
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
import java.util.stream.Collectors;

public class tpdeny implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player target = (Player) sender;
        List<Pair<UUID, Boolean>> requests;

        for (Map.Entry<UUID, AbstractMap.SimpleEntry<UUID, Boolean>> entry : teleportsCacheData.getPendingTeleportCache().entrySet()) {
            AbstractMap.SimpleEntry<UUID, Boolean> pair = entry.getValue();

            if (pair.getKey().equals(target.getUniqueId())) {
                target.playSound(target, Sound.BLOCK_ANVIL_LAND, SoundCategory.MASTER, 1, 1);

                Player requester = Bukkit.getPlayer(entry.getKey());
                Objects.requireNonNull(requester).playSound(requester, Sound.BLOCK_ANVIL_LAND, SoundCategory.MASTER, 1, 1);

                target.sendMessage(">> Teleport request from " + ChatColor.GOLD + Objects.requireNonNull(requester).getName() + ChatColor.RED + " denied.");
                requester.sendMessage(">> Your teleport request to " + ChatColor.GOLD + target.getName() + ChatColor.RED + " denied.");

                teleportsCacheData.getPendingTeleportCache().remove(entry.getKey());
                return true;
            }

        }

        target.sendMessage(">> "+ChatColor.RED+"You have no pending teleport requests!");
        return false;
    }

}
