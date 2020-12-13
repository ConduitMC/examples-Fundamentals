package systems.conduit.fundamentals;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.mixins.ServerPlayer;

import java.util.Optional;

/**
 * @author Innectic
 * @since 12/13/2020
 */
public class CommandUtils {

    public static Optional<String> tryGet(CommandContext<CommandSourceStack> c, String name) {
        try {
            return Optional.of(StringArgumentType.getString(c, name));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    public static Optional<ServerPlayer> tryGetPlayerArgument(CommandContext<CommandSourceStack> c) {
        Optional<String> player = tryGet(c, "player");
        if (player.isPresent()) return Conduit.getPlayerManager().getPlayer(player.get());
        return Optional.empty();
    }
}
