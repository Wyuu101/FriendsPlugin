package net.dxzzz.friends;

public class FriendsAPI {
    public static PlayerData getPlayerData(String playerName){
        return new PlayerData(playerName);
    }
}
