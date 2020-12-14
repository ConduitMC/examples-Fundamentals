package systems.conduit.fundamentals.managers;

import lombok.RequiredArgsConstructor;
import systems.conduit.fundamentals.FundamentalsPlugin;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.mixins.ServerPlayer;

import java.util.HashMap;
import java.util.Optional;

/**
 * @author Innectic
 * @since 12/13/2020
 */
@RequiredArgsConstructor
public class TPRunnable implements Runnable {

    private final FundamentalsPlugin plugin;
    private static final long REQUEST_LIFETIME = 5 * 60 * 1000; // 5 mins -> millis

    @Override
    public void run() {
        new HashMap<>(plugin.getTpManager().getActiveRequests()).forEach((creator, data) -> {
            long delta = System.currentTimeMillis() - data.getMillis();

            if (delta >= REQUEST_LIFETIME) {
                // This request must expire.
                Optional<ServerPlayer> player = Conduit.getPlayerManager().getPlayer(creator);
                if (!player.isPresent()) {
                    // If the creator of the tp request is no longer offline, just silently delete the request.
                    plugin.getTpManager().getActiveRequests().remove(creator);
                    return;
                }
                // Creator is still online, so we'll send some messages about the expiration.
                plugin.getTpManager().expire(player.get());
            }
        });
    }
}
