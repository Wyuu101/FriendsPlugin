package net.dxzzz.friends;

import com.skyedxzx.redisplayertracker.api.RedisPlayerAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import net.dxzzz.friends.Commands.*;
import net.dxzzz.friends.Database.DatabaseManager;
import net.dxzzz.friends.GUI.FriendsGuiListener;
import net.dxzzz.friends.Listener.PlayerJoinListener;
import net.dxzzz.friends.Tab.TabCompliter_fm;
import net.dxzzz.friends.Tab.TabCompliter_hy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Friends extends JavaPlugin {
    private static Friends instance;
    public static PlaceholderAPIPlugin placeholderAPI;
    public DatabaseManager databaseManager;
    public final Logger logger= getLogger();
    private String username;
    private String password;
    private int port;
    private String host;
    public boolean asLobby;
    private boolean loadConfigSuccess ;
    private String redisHost;
    private int redisPort;
    private String redisPassword;
    private int redisDatabase;
    private RedisManager redisManager;
    private RedisPlayerAPI redisPlayerAPI;
    @Override
    public void onEnable() {
        // Plugin startup logic
        logger.info("===========[Friends正在加载中]===========");
        logger.info("Author: X_32mx");
        logger.info("QQ: 2644489337");
        logger.info("This plugin is only for Dxzzz.net");
        this.saveDefaultConfig();
        this.reloadConfig();
        this.loadConfig();

        if(!loadConfigSuccess){
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if(!this.setupPlaceholderAPI()){
            logger.severe(ChatColor.RED+"未找到PlaceholderAPI前置插件,部分功能将失效。");
        }
        else {
            logger.info("已查找到PlaceholderAPI");
        }
        databaseManager = new DatabaseManager(username, password, host, port);
        databaseManager.createForm_PlayerData();
        databaseManager.createForm_InviteHistory();

        redisManager = new RedisManager(redisHost, redisPort, redisPassword, redisDatabase);
        redisPlayerAPI = redisManager.getRedisApi();

        Bukkit.getPluginCommand("hy").setExecutor(new CommandExc_hy(this));
        Bukkit.getPluginCommand("fg").setExecutor(new CommandExc_fg(this));
        Bukkit.getPluginCommand("fm").setExecutor(new CommandExc_fm(this));
        Bukkit.getPluginCommand("fr").setExecutor(new CommandExc_fr(this));
        Bukkit.getPluginCommand("hyop").setExecutor(new CommandExc_hyop(this));
        getCommand("hy").setTabCompleter(new TabCompliter_hy());
        getCommand("fm").setTabCompleter(new TabCompliter_fm());

        PlayerJoinListener playerJoinListener = new PlayerJoinListener();
        FriendsGuiListener friendsGuiListener = new FriendsGuiListener(this);
        Bukkit.getPluginManager().registerEvents(playerJoinListener, this);
        Bukkit.getPluginManager().registerEvents(friendsGuiListener, this);


        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        instance = this;
        logger.info("==========[Friends好友系统已加载完毕]=========");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        databaseManager.closeDataBase();
        redisManager.closeRedis();
        logger.info("好友系统已卸载");

    }

    private void loadConfig() {
        username = this.getConfig().getString("DataBase.MySQL.Username", "root");
        password = this.getConfig().getString("DataBase.MySQL.Password");
        host = this.getConfig().getString("DataBase.MySQL.Host", "localhost");
        port = this.getConfig().getInt("DataBase.MySQL.Port", 3306);
        asLobby = this.getConfig().getBoolean("AsLobby", true);
        redisDatabase = this.getConfig().getInt("Redis.database", 0);
        redisHost = this.getConfig().getString("Redis.host", "localhost");
        redisPort = this.getConfig().getInt("Redis.port", 6379);
        redisPassword = this.getConfig().getString("Redis.password");
        loadConfigSuccess=true;
    }


    private boolean setupPlaceholderAPI() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            return false;
        }
        placeholderAPI = (PlaceholderAPIPlugin) getServer().getPluginManager().getPlugin("PlaceholderAPI");
        return placeholderAPI != null;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    public RedisPlayerAPI getRedisApi() {
        return redisPlayerAPI;
    }
    public static Friends getInstance(){
        return instance;
    }
}
