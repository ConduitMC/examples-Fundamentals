package systems.conduit.fundamentals.commands.tpa;

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
public class TPDeny extends BaseCommand {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("tpdeny").executes(source -> {
            if (!(source.getSource().getEntity() instanceof ServerPlayer)) {
                source.getSource().sendFailure(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("You aren't a player!").withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))));
                return 0;
            }
            Optional<FundamentalsPlugin> plugin = FundamentalsPlugin.getPlugin(FundamentalsPlugin.class);
            if (!plugin.isPresent()) return 0;

            ServerPlayer player = (ServerPlayer) source.getSource().getEntity();

            // Check if the player has an active TP request
            Optional<UUID> requester = plugin.get().getTpManager().hasRequest(player);
            if (!requester.isPresent()) {
                // No request for this player
                source.getSource().sendFailure(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("You have no active requests!").withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))));
                return 0;
            }

            // Has an active request, remove it.
            plugin.get().getTpManager().removeTPATargetting(player);

            // Tell the requesting player that is was denied.
            Conduit.getPlayerManager().getPlayer(requester.get()).ifPresent(p -> p.sendMessage(CommandUtils.BASE_COMPONENT.copy().append("Your TP request has been denied.").withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))));
            source.getSource().sendSuccess(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("Request has been denied!").withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))), false);

            return 1;
        });
    }
}
