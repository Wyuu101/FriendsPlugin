package net.dxzzz.friends.Database;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import net.dxzzz.friends.Utils.UniversalModule;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class DatabaseManager {
    private final Database mainDb;
    private final Database userDb;
    public DatabaseManager(String username, String password, String host, int port){
        String address = host+":"+port;
        DatabaseOptions mainDb_options= DatabaseOptions.builder().mysql(username, password, "Friends", address).build();
        DatabaseOptions userDb_options= DatabaseOptions.builder().mysql(username, password, "authme", address).build();
        mainDb = PooledDatabaseOptions.builder().options(mainDb_options).createHikariDatabase();
        userDb = PooledDatabaseOptions.builder().options(userDb_options).createHikariDatabase();


    }

    public Database getMainDb (){
        return mainDb;
    }




    //创建玩家数据表
    public void createForm_PlayerData() {
        try {
            mainDb.executeUpdate("CREATE TABLE IF NOT EXISTS PlayerData (" +
                    "username VARCHAR(255) PRIMARY KEY," +
                    "friends TEXT NOT NULL," +
                    "lastchatwith VARCHAR(255)," +
                    "num INT DEFAULT 0," +
                    "vip INT DEFAULT 0," +
                    "canreceiverequest INT DEFAULT 1," +
                    "canreceiveprivatemsg INT DEFAULT 1," +
                    "canreceiveglobalmsg INT DEFAULT 1)");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //创建邀请历史数据表
    public void createForm_InviteHistory() {
        try {
            mainDb.executeUpdate("CREATE TABLE IF NOT EXISTS InviteHistory (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "sender VARCHAR(255) NOT NULL," +
                    "target VARCHAR(255) NOT NULL," +
                    "time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"+
                    "UNIQUE KEY unique_invite (sender, target))");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }






    /*
    创建玩家的默认数据

     @param username 玩家用户名
     @param friends 玩家的好友列表
     @param num 玩家的好友数量
     @param vip 玩家是否为VIP(1为VIP，0为普通)
     */
    public void createPlayerData(String username,String friends,int num,int vip){
        try{
            mainDb.executeUpdate("INSERT IGNORE INTO PlayerData (username, friends, num, vip) VALUES (?, ?, ?, ?)",username,friends,num,vip);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*
    获取邀请历史的时间戳

    @param sender 邀请者用户名
    @param target 被邀请者用户名
    @return 邀请时间戳
     */
    public long getInviteHistoryTime(String sender, String target){
        Timestamp currentTime = new Timestamp(0);
        try{
            if(mainDb.getFirstColumn("SELECT time FROM InviteHistory WHERE sender = ? AND target = ?", sender, target)!=null){
                currentTime = mainDb.getFirstColumn("SELECT time FROM InviteHistory WHERE sender = ? AND target = ?", sender, target);
            }
            return currentTime.getTime();
        }catch (SQLException e) {
            e.printStackTrace();
            return currentTime.getTime();
        }
    }


    /*
    获取玩家最后的聊天对象

    @param username 玩家用户名
    @return 最后的聊天对象
     */
    public String getLastChatWith(String username){
        String lastChatWith = null;
        try{
            if(mainDb.getFirstColumn("SELECT lastchatwith FROM PlayerData WHERE username = ?", username)!=null){
                lastChatWith = mainDb.getFirstColumn("SELECT lastchatwith FROM PlayerData WHERE username = ?", username);
            }
            return lastChatWith;
        }catch (SQLException e) {
            e.printStackTrace();
            return lastChatWith;
        }
    }



    /*
    获取玩家的所有好友
    @return 好友列表
     */
    public List<String> getAllFriends(String username){
        List<String>  result = new ArrayList<>();
        try{
            String friends = mainDb.getFirstColumn("SELECT friends FROM PlayerData WHERE username = ?",username);
            result = UniversalModule.stringToList(friends);
            return result;
        }catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }


    /*
    获取玩家的基本信息

    @param username 玩家用户名
    @return 好友数量、是否为VIP、最后一个聊天对象
     */
    public List<String> getPlayerBasicInfo(String username){
        List<String> result = new ArrayList<>();
        try{
            if(mainDb.getFirstColumn("SELECT num FROM PlayerData WHERE username = ?",username)==null){
                return result;
            }
            int num = mainDb.getFirstColumn("SELECT num FROM PlayerData WHERE username = ?",username);
            int vip =mainDb.getFirstColumn("SELECT vip FROM PlayerData WHERE username = ?",username);
            int canreceiverequest =mainDb.getFirstColumn("SELECT canreceiverequest FROM PlayerData WHERE username = ?",username);
            int canreceiveprivatemsg =mainDb.getFirstColumn("SELECT canreceiveprivatemsg FROM PlayerData WHERE username = ?",username);
            int canreceiveglobalmsg =mainDb.getFirstColumn("SELECT canreceiveglobalmsg FROM PlayerData WHERE username = ?",username);
            String lastChatWith =mainDb.getFirstColumn("SELECT lastchatwith FROM PlayerData WHERE username = ?",username);
            result.add(String.valueOf(num));
            result.add(String.valueOf(vip));
            result.add(lastChatWith);
            result.add(String.valueOf(canreceiverequest));
            result.add(String.valueOf(canreceiveprivatemsg));
            result.add(String.valueOf(canreceiveglobalmsg));
            return result;
        }catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }


    /*
    新增或者覆盖邀请历史记录

    @param sender 邀请者用户名
    @param target 被邀请者用户名
     */
    public void addOrSetInviteHistory(String sender, String target,long timeMills){
        Timestamp timestamp = new Timestamp(timeMills);
        try{
            mainDb.executeUpdate("INSERT INTO InviteHistory (sender, target, time) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE time = VALUES(time)",sender,target,timestamp);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    设置玩家的最后的聊天对象

    @param username 玩家用户名
    @param lastChatWith 玩家的最后的聊天对象
     */
    public void setLastChatWith(String username, String lastChatWith) {
        try{
            mainDb.executeUpdate("UPDATE PlayerData SET lastchatwith = ? WHERE username = ?",lastChatWith,username);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    设置玩家的好友列表、好友数量

    @param username 玩家用户名
    @param friends 玩家的好友列表
    @param num 玩家的好友数量

     */
    public void setPlayerFriends(String username ,String friends,int num){
        try{
            mainDb.executeUpdate("UPDATE PlayerData SET friends = ?, num = ? WHERE username = ?",friends,num,username);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    设置玩家的VIP状态

    @param username 玩家用户名
    @param vip 玩家是否为VIP(1为VIP，0为普通)

     */
    public void setPlayerVipStatus(String username ,int vip){
        try{
            mainDb.executeUpdate("UPDATE PlayerData SET vip = ? WHERE username = ?",vip,username);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    设置玩家的被邀请状态开关

    @param username 玩家用户名
    @param canBeInvite 玩家是否可以被邀请(1为可以，0为不可以)

     */
    public void setPlayerRequestStatus(String username ,int canReceiveFriendRequest){
        try{
            mainDb.executeUpdate("UPDATE PlayerData SET canreceiverequest = ? WHERE username = ?",canReceiveFriendRequest,username);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     设置玩家的私聊状态开关

    @param username 玩家用户名
    @param canBeInvite 玩家是否可以收到私聊消息(1为可以，0为不可以)
     */
    public void setPlayerPrivateMsgStatus(String username ,int canReceivePrivateMsg){
        try{
            mainDb.executeUpdate("UPDATE PlayerData SET canreceiveprivatemsg = ? WHERE username = ?",canReceivePrivateMsg,username);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    设置玩家的全局聊天状态开关

    @param username 玩家用户名
    @param canBeInvite 玩家是否可以收到全局消息(1为可以，0为不可以)
     */
    public void setPlayerGlobalMsgStatus(String username ,int canReceiveGlobalMsg){
        try{
            mainDb.executeUpdate("UPDATE PlayerData SET canreceiveglobalmsg = ? WHERE username = ?",canReceiveGlobalMsg,username);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /*
    关闭数据库连接
     */
    public void closeDataBase(){
        if(mainDb!=null){
            mainDb.close();
        }
        if(userDb!= null){
            userDb.close();
        }
        DB.close();
    }



    public String getUserRealName(String username){
        String realname = username;
        try{
            realname = userDb.getFirstColumn("SELECT realname FROM authme WHERE username = ?",username);
            if(realname==null){
                realname= username;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return realname;
    }
}
