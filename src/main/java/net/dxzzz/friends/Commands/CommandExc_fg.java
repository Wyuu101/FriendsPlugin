package net.dxzzz.friends.Commands;

import net.dxzzz.friends.MessageManagement;
import net.dxzzz.friends.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;

public class CommandExc_fg implements CommandExecutor {
    private final JavaPlugin plugin;
    public CommandExc_fg(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public  boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("§c你必须是个玩家才能执行该命令！");
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 0){
            return false;
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                PlayerData playerData = new PlayerData(player.getName());
                if(!playerData.isAvailable()){
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            MessageManagement.error(player);
                        }
                    }.runTask(plugin);
                    return;
                }
                if(!playerData.canReceiveGlobalMessageStatus()){
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            MessageManagement.yourGlobalChatClosed(player);
                        }
                    }.runTask(plugin);
                    return;
                }
                String content = String.join(" ", args);
                List<String> friends = playerData.getFriends();
                Iterator<String> iterator = friends.iterator();
                while (iterator.hasNext()) {
                    String friend = iterator.next();
                    PlayerData friendData = new PlayerData(friend);
                    if (!friendData.canReceiveGlobalMessageStatus()) {
                        iterator.remove();
                    }
                    //此时friends列表已经是可以接收消息的好友列表
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        MessageManagement.bc_sendMsgToAllFriends(player,content,friends,plugin);
                    }
                }.runTask(plugin);
            }
        }.runTaskAsynchronously(plugin);

        return true;
    }
}
