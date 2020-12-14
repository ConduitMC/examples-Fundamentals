package systems.conduit.fundamentals.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import systems.conduit.fundamentals.CommandUtils;
import systems.conduit.fundamentals.FundamentalsPlugin;
import systems.conduit.main.api.mixins.ServerPlayer;
import systems.conduit.main.core.commands.BaseCommand;

import java.util.Optional;

/**
 * @author Innectic
 * @since 12/13/2020
 */
public class TPACommand extends BaseCommand {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("tpa").then(Commands.argument("player", StringArgumentType.word()).executes(source -> {
            if (!(source.getSource().getEntity() instanceof ServerPlayer)) {
                // Sender isn't a player! Can't tpa.
                source.getSource().sendFailure(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("You aren't a player!").withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))));
                return 0;
            }
            ServerPlayer sender = (ServerPlayer) source.getSource().getEntity();

            Optional<ServerPlayer> targetPlayer = CommandUtils.tryGetPlayerArgument(source);
            if (!targetPlayer.isPresent()) {
                // Invalid player
                source.getSource().sendFailure(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("Cannot find targeted player!")).withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true)));
                return 0;
            }

            Optional<FundamentalsPlugin> plugin = FundamentalsPlugin.getPlugin();
            if (!plugin.isPresent()) return 0;

            plugin.get().getTpManager().addTPA(sender, targetPlayer.get());

            source.getSource().sendSuccess(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("Teleportation request has been sent!")
                    .withStyle(s -> s.withColor(ChatFormatting.GREEN).withBold(true))), false);

            targetPlayer.get().sendMessage(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("Teleportation request from: ")
                    .withStyle(s -> s.withColor(ChatFormatting.GREEN).withBold(true)).append(new TextComponent(sender.getName()).withStyle(s -> s.withColor(ChatFormatting.AQUA).withBold(true)))));
            targetPlayer.get().sendMessage(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("Type '/tpaccept' or '/tpdeny'")
                    .withStyle(s -> s.withColor(ChatFormatting.GREEN).withBold(true))));

            return 1;
        }));
    }
}
