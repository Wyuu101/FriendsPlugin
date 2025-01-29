package net.dxzzz.friends;
import com.skyedxzx.redisplayertracker.api.RedisPlayerAPI;

public class RedisManager {
    private static RedisPlayerAPI redisApi;
    public RedisManager(String host, int port, String password, int database) {
        redisApi = new RedisPlayerAPI(host, port, password, database);
    }
    public RedisPlayerAPI getRedisApi() {
        return redisApi;
    }
    public void closeRedis() {
        if (redisApi != null) {
            redisApi.close();
        }
    }
}

