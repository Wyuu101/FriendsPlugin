package net.dxzzz.friends.Tab;

import net.dxzzz.friends.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompliter_fm implements TabCompleter {

    // 这个方法在玩家输入命令时被调用
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            return null;
        }
        Player player = (Player) sender;
        // 如果命令参数是第一个（例如：/mycommand <TAB>）
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            PlayerData playerData = new PlayerData(player.getName());
            List<String> friends = playerData.getFriends();
            completions.addAll(friends);
            return completions;
        }
        return completions;
    }
}
