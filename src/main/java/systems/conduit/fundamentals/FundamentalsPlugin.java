package systems.conduit.fundamentals;

import systems.conduit.fundamentals.commands.FlyCommand;
import systems.conduit.fundamentals.commands.FundamentalsCommand;
import systems.conduit.main.core.plugin.Plugin;
import systems.conduit.main.core.plugin.annotation.PluginMeta;

/**
 * @author Innectic
 * @since 12/13/2020
 */
@PluginMeta(name = "Fundamentals", version = "0.0.1", description = "Fundamental utilities", author = "ConduitMC")
public class FundamentalsPlugin extends Plugin {

    @Override
    protected void onEnable() {
        registerCommands(new FundamentalsCommand(), new FlyCommand());
    }

    @Override
    protected void onDisable() {

    }
}
