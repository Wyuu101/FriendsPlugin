package net.dxzzz.friends;

import net.dxzzz.friends.Database.DatabaseManager;
import net.dxzzz.friends.Utils.UniversalModule;

import java.util.ArrayList;
import java.util.List;

public class PlayerData{
    private final DatabaseManager databaseManager = Friends.getInstance().getDatabaseManager();
    private String username;
    private boolean isAvailable;
    private int num = 0;
    private boolean isVip= false;
    private boolean canReceiveFriendRequest = true;
    private boolean canReceivePrivateMessage = true;
    private boolean canReceiveGlobalMessage = true;
    private List<String> friends = null;
    private String lastChatWith = null;

    public PlayerData(String username) {
        this.username = username;
        loadData();
    }

    private void loadData() {
        List<String> basicInfo = databaseManager.getPlayerBasicInfo(username);
        if(basicInfo.isEmpty()){
            this.isAvailable = false;
            this.num = 0;
            this.isVip = false;
            this.friends = new ArrayList<>();
            return;
        }
        this.num = Integer.parseInt(basicInfo.get(0));
        this.isVip = (basicInfo.get(1).equals("1"));
        this.friends = databaseManager.getAllFriends(username);
        this.isAvailable = true;
        this.lastChatWith = basicInfo.get(2);
        this.canReceiveFriendRequest = basicInfo.get(3).equals("1");
        this.canReceivePrivateMessage = basicInfo.get(4).equals("1");
        this.canReceiveGlobalMessage = basicInfo.get(5).equals("1");
    }

    public boolean isAvailable() {
        return isAvailable;
    }
    public int getNum() {
        return num;
    }
    public boolean isVip() {
        return isVip;
    }
    public List<String> getFriends() {
        return friends;
    }
    public String getLastChatWith() {
        return lastChatWith;
    }
    public boolean canReceiveFriendRequestStatus() {
        return canReceiveFriendRequest;
    }
    public boolean canReceivePrivateMessageStatus() {
        return canReceivePrivateMessage;
    }
    public boolean canReceiveGlobalMessageStatus() {
        return canReceiveGlobalMessage;
    }

    public void setLastChatWith(String lastChatWith) {
        this.lastChatWith = lastChatWith;
    }

    public void setVip(boolean isVip) {
        this.isVip = isVip;
    }
    public void setFriends(List<String> friends) {
        this.friends = friends;
        this.num = friends.size();
    }

    public void switchCanReceiveFriendRequestStatus() {
        canReceiveFriendRequest = !canReceiveFriendRequest;
    }
    public void switchCanReceivePrivateMessageStatus() {
        canReceivePrivateMessage = !canReceivePrivateMessage;
    }
    public void switchCanReceiveGlobalMessageStatus() {
        canReceiveGlobalMessage = !canReceiveGlobalMessage;
    }

    public void setCanReceiveFriendRequestStatus(boolean canReceiveFriendRequest) {
        this.canReceiveFriendRequest = canReceiveFriendRequest;
    }
    public void setCanReceivePrivateMessageStatus(boolean canReceivePrivateMessage) {
        this.canReceivePrivateMessage = canReceivePrivateMessage;
    }
    public void setCanReceiveGlobalMessageStatus(boolean canReceiveGlobalMessage) {
        this.canReceiveGlobalMessage = canReceiveGlobalMessage;
    }

    public void saveData() {
        databaseManager.setPlayerVipStatus(username,isVip?1:0);
        databaseManager.setPlayerFriends(username, UniversalModule.listToString(friends),num);
        databaseManager.setLastChatWith(username,lastChatWith);
        databaseManager.setPlayerRequestStatus(username,canReceiveFriendRequest?1:0);
        databaseManager.setPlayerPrivateMsgStatus(username,canReceivePrivateMessage?1:0);
        databaseManager.setPlayerGlobalMsgStatus(username,canReceiveGlobalMessage?1:0);
    }

    public long getInviteHistoryTime(String target) {
        return databaseManager.getInviteHistoryTime(username,target);
    }

}
