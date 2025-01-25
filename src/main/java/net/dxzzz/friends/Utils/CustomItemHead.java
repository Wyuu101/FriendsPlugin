package net.dxzzz.friends.Utils;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class CustomItemHead extends ItemStack {
    private static final HeadDatabaseAPI HeadDatabaseAPI = new HeadDatabaseAPI();
    public CustomItemHead(Material material, int amount, short damage, String displayName) {
        super(material, amount, damage);
        SkullMeta meta = (SkullMeta) this.getItemMeta();
        meta.setDisplayName(displayName);
        this.setItemMeta(meta);
    }
    public CustomItemHead(Material material, int amount, short damage, String displayName, List<String> lore) {
        super(material, amount, damage);
        SkullMeta meta = (SkullMeta) this.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        this.setItemMeta(meta);
    }
    public CustomItemHead(int amount, String displayName, List<String> lore, String playerName) {
        super(Material.SKULL_ITEM, amount, (short)3);
        SkullMeta meta = (SkullMeta) this.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        meta.setOwner(playerName);
        this.setItemMeta(meta);
    }
    public CustomItemHead(String headID, String displayName){
        super(HeadDatabaseAPI.getItemHead(headID));
        SkullMeta meta = (SkullMeta) this.getItemMeta();
        meta.setDisplayName(displayName);
        this.setItemMeta(meta);
    }
    public CustomItemHead(String headID, String displayName, List<String> lore){
        super(HeadDatabaseAPI.getItemHead(headID));
        SkullMeta meta = (SkullMeta) this.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        this.setItemMeta(meta);
    }

}
