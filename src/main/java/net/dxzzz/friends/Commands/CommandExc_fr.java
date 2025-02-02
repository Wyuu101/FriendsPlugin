package net.dxzzz.friends.Commands;

import net.dxzzz.friends.Friends;
import net.dxzzz.friends.MessageManagement;
import net.dxzzz.friends.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CommandExc_fr implements CommandExecutor {
    private final Friends plugin;
    public CommandExc_fr(Friends plugin) {
        this.plugin = plugin;
    }


    @Override
    public  boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("§c你必须是个玩家才能执行该命令！");
            return true;
        }
        Player player = (Player) sender;
        if(args.length < 1){
            return false;
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                PlayerData playerData = new PlayerData(player.getName());
                if(!playerData.isAvailable()){
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            MessageManagement.error(player);
                        }
                    }.runTask(plugin);
                    return;
                }
                if(playerData.getLastChatWith() == null){
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            MessageManagement.noReplyPlayer(player);
                        }
                    }.runTask(plugin);
                    return;
                }
                List<String> friends = playerData.getFriends();
                if(!friends.contains(playerData.getLastChatWith())) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            MessageManagement.notYourFriend(player);
                        }
                    }.runTask(plugin);
                    return;
                }
                List<String> onLinePlayers =new ArrayList<>(plugin.getRedisApi().getOnlinePlayers().keySet());
                if(!onLinePlayers.contains(playerData.getLastChatWith())){
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            MessageManagement.targetOffline(player);
                        }
                    }.runTask(plugin);
                    return;
                }
                PlayerData targetData = new PlayerData(playerData.getLastChatWith());
                if(!targetData.canReceivePrivateMessageStatus()){
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            MessageManagement.friendChatClosed(player);
                        }
                    }.runTask(plugin);
                    return;
                }
                String content = String.join(" ", args);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        MessageManagement.privateMessageSucceed(player, content);
                        MessageManagement.bc_sendMsgToFriend(player, playerData.getLastChatWith(), content, plugin);
                    }
                }.runTask(plugin);
                targetData.setLastChatWith(player.getName());
                targetData.saveData();
            }
        }.runTaskAsynchronously(plugin);
        return true;

    }
}
