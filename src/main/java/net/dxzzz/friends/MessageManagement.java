package net.dxzzz.friends;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageManagement {
    private static final JavaPlugin plugin = JavaPlugin.getPlugin(Friends.class);
    //好友位已满
    public static void noFriendPosition(Player player) {
        player.sendMessage("§3好友 >> §c你的好友位已满，前往好友菜单可购买更多位置。");
    }

    //对方好友位已满
    public static void noFriendPositionOfTarget(Player player) {
        player.sendMessage("§3好友 >> §c对方的好友位已满");
    }

    //已删除好友
    public static void hasDeletedFriend(Player player, String friendName) {
        player.sendMessage("§3好友 >> §7已删除好友 §e" + friendName);
    }

    //已发送好友请求
    public static void sendFriendRequest(Player player, String friendName) {
        player.sendMessage("§3好友 >> §a已向 §6" + friendName + " §a发送好友请求。");
    }

    //你已和对方成为好友
    public static void becomeFriends(Player player, OfflinePlayer target) {
        String friendName = PlaceholderAPI.setPlaceholders(target, "%tag_all%");
        player.sendMessage("§3好友 >> §a你已和 §f" + friendName + " §a成为好友。");
    }

    //已经成为好友
    public static void alreadyFriends(Player player) {
        player.sendMessage("§3好友 >> §c这个玩家已经是你的好友了");
    }

    //输入的参数有误
    public static void invalidArgs(Player player) {
        player.sendMessage("§3好友 >> §c输入的参数有误，请检查后重试。");
    }

    //对方并不是你的好友
    public static void notYourFriend(Player player) {
        player.sendMessage("§3好友 >> §c这个玩家不是你的好友");
    }

    //对方不存在
    public static void targetNotExist(Player player) {
        player.sendMessage("§3好友 >> §c对方不存在，请检查后重试。");
    }
    //对方不存在
    public static void targetNotExist(CommandSender sender) {
        sender.sendMessage("§3好友 >> §c对方不存在，请检查后重试。");
    }

    //不能添加自己为好友
    public static void cannotAddSelf(Player player) {
        player.sendMessage("§3好友 >> §c不能添加自己为好友喵=w=");
    }

    //对方关闭了好友邀请功能
    public static void friendInviteClosed(Player player) {
        player.sendMessage("§3好友 >> §c对方已关闭好友邀请功能");
    }

    //对方关闭了好友私聊功能
    public static void friendChatClosed(Player player) {
        player.sendMessage("§3好友 >> §c对方已关闭好友私聊功能");
    }

    //你已关闭了好友私聊功能
    public static void yourFriendChatClosed(Player player) {
        player.sendMessage("§3好友 >> §c你已关闭了好友私聊功能");
    }

    //你已关闭了好友全局聊天功能
    public static void yourGlobalChatClosed(Player player) {
        player.sendMessage("§3好友 >> §c你已关闭了好友全局聊天功能");
    }

    //已为对方开通好友VIP
    public static void openVip(CommandSender sender) {
        sender.sendMessage("§3好友 >> §a已为对方开通好友VIP，可以添加更多好友。");
    }

    //对方没有给你发送过好友请求
    public static void noRequest(Player player) {
        player.sendMessage("§3好友 >> §c该玩家没有向你发送过好友请求。");
    }

    //没有可回复的玩家
    public static void noReplyPlayer(Player player) {
        player.sendMessage("§3好友 >> §c没有可以回复的玩家。");
    }

    //还需要若干秒后才能再次发送发好友申请
    public static void needWait(Player player,long leftTime) {
        player.sendMessage("§3好友 >> §c你还需要"+leftTime+"秒后才能再次向其发送发好友申请。");
    }

    //发生错误
    public static void error(Player player) {
        player.sendMessage("§3好友 >> §c发生错误，请联系管理员。");
    }

    //对方不在线
    public static void targetOffline(Player player) {
        player.sendMessage("§3好友 >> §c该玩家不在线");
    }

    //对方不在线
    public static void targetOffline(CommandSender sender) {
        sender.sendMessage("§3好友 >> §c该玩家不在线");
    }

    //成功发送私聊消息
    public static void privateMessageSucceed(Player player,String content){
        player.sendMessage("§6[§a姬友§6] §b我 §f-> §7"+PlaceholderAPI.setPlaceholders(player, "%tag_all%")+" §a> §f"+content);
    }

    /*
    通过bc给所有好友发消息

    @param player 发送者
    @param content 消息内容
    @param friendList 好友列表
    @param plugin 插件对象

    @return void
     */
    public static void bc_sendMsgToAllFriends(Player player, String content, List<String> friendList, JavaPlugin plugin) {
        friendList.add(player.getName());
        for (String friend : friendList) {
            String msgPrefix = "§6[§a姬友§6] §6[§a全局§6] §7"+PlaceholderAPI.setPlaceholders(player, "%tag_all%")+" §a> §f";
            String message = msgPrefix + content;
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("Message");
                out.writeUTF(friend);
                out.writeUTF(message);

            } catch (IOException e) {
                e.printStackTrace();
            }
            player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
        }
    }


    /*
    通过bc给好友发送私聊消息

    @param player 发送者
    @param receiver 接收者
    @param content 私聊消息
    @param plugin 插件对象

    @return void
     */
    public static void bc_sendMsgToFriend(Player player, String receiver, String content, JavaPlugin plugin) {
        String msgPrefix = "§6[§a姬友§6] §7"+PlaceholderAPI.setPlaceholders(player, "%tag_all%")+" §f-> §b我 §a> §f";
        String message = msgPrefix + content;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Message");
            out.writeUTF(receiver);
            out.writeUTF(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
    }


    public static void bc_sendFriendRequest(Player player, String target, JavaPlugin plugin) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("FriendChannel_InviteMsg");
            out.writeUTF(player.getName());
            out.writeUTF(target);

        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());

    }



    /*
    通过bc给好友发送添加成功通知

    @param receiver 接收者的名字
    @param target 好友的名字
    @param plugin 插件对象

    @return void
     */
    public static void bc_becomeFriends(String receiver,Player target, JavaPlugin plugin) {
        String message = "§3好友 >> §a你已和§7"+PlaceholderAPI.setPlaceholders(target, "%tag_all%")+"§a成为好友。";
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Message");
            out.writeUTF(receiver);
            out.writeUTF(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
        target.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
    }

    public static void friendList(Player player,PlayerData playerData,int page) {
        TextComponent message = new TextComponent("§e好友列表:\n");
        List<String> onLinePlayerList =new ArrayList<>(Friends.redisPlayerAPI.getOnlinePlayers().keySet());
        int totalPage = (int) Math.ceil(playerData.getFriends().size() / 10.0);
        totalPage = totalPage == 0 ? 1 : totalPage;
        int upperLimit = playerData.isVip() ? 100 : 20;
        List<String> friends = playerData.getFriends();
        List<String> onlineFriends = new ArrayList<>();
        for(String friend : friends){
            if(onLinePlayerList.contains(friend)) {
                onlineFriends.add(friend);
            }
        }
        List<TextComponent> friendInfoList = new ArrayList<>();
        for(String friend : onlineFriends) {
            TextComponent friendInfo=new TextComponent("§a● §f"+PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(friend), "%tag_all%")+" ");
            TextComponent teamOperate = new TextComponent("§a[§a§n组队邀请§a] ");
            teamOperate.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zd yq "+friend));
            teamOperate.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§e点击确认操作").create()));
            TextComponent privateMessageOperate = new TextComponent("§e[§e§n私聊§e] \n");
            privateMessageOperate.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fm "+friend+" "));
            privateMessageOperate.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§e点击确认操作").create()));
            friendInfo.addExtra(teamOperate);
            friendInfo.addExtra(privateMessageOperate);
            friendInfoList.add(friendInfo);
        }
        friends.remove(onlineFriends);
        for(String friend : friends) {
            TextComponent friendInfo=new TextComponent("§7● "+PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(friend), "%tag_all%")+" \n");
            friendInfoList.add(friendInfo);
        }
        int startIndex = (page - 1) * 10;
        int endIndex = Math.min(startIndex + 9, friends.size()-1);
        for(int i=startIndex;i<=endIndex;i++) {
            message.addExtra(friendInfoList.get(i));
        }
        TextComponent tips = new TextComponent("§c当前第"+page+"/"+totalPage+"页,输入/hy lb <页数>来翻页\n");
        message.addExtra(tips);
        TextComponent otherInfo=new TextComponent("§a在线: §a"+onlineFriends.size()+"§e/§7"+upperLimit+" §d上限: "+upperLimit);
        message.addExtra(otherInfo);
        player.spigot().sendMessage(message);
    }
}
