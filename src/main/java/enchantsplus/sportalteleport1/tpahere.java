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

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class tpahere implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        Player requester = (Player) sender;

        if (args.length == 0) {
            requester.sendMessage(">> " + ChatColor.RED+ "You must specify a player to teleport to!");
            return false;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            requester.sendMessage(">> "+ChatColor.RED+"Player \"" + args[0] + "\" was not found!");
            return false;
        }

        if (target == requester) {
            requester.sendMessage(">>"+ChatColor.RED+" You cannot teleport to yourself!");
            return false;
        }

        target.playSound(target, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 0);
        requester.sendMessage("Sent a request to " + ChatColor.GOLD + target.getName() + ChatColor.WHITE + " to teleport to you.");
        requester.sendMessage(">> You can use " + ChatColor.RED + "/tpcancel " + ChatColor.WHITE + " to cancel your request.");
        target.sendMessage(ChatColor.GOLD + requester.getName() + "requested you teleport to them.");
        target.sendMessage(">> You can use " + ChatColor.GREEN + "/tpaccept" + ChatColor.WHITE + " to accept the request.");
        target.sendMessage(">> You can use " + ChatColor.RED + "/tpdeny" + ChatColor.WHITE + " to deny the request.");
        AbstractMap.SimpleEntry<UUID, Boolean> request = new AbstractMap.SimpleEntry<>(target.getUniqueId(), true);
        teleportsCacheData.getPendingTeleportCache().put(requester.getUniqueId(), request);



        return true;
    }
}
