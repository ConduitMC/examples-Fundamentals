package systems.conduit.fundamentals;

import lombok.Getter;
import systems.conduit.fundamentals.commands.FlyCommand;
import systems.conduit.fundamentals.commands.FundamentalsCommand;
import systems.conduit.fundamentals.commands.HealCommand;
import systems.conduit.fundamentals.commands.MoreCommand;
import systems.conduit.fundamentals.commands.tpa.TPACommand;
import systems.conduit.fundamentals.commands.tpa.TPAccept;
import systems.conduit.fundamentals.commands.tpa.TPDeny;
import systems.conduit.fundamentals.managers.TPManager;
import systems.conduit.main.core.events.annotations.Listener;
import systems.conduit.main.core.events.types.PlayerEvents;
import systems.conduit.main.core.plugin.Plugin;
import systems.conduit.main.core.plugin.annotation.PluginMeta;
import systems.conduit.main.core.plugin.config.annotation.ConfigFile;

/**
 * @author Innectic
 * @since 12/13/2020
 */
@PluginMeta(name = "Fundamentals", version = "0.0.1", description = "Fundamental utilities", author = "ConduitMC", reloadable = true)
@ConfigFile(name = "first", type = "json", defaultFile = "first")
public class FundamentalsPlugin extends Plugin {

    @Getter private final TPManager tpManager = new TPManager();

    @Override
    protected void onEnable() {
        registerCommands(new FundamentalsCommand(), new FlyCommand(), new HealCommand(), new TPACommand(), new TPAccept(), new TPDeny(), new MoreCommand());
    }

    @Override
    protected void onDisable() {

    }
}
