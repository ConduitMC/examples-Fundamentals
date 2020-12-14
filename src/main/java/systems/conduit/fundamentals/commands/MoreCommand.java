package systems.conduit.fundamentals.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import systems.conduit.fundamentals.CommandUtils;
import systems.conduit.fundamentals.FundamentalsPlugin;
import systems.conduit.main.api.mixins.ServerPlayer;
import systems.conduit.main.core.commands.BaseCommand;

import java.util.Optional;

/**
 * @author Innectic
 * @since 12/13/2020
 */
public class MoreCommand extends BaseCommand {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("more").executes(ctx -> {
            if (!(ctx.getSource().getEntity() instanceof ServerPlayer)) {
                ctx.getSource().sendFailure(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("You aren't a player!").withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))));
                return 0;
            }
            Optional<FundamentalsPlugin> plugin = FundamentalsPlugin.getPlugin();
            if (!plugin.isPresent()) return 0;

            ServerPlayer player = (ServerPlayer) ctx.getSource().getEntity();

            ItemStack item = player.getMainHandItem();
            item.setCount(item.getItem().getMaxStackSize());

            return 1;
        });
    }
}
