package systems.conduit.fundamentals.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import systems.conduit.fundamentals.CommandUtils;
import systems.conduit.main.api.mixins.Player;
import systems.conduit.main.api.mixins.ServerPlayer;
import systems.conduit.main.core.commands.BaseCommand;

import java.util.Optional;

/**
 * @author Innectic
 * @since 12/13/2020
 */
public class HealCommand extends BaseCommand {

    private int heal(CommandContext<CommandSourceStack> source, Optional<String> player) {
        ServerPlayer target = null;

        if (player.isPresent()) {
            // If there was a provided name, try to lookup the player.
            Optional<ServerPlayer> p = CommandUtils.tryGetPlayerArgument(source);
            if (!p.isPresent()) {
                // Invalid player
                source.getSource().sendFailure(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("Cannot find targeted player!")).withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true)));
                return 0;
            }
            // Target was found!
            target = p.get();
        } else {
            // If there wasn't a provided player, see if the command sender is a player.
            if (source.getSource().getEntity() == null || !(source.getSource().getEntity() instanceof Player)) {
                source.getSource().sendFailure(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("You aren't a player!")
                        .withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))));
                return 0;
            }
            target = (ServerPlayer) source.getSource().getEntity();
        }

        // Definitely have a target now. Heal em!
        target.setHealth(target.getMaxHealth());
        target.setAirSupply(target.getMaxAirSupply());
        target.getFoodData().setFoodLevel(20);

        String prefix;
        String suffix;

        if (player.isPresent()) {
            prefix =  player.get() + " ";
            suffix = "has";
        } else {
            prefix = "You ";
            suffix = "have";
        }

        TextComponent prefixComponent = (TextComponent) new TextComponent(prefix).withStyle(s -> s.withColor(ChatFormatting.AQUA).withBold(true));

        source.getSource().sendSuccess(
                CommandUtils.BASE_COMPONENT.copy().append(prefixComponent)
                        .append(new TextComponent(suffix + " been healed!").withStyle(s -> s.withColor(ChatFormatting.GREEN).withBold(true))), false);

        return 1;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("heal")
                .executes(s -> heal(s, Optional.empty()))
                .then(Commands.argument("player", StringArgumentType.word())
                        .executes(s -> heal(s, CommandUtils.tryGet(s, "player"))));
    }
}
