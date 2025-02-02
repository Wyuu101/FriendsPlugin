package net.dxzzz.friends.Commands;

import net.dxzzz.friends.Friends;
import net.dxzzz.friends.GUI.FriendsGuiManager;
import net.dxzzz.friends.MessageManagement;
import net.dxzzz.friends.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandExc_hyop implements CommandExecutor {
    private final Friends plugin;
    public CommandExc_hyop(Friends plugin) {
        this.plugin = plugin;
    }


    @Override
    public  boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if((sender instanceof ConsoleCommandSender)||((sender instanceof Player)&&sender.isOp())) {
            if(args.length<2){
                return false;
            }
            if(args[0].equalsIgnoreCase("vip")) {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        String args1 = plugin.getDatabaseManager().getUserRealName(args[1]);
                        PlayerData targetData = new PlayerData(args1);
                        if(!targetData.isAvailable()){
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    MessageManagement.targetNotExist(sender);
                                }
                            }.runTask(plugin);
                            return;
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                MessageManagement.openVip(sender);
                            }
                        }.runTask(plugin);
                        targetData.setVip(true);
                        targetData.saveData();
                    }
                }.runTaskAsynchronously(plugin);
            }else if(args[0].equals("gui")){
                Player target = Bukkit.getPlayer(args[1]);
                if(target.isOnline()){
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            FriendsGuiManager guiManager = new FriendsGuiManager(target);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    guiManager.openGui(1);
                                }
                            }.runTask(plugin);
                        }
                    }.runTaskAsynchronously(plugin);
                }
                else {
                    MessageManagement.targetOffline(sender);
                }
            }
        }
        return true;
    }
}
