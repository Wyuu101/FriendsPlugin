package net.dxzzz.friends.GUI;

import net.dxzzz.friends.Friends;
import net.dxzzz.friends.PlayerData;
import net.dxzzz.friends.Utils.CustomItemHead;
import net.dxzzz.friends.Utils.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FriendsGuiManager {
    private Inventory gui1 = null;
    private Inventory gui2 = null;
    private Inventory gui3 = null;
    private Inventory gui4 = null;
    private Inventory gui5 = null;
    private Player player;
    private PlayerData playerData;
    private int upperLimit;
    private List<String> friends;
    private static List<String> onLinePlayers;
    private int numOfOnLineFriends;
    private List<String> onlineFriends;

    public FriendsGuiManager(Player player) {
        this.player = player;
        this.playerData = new PlayerData(player.getName());
        this.friends = playerData.getFriends();
        onLinePlayers =new ArrayList<>(Friends.redisPlayerAPI.getOnlinePlayers().keySet());
        this.upperLimit = playerData.isVip() ? 20 : 100;
        this.onlineFriends = new ArrayList<>();
        for(String friend :friends){
            if(onLinePlayers.contains(friend)){
                onlineFriends.add(friend);
            }
        }
        this.numOfOnLineFriends = onlineFriends.size();
        createGui(player);
    }

    private void createGui(Player player) {
        String guiTitle = "§6Dxz§ezz.§bNet §c>> §4好友";
        gui1 = Bukkit.createInventory(null, 54, guiTitle+" §7(第1页)");
        gui2 = Bukkit.createInventory(null, 54, guiTitle+" §7(第2页)");
        gui3 = Bukkit.createInventory(null, 54, guiTitle+" §7(第3页)");
        gui4 = Bukkit.createInventory(null, 54, guiTitle+" §7(第4页)");
        gui5 = Bukkit.createInventory(null, 54, guiTitle+" §7(第5页)");
        //边界物品
        ItemStack border = new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7, "§7Dxzzz.Net");
        int[] borderIndex = {1, 9, 10, 11, 12, 13, 14, 15, 16,17, 19,28,37,46};
        for (int index = 0; index < borderIndex.length; index++) {
            gui1.setItem(borderIndex[index], border);
            gui2.setItem(borderIndex[index], border);
            gui3.setItem(borderIndex[index], border);
            gui4.setItem(borderIndex[index], border);
            gui5.setItem(borderIndex[index], border);
        }

        //概览
        List<String> overviewLore = new ArrayList<>();
        overviewLore.add("");
        overviewLore.add("§f在线: §a"+numOfOnLineFriends+"§7/§a"+playerData.getNum());
        ItemStack overview = new CustomItemStack(Material.ENCHANTED_BOOK, 1, (short) 0, "§e好友", overviewLore);
        gui1.setItem(0, overview);
        gui2.setItem(0,overview);
        gui3.setItem(0,overview);
        gui4.setItem(0,overview);
        gui5.setItem(0,overview);

        //添加好友按钮
        List<String> inviteButtonLore = new ArrayList<>();
        inviteButtonLore.add("§7点击添加更多好友");
        ItemStack inviteButton = new CustomItemHead("10209", "§a添加更多好友", inviteButtonLore);
        gui1.setItem(2,inviteButton);
        gui2.setItem(2,inviteButton);
        gui3.setItem(2,inviteButton);
        gui4.setItem(2,inviteButton);
        gui5.setItem(2,inviteButton);

        //返回按钮
        ItemStack backButton = new CustomItemHead("9222", "§c✈返回", null);
        gui1.setItem(53,backButton);
        gui2.setItem(53,backButton);
        gui3.setItem(53,backButton);
        gui4.setItem(53,backButton);
        gui5.setItem(53,backButton);

        //好友申请设置按钮
        List<String> switchInviteButtonLore = new ArrayList<>();
        switchInviteButtonLore.add("§7关闭好友申请后将不会收到任何好友申请");
        switchInviteButtonLore.add("§f当前状态: "+(playerData.canReceiveFriendRequestStatus() ? "§a开启" : "§c关闭"));
        ItemStack switchInviteButton = new CustomItemHead("9181", "§a开启/关闭好友申请", switchInviteButtonLore);
        gui1.setItem(18,switchInviteButton);
        gui2.setItem(18,switchInviteButton);
        gui3.setItem(18,switchInviteButton);
        gui4.setItem(18,switchInviteButton);
        gui5.setItem(18,switchInviteButton);

        //好友私聊设置按钮
        List<String> switchPrivateChatButtonLore = new ArrayList<>();
        switchPrivateChatButtonLore.add("§7关闭后将禁用好友私聊功能");
        switchPrivateChatButtonLore.add("§f当前状态: "+(playerData.canReceivePrivateMessageStatus() ? "§a开启" : "§c关闭"));
        ItemStack switchPrivateChatButton = new CustomItemHead("9174", "§a开启/关闭好友私聊", switchPrivateChatButtonLore);
        gui1.setItem(27,switchPrivateChatButton);
        gui2.setItem(27,switchPrivateChatButton);
        gui3.setItem(27,switchPrivateChatButton);
        gui4.setItem(27,switchPrivateChatButton);
        gui5.setItem(27,switchPrivateChatButton);

        //好友全局聊天设置按钮
        List<String> switchGlobalChatButtonLore = new ArrayList<>();
        switchGlobalChatButtonLore.add("§7关闭后将禁用好友全局聊天功能");
        switchGlobalChatButtonLore.add("§f当前状态: "+(playerData.canReceiveGlobalMessageStatus() ? "§a开启" : "§c关闭"));
        ItemStack switchGlobalChatButton = new CustomItemHead("9183", "§a开启/关闭好友全局聊天", switchGlobalChatButtonLore);
        gui1.setItem(36,switchGlobalChatButton);
        gui2.setItem(36,switchGlobalChatButton);
        gui3.setItem(36,switchGlobalChatButton);
        gui4.setItem(36,switchGlobalChatButton);
        gui5.setItem(36,switchGlobalChatButton);

        //好友VIP购买按钮
        List<String> vipButtonLore = new ArrayList<>();
        vipButtonLore.add("§7购买后可将好友位提升至 100位 ！！！");
        vipButtonLore.add("§7妥妥的交际花，恐怖如斯喵！");
        vipButtonLore.add("");
        vipButtonLore.add("§f当前状态: "+(playerData.isVip() ? "§a已拥有" : "§c未拥有"));
        vipButtonLore.add("");
        vipButtonLore.add("§a永久§f/§6150 点券");
        vipButtonLore.add("");
        vipButtonLore.add("§e点击购买");
        ItemStack vipButton = new CustomItemHead("9600", "§d解锁更多好友位", vipButtonLore);
        gui1.setItem(45, vipButton);
        gui2.setItem(45, vipButton);
        gui3.setItem(45, vipButton);
        gui4.setItem(45, vipButton);
        gui5.setItem(45, vipButton);


        //加载好友头颅
        loadPlayerHead(1,gui1);
        loadPlayerHead(2,gui2);
        loadPlayerHead(3,gui3);
        loadPlayerHead(4,gui4);
        loadPlayerHead(5,gui5);
    }

    private void loadPlayerHead(int page,Inventory gui) {
        int startIndex = (page - 1) * 21;
        int endIndex = Math.min(startIndex + 20,friends.size()-1);
        int count =0;
        for (int i = startIndex; i <= endIndex; i++) {
            String friendName = friends.get(i);
            List<String> friendHeadLore = new ArrayList<>();
            friendHeadLore.add((onlineFriends.contains(friendName) ? "§a在线" : "§7离线"));
            friendHeadLore.add("");
            friendHeadLore.add("§e点击查看进行操作");
            ItemStack friendHead = new CustomItemHead(1, "§e" + friendName, friendHeadLore, friendName);
            if (count < 7) {
                gui.setItem(count + 20, friendHead);
            } else if (count < 14) {
                gui.setItem(count - 7 + 29, friendHead);
            } else if (count < 21) {
                gui.setItem(count - 14 + 38, friendHead);
            }
            count++;
        }
        if(friends.size() > page*21) {
            //下一页按钮
            List<String> nextPageButtonLore = new ArrayList<>();
            nextPageButtonLore.add("§7点击前往下一页");
            ItemStack nextPageButton = new CustomItemHead("9223", "§a下一页", nextPageButtonLore);
            gui.setItem(50, nextPageButton);
        }
        if(page>1) {
            //前一页按钮
            List<String> previousPageButtonLore = new ArrayList<>();
            previousPageButtonLore.add("§7点击返回上一页");
            ItemStack previousPageButton = new CustomItemHead("9226", "§a上一页", previousPageButtonLore);
            gui.setItem(48, previousPageButton);
        }
    }

    public void openGui(int page) {
        switch (page) {
            case 1:
                player.openInventory(gui1);
                break;
            case 2:
                player.openInventory(gui2);
                break;
            case 3:
                player.openInventory(gui3);
                break;
            case 4:
                player.openInventory(gui4);
                break;
            case 5:
                player.openInventory(gui5);
                break;
            default:
                break;
        }
    }
}
