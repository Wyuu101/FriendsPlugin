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
import java.util.Arrays;
import java.util.List;

public class CommandExc_fm implements CommandExecutor {
    private final Friends plugin;
    public CommandExc_fm(Friends plugin) {
        this.plugin = plugin;
    }


    @Override
    public  boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("§c你必须是个玩家才能执行该命令！");
            return true;
        }
        Player player = (Player) sender;
        if(args.length < 2){
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
                if(!playerData.canReceivePrivateMessageStatus()){
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            MessageManagement.yourFriendChatClosed(player);
                        }
                    }.runTask(plugin);
                    return;
                }
                List<String> friends = playerData.getFriends();
                String args0 = plugin.getDatabaseManager().getUserRealName(args[0]);
                if(!friends.contains(args0)){
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            MessageManagement.notYourFriend(player);
                        }
                    }.runTask(plugin);
                    return;
                }
                List<String> onLinePlayers =new ArrayList<>(plugin.getRedisApi().getOnlinePlayers().keySet());
                if(!onLinePlayers.contains(args0)){
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            MessageManagement.targetOffline(player);
                        }
                    }.runTask(plugin);
                    return;
                }
                String content = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                PlayerData targetData = new PlayerData(args0);
                if(!targetData.canReceivePrivateMessageStatus()){
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            MessageManagement.friendChatClosed(player);
                        }
                    }.runTask(plugin);
                    return;
                }
                targetData.setLastChatWith(player.getName());
                targetData.saveData();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        MessageManagement.privateMessageSucceed(player, args0,content);
                        MessageManagement.bc_sendMsgToFriend(player, args0, content, plugin);
                    }
                }.runTask(plugin);
            }
        }.runTaskAsynchronously(plugin);
        return true;
    }
}
