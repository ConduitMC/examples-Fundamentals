package systems.conduit.fundamentals.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
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
public class FundamentalsCommand extends BaseCommand {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("fundamentals")
                .then(Commands.literal("speed")
                .then(Commands.literal("walk").then(Commands.argument("speed", FloatArgumentType.floatArg()).then(Commands.argument("player", StringArgumentType.word()).executes(source -> {
                    Optional<ServerPlayer> target = CommandUtils.tryGetPlayerArgument(source);

                    if (!target.isPresent()) {
                        source.getSource().sendFailure(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("Cannot find targeted player!")
                                .withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))));
                        return 0;
                    }
                    // If there wasn't a provided player, then check if the command sender is a player.
                    if (source.getSource().getEntity() == null || !(source.getSource().getEntity() instanceof Player)) {
                        source.getSource().sendFailure(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("You aren't a player!")
                                .withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))));
                        return 0;
                    }
                    // Command sender is a player
                    target = Optional.of((ServerPlayer) source.getSource().getEntity());

                    // Now, we can set the abilities of the target.
                    target.get().getAbilities().setWalkingSpeed(FloatArgumentType.getFloat(source, "speed"));
                    target.get().sendUpdatedAbilities();
                    source.getSource().sendSuccess(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("Walking speed has been set!").withStyle(s -> s.withBold(true).withColor(ChatFormatting.GREEN))), false);

                    return 1;
                }))))
                .then(Commands.literal("fly").then(Commands.argument("speed", FloatArgumentType.floatArg()).then(Commands.argument("player", StringArgumentType.word()).executes(source -> {
                    Optional<ServerPlayer> target = CommandUtils.tryGetPlayerArgument(source);

                    if (!target.isPresent()) {
                        source.getSource().sendFailure(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("Cannot find targeted player!")
                                .withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))));
                        return 0;
                    }
                    // If there wasn't a provided player, then check if the command sender is a player.
                    if (source.getSource().getEntity() == null || !(source.getSource().getEntity() instanceof Player)) {
                        source.getSource().sendFailure(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("You aren't a player!")
                                .withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))));
                        return 0;
                    }
                    // Command sender is a player
                    target = Optional.of((ServerPlayer) source.getSource().getEntity());

                    // Now, we can set the abilities of the target.
                    target.get().getAbilities().setFlyingSpeed(FloatArgumentType.getFloat(source, "speed"));
                    target.get().sendUpdatedAbilities();
                    source.getSource().sendSuccess(CommandUtils.BASE_COMPONENT.copy().append(new TextComponent("Flying speed has been set!").withStyle(s -> s.withBold(true).withColor(ChatFormatting.GREEN))), false);

                    return 1;
                })))));
    }
}
