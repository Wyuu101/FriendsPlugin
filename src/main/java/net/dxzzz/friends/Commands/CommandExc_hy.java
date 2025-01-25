package net.dxzzz.friends.Commands;

import net.dxzzz.friends.Friends;
import net.dxzzz.friends.MessageManagement;
import net.dxzzz.friends.PlayerData;
import net.dxzzz.friends.Utils.UniversalModule;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CommandExc_hy implements CommandExecutor {
    private final JavaPlugin plugin;
    public CommandExc_hy(JavaPlugin plugin) {
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
        if(args[0].equalsIgnoreCase("gui")) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "hyop gui "+player.getName());
        }
        if(args[0].equalsIgnoreCase("tj")) {
            if(args.length < 2){
                MessageManagement.invalidArgs(player);
                return false;
            }
            //TODO: 实现添加好友功能

            new BukkitRunnable() {
                @Override
                public void run() {
                    String args1 = Friends.getDatabaseManager().getUserRealName(args[1]);
                    PlayerData playerData = new PlayerData(player.getName());
                    PlayerData targetData = new PlayerData(args1);
                    if(!playerData.isAvailable()){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                MessageManagement.error(player);
                            }
                        }.runTask(plugin);
                        return;
                    }
                    List<String> playerFriends = playerData.getFriends();
                    if(playerFriends.contains(args1)){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                MessageManagement.alreadyFriends(player);
                            }
                        }.runTask(plugin);
                        return;
                    }
                    if(((!(playerData.isVip()))&&(playerData.getNum() >= 20))||((playerData.isVip())&&(playerData.getNum() >= 100))){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                MessageManagement.noFriendPosition(player);
                            }
                        }.runTask(plugin);
                        return;
                    }
                    if(args1.equalsIgnoreCase(player.getName())){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                MessageManagement.cannotAddSelf(player);
                            }
                        }.runTask(plugin);
                        return;
                    }
                    if(!targetData.isAvailable()){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                MessageManagement.targetNotExist(player);
                            }
                        }.runTask(plugin);
                        return;
                    }
                    if(!playerData.canReceiveFriendRequestStatus()){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                MessageManagement.friendInviteClosed(player);
                            }
                        }.runTask(plugin);
                        return;
                    }
                    if(((!(targetData.isVip()))&&targetData.getNum() >=20)||((targetData.isVip())&&(targetData.getNum() >= 100))){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                MessageManagement.noFriendPositionOfTarget(player);
                            }
                        }.runTask(plugin);
                        return;
                    }
                    long currentTime = System.currentTimeMillis();
                    long inviteTime = playerData.getInviteHistoryTime(args1);
                    long timeDifference = (currentTime - inviteTime)/1000;
                    if(timeDifference<60){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                MessageManagement.needWait(player,  (60 - timeDifference));
                            }
                        }.runTask(plugin);
                        return;
                    }
                    Friends.getDatabaseManager().addOrSetInviteHistory(player.getName(), args1, currentTime);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            MessageManagement.sendFriendRequest(player, args1);
                            MessageManagement.bc_sendFriendRequest(player,args1,plugin);
                        }
                    }.runTask(plugin);
                }
            }.runTaskAsynchronously(plugin);
            return true;
        }
        else if(args[0].equalsIgnoreCase("sc")) {
            if(args.length < 2){
                MessageManagement.invalidArgs(player);
                return false;
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    String args1 = Friends.getDatabaseManager().getUserRealName(args[1]);
                    PlayerData playerData = new PlayerData(player.getName());
                    PlayerData targetData = new PlayerData(args1);
                    if(!playerData.isAvailable()){
                        new BukkitRunnable() {
                            public void run() {
                                MessageManagement.error(player);
                            }
                        }.runTask(plugin);
                        return;
                    }
                    List<String> playerFriends = playerData.getFriends();
                    List<String> targetFriends = targetData.getFriends();
                    if(!(playerFriends.contains(args1))){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                MessageManagement.notYourFriend(player);
                            }
                        }.runTask(plugin);
                        return;
                    }
                    playerFriends.remove(args1);
                    targetFriends.remove(player.getName());
                    playerData.setFriends(playerFriends);
                    targetData.setFriends(targetFriends);
                    playerData.saveData();
                    targetData.saveData();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            MessageManagement.hasDeletedFriend(player, args1);
                        }
                    }.runTask(plugin);
                }
            }.runTaskAsynchronously(plugin);
            return true;
        }
        else if(args[0].equalsIgnoreCase("js")) {
            if(args.length < 2){
                MessageManagement.invalidArgs(player);
                return false;
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    PlayerData playerData = new PlayerData(player.getName());
                    if(!playerData.isAvailable()){
                        new BukkitRunnable() {
                            public void run() {
                                MessageManagement.error(player);
                            }
                        }.runTask(plugin);
                        return;
                    }
                    String args1 = Friends.getDatabaseManager().getUserRealName(args[1]);
                    if(playerData.getFriends().contains(args1)){
                        new BukkitRunnable() {
                            public void run() {
                                MessageManagement.alreadyFriends(player);
                            }
                        }.runTask(plugin);
                        return;
                    }
                    long currentTime = System.currentTimeMillis();
                    PlayerData targetData = new PlayerData(args1);
                    long inviteTime = targetData.getInviteHistoryTime(player.getName());
                    long timeDifference = (currentTime - inviteTime)/1000;
                    if(timeDifference>=60){
                        new BukkitRunnable() {
                            public void run() {
                                MessageManagement.noRequest(player);
                            }
                        }.runTask(plugin);
                        return;
                    }
                    if(!targetData.isAvailable()){
                        new BukkitRunnable() {
                            public void run() {
                                MessageManagement.error(player);
                            }
                        }.runTask(plugin);
                        return;
                    }
                    if(((!(playerData.isVip()))&&(playerData.getNum()>=20))||((playerData.isVip())&&(playerData.getNum() >= 100))){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                MessageManagement.noFriendPosition(player);
                            }
                        }.runTask(plugin);
                        return;
                    }
                    if(((!(targetData.isVip()))&&(targetData.getNum()>=20))||((targetData.isVip())&&(targetData.getNum() >= 100))){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                MessageManagement.noFriendPositionOfTarget(player);
                            }
                        }.runTask(plugin);
                        return;
                    }
                    List<String> friends = playerData.getFriends();
                    List<String> targetFriends = targetData.getFriends();
                    friends.add(args1);
                    targetFriends.add(player.getName());
                    playerData.setFriends(friends);
                    targetData.setFriends(targetFriends);
                    playerData.saveData();
                    targetData.saveData();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            MessageManagement.becomeFriends(player, Bukkit.getOfflinePlayer(args1));
                            MessageManagement.bc_becomeFriends(args1, player,plugin);
                        }
                    }.runTask(plugin);
                }
            }.runTaskAsynchronously(plugin);
            return true;
        }
        else if(args[0].equalsIgnoreCase("lb")) {
            PlayerData playerData = new PlayerData(player.getName());
            if(!playerData.isAvailable()){
                MessageManagement.error(player);
                return true;
            }
            if(args.length == 1){
                MessageManagement.friendList(player,playerData,1);
                return true;
            }
            else {
                if(UniversalModule.canConvertToInt(args[1])){
                    int page = Integer.parseInt(args[1]);
                    if(page<1||(page> (int)(Math.ceil(playerData.getFriends().size() / 10.0)))){
                        MessageManagement.friendList(player,playerData,1);
                        return true;
                    }
                    else{
                        MessageManagement.friendList(player,playerData,page);
                        return true;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
