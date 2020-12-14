package systems.conduit.fundamentals.managers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import systems.conduit.fundamentals.CommandUtils;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.mixins.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Innectic
 * @since 12/13/2020
 */
public class TPManager {

    @AllArgsConstructor
    @Getter
    public static class TPAData {
        private UUID target;
        private long millis;
    }

    @Getter private Map<UUID, TPAData> activeRequests = new HashMap<>();

    public Optional<TPAData> getRequest(ServerPlayer player) {
        return Optional.ofNullable(activeRequests.getOrDefault(player.getUUID(), null));
    }

    public void removeTPATargetting(ServerPlayer player) {
        Map<UUID, TPAData> clone = new HashMap<>(activeRequests);

        clone.entrySet().stream().filter(entry -> entry.getValue().getTarget().equals(player.getUUID())).forEach(e -> activeRequests.remove(e.getKey()));
    }

    public void removeTPABy(ServerPlayer player) {
        activeRequests.remove(player.getUUID());
    }

    public void addTPA(ServerPlayer creator, ServerPlayer target) {
        activeRequests.put(creator.getUUID(), new TPAData(target.getUUID(), System.currentTimeMillis()));
    }

    public void expire(ServerPlayer target) {
        TPAData data = activeRequests.getOrDefault(target.getUUID(), null);
        if (data == null) return;

        target.sendMessage(CommandUtils.BASE_COMPONENT.append(new TextComponent("TP request expired!").withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))));
        Conduit.getPlayerManager().getPlayer(data.getTarget()).ifPresent(p -> p.sendMessage(new TextComponent("TP request from " + p.getName() + " expired!").withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))));

        removeTPABy(target);
    }

    public Optional<UUID> hasRequest(ServerPlayer player) {
        return activeRequests.entrySet().stream().filter(entry -> entry.getValue().getTarget().equals(player.getUUID())).map(Map.Entry::getKey).findFirst();
    }
}
