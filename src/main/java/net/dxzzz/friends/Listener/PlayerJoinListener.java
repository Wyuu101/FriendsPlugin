package net.dxzzz.friends.Listener;

import net.dxzzz.friends.Database.DatabaseManager;
import net.dxzzz.friends.Friends;
import net.dxzzz.friends.Utils.UniversalModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerJoinListener implements Listener {
    private final Friends plugin = Friends.getInstance();
    private final DatabaseManager databaseManager = plugin.getDatabaseManager();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.asLobby) {
            Player player = event.getPlayer();
            List<String> friends = new ArrayList<>();
            databaseManager.createPlayerData(player.getName(), UniversalModule.listToString(friends), 0, 0);
        }
    }
}
