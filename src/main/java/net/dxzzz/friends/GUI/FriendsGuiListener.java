package net.dxzzz.friends.GUI;
import de.rapha149.signgui.SignGUI;
import net.dxzzz.friends.Friends;
import net.dxzzz.friends.PlayerData;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.upperlevel.spigot.book.BookUtil;
import java.util.Collections;

public class FriendsGuiListener implements Listener {
    private final Friends plugin;
    public FriendsGuiListener(Friends plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if ((slot >= 20 && slot <= 26)||(slot >= 29 && slot <= 35)||(slot >= 38 && slot <= 44)) {
                ItemStack item = event.getInventory().getItem(slot);
                if (item != null && item.getType() == Material.SKULL_ITEM) {
                    //TODO: 打开书本GUI
                    String targetName =item.getItemMeta().getDisplayName().substring(2);
                    ItemStack book = BookUtil.writtenBook()
                            .author("Admin")
                            .title("好友管理")
                            .pages(
                                    new BookUtil.PageBuilder()
                                            .add(new TextComponent("§c§l好友管理"))
                                            .newLine()
                                            .add(new TextComponent("§7-=-=-=-=-=-=-=-"))
                                            .newLine()
                                            .add(new TextComponent("请选择要对"+targetName+"进行的操作"))
                                            .newLine()
                                            .add(
                                                    BookUtil.TextBuilder.of("§6§n发送私聊")
                                                            .onClick(BookUtil.ClickAction.suggestCommand("/fm "+targetName+" "))
                                                            .onHover(BookUtil.HoverAction.showText("§e点击确认操作"))
                                                            .build()
                                            )
                                            .newLine()
                                            .newLine()
                                            .add(
                                                    BookUtil.TextBuilder.of("§4§n删除好友")
                                                            .onClick(BookUtil.ClickAction.runCommand("/hy sc "+targetName))
                                                            .onHover(BookUtil.HoverAction.showText("§e点击确认操作"))
                                                            .build()
                                            )
                                            .newLine()
                                            .newLine()
                                            .add(
                                                    BookUtil.TextBuilder.of("§a§n组队邀请")
                                                            .onClick(BookUtil.ClickAction.runCommand("/zd yq "+targetName))
                                                            .onHover(BookUtil.HoverAction.showText("§e点击确认操作"))
                                                            .build()
                                            )
                                            .newLine()
                                            .newLine()
                                            .add(
                                                    BookUtil.TextBuilder.of("§7[§3§n返回上一级菜单§7]")
                                                            .onClick(BookUtil.ClickAction.runCommand("/hy gui"))
                                                            .onHover(BookUtil.HoverAction.showText("§e点击确认操作"))
                                                            .build()
                                            )
                                            .build()
                            )
                            .build();

                    BookUtil.openPlayer(player, book);
                }
            }

            switch (slot) {
                case 2:
                    try {
                        SignGUI gui = SignGUI.builder()
                                .setLines(null, "请在第一行输入", "要添加的玩家名")
                                .setHandler((p, result) -> {
                                    String name = plugin.getDatabaseManager().getUserRealName(result.getLine(0));
                                    Bukkit.getScheduler().runTask(plugin, () -> {
                                        player.performCommand("hy tj " + name);
                                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                                    });
                                    return Collections.emptyList();
                                })
                                .build();
                        gui.open(player);
                    }
                    catch (Exception e) {
                        return;
                    }
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    break;
                case 18:
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            PlayerData playerData = new PlayerData(player.getName());
                            playerData.switchCanReceiveFriendRequestStatus();
                            playerData.saveData();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                                }
                            }.runTask(plugin);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    FriendsGuiManager friendsGuiManager = new FriendsGuiManager(player);
                                    if(event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第1页)")){
                                        friendsGuiManager.openGui(1);
                                    }else if(event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第2页)")){
                                        friendsGuiManager.openGui(2);
                                    }else if(event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第3页)")){
                                        friendsGuiManager.openGui(3);
                                    }else if(event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第4页)")){
                                        friendsGuiManager.openGui(4);
                                    }else if(event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第5页)")) {
                                        friendsGuiManager.openGui(5);
                                    }
                                }
                            }.runTaskLater(plugin,5);
                        }
                    }.runTaskAsynchronously(plugin);
                    break;
                case 27:
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            PlayerData playerData = new PlayerData(player.getName());
                            playerData.switchCanReceivePrivateMessageStatus();
                            playerData.saveData();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                                }
                            }.runTask(plugin);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    FriendsGuiManager friendsGuiManager = new FriendsGuiManager(player);
                                    if (event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第1页)")) {
                                        friendsGuiManager.openGui(1);
                                    } else if (event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第2页)")) {
                                        friendsGuiManager.openGui(2);
                                    } else if (event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第3页)")) {
                                        friendsGuiManager.openGui(3);
                                    } else if (event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第4页)")) {
                                        friendsGuiManager.openGui(4);
                                    } else if (event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第5页)")) {
                                        friendsGuiManager.openGui(5);
                                    }
                                }
                            }.runTaskLater(plugin, 5);
                        }
                    }.runTaskAsynchronously(plugin);
                    break;
                case 36:
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            PlayerData playerData = new PlayerData(player.getName());
                            playerData.switchCanReceiveGlobalMessageStatus();
                            playerData.saveData();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                                }
                            }.runTask(plugin);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    FriendsGuiManager friendsGuiManager = new FriendsGuiManager(player);
                                    if (event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第1页)")) {
                                        friendsGuiManager.openGui(1);
                                    } else if (event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第2页)")) {
                                        friendsGuiManager.openGui(2);
                                    } else if (event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第3页)")) {
                                        friendsGuiManager.openGui(3);
                                    } else if (event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第4页)")) {
                                        friendsGuiManager.openGui(4);
                                    } else if (event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第5页)")) {
                                        friendsGuiManager.openGui(5);
                                    }
                                }
                            }.runTaskLater(plugin, 5);
                        }
                    }.runTaskAsynchronously(plugin);
                    break;
                case 45:
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "dgui openfor "+player.getName()+" tradeconfirm-friends-vip.yml");
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    break;
                case 48:
                    FriendsGuiManager friendsGuiManager = new FriendsGuiManager(player);
                    if(event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第2页)")){
                        friendsGuiManager.openGui(1);
                    }else if(event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第3页)")){
                        friendsGuiManager.openGui(2);
                    }else if(event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第4页)")){
                        friendsGuiManager.openGui(3);
                    }else if(event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第5页)")) {
                        friendsGuiManager.openGui(4);
                    }
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
                    break;
                case 50:
                    FriendsGuiManager friendsGuiManager0 = new FriendsGuiManager(player);
                    if(event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第1页)")){
                        friendsGuiManager0.openGui(2);
                    }else if(event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第2页)")){
                        friendsGuiManager0.openGui(3);
                    }else if(event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第3页)")){
                        friendsGuiManager0.openGui(4);
                    }else if(event.getInventory().getName().contains("§6Dxz§ezz.§bNet §c>> §4好友 §7(第4页)")){
                        friendsGuiManager0.openGui(5);
                    }
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
                    break;
                case 53:
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "dgui openfor "+player.getName()+" gerencaidan.yml");
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    break;
                default:
                    break;
            }
        }
    }
}
