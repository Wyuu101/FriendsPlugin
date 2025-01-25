package net.dxzzz.friends.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CustomItemStack extends ItemStack {
    public CustomItemStack(Material material, int amount, short damage, String displayName, List<String> lore) {
         super(material, amount, damage);
         ItemMeta meta = super.getItemMeta();
         meta.setDisplayName(displayName);
         meta.setLore(lore);
         super.setItemMeta(meta);
    }
    public CustomItemStack(Material material, int amount, short damage, String displayName) {
        super(material, amount, damage);
        ItemMeta meta = super.getItemMeta();
        meta.setDisplayName(displayName);
        super.setItemMeta(meta);
    }
}
