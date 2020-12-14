package systems.conduit.fundamentals;

import lombok.Getter;
import systems.conduit.fundamentals.commands.*;
import systems.conduit.fundamentals.managers.TPManager;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.plugin.Plugin;
import systems.conduit.main.core.plugin.annotation.PluginMeta;

import java.util.Optional;

/**
 * @author Innectic
 * @since 12/13/2020
 */
@PluginMeta(name = "Fundamentals", version = "0.0.1", description = "Fundamental utilities", author = "ConduitMC")
public class FundamentalsPlugin extends Plugin {

    @Getter private final TPManager tpManager = new TPManager();

    @Override
    protected void onEnable() {
        registerCommands(new FundamentalsCommand(), new FlyCommand(), new HealCommand(), new TPACommand(), new TPAccept());
    }

    @Override
    protected void onDisable() {

    }

    public static Optional<FundamentalsPlugin> getPlugin() {
        return Conduit.getPluginManager().getPlugin("Fundamentals").map(FundamentalsPlugin.class::cast);
    }
}
