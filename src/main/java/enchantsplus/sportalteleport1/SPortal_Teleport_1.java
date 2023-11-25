package enchantsplus.sportalteleport1;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SPortal_Teleport_1 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(this.getCommand("tpa")).setExecutor(new tpa());
        Objects.requireNonNull(this.getCommand("tpaccept")).setExecutor(new tpaccept());
        Objects.requireNonNull(this.getCommand("tpdeny")).setExecutor(new tpdeny());
        Objects.requireNonNull(this.getCommand("tpahere")).setExecutor(new tpahere());
        Objects.requireNonNull(this.getCommand("tpcancel")).setExecutor(new tpcancel());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
