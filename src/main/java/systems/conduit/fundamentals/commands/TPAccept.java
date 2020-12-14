package systems.conduit.fundamentals.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import systems.conduit.fundamentals.CommandUtils;
import systems.conduit.fundamentals.FundamentalsPlugin;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.mixins.ServerPlayer;
import systems.conduit.main.core.commands.BaseCommand;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Innectic
 * @since 12/13/2020
 */
public class TPAccept extends BaseCommand {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("tpaccept").executes(source -> {
            // Ensure the sender is a player
            if (!(source.getSource().getEntity() instanceof ServerPlayer)) {
                source.getSource().sendFailure(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("You aren't a player!").withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))));
                return 0;
            }
            Optional<FundamentalsPlugin> plugin = FundamentalsPlugin.getPlugin();
            if (!plugin.isPresent()) return 0;

            ServerPlayer player = (ServerPlayer) source.getSource().getEntity();

            // Check if the player has an active TP request
            Optional<UUID> requester = plugin.get().getTpManager().hasRequest(player);
            if (!requester.isPresent()) {
                // No request for this player
                source.getSource().sendFailure(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("You have no active requests!").withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))));
                return 0;
            }

            // Active request, so try to determine the player that sent it and teleport them in.
            Optional<ServerPlayer> requesterPlayer = Conduit.getPlayerManager().getPlayer(requester.get());
            if (!requesterPlayer.isPresent()) {
                // Player that requested is not logged on anymore and somehow the request still existed.
                source.getSource().sendFailure(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("Player is no longer online.").withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))));
                return 0;
            }

            // Player is online, and the request was accepted. Now they can be teleported and the request be deleted.
            requesterPlayer.get().teleport(player);
            plugin.get().getTpManager().removeTPABy(requesterPlayer.get());

            TextComponent component = (TextComponent) CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("Teleportation request has been accepted").withStyle(s -> s.withColor(ChatFormatting.GREEN).withBold(true)));

            source.getSource().sendSuccess(component, false);
            requesterPlayer.get().sendMessage(component);

            return 1;
        });
    }
}
