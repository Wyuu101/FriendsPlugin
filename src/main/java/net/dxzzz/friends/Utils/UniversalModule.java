package net.dxzzz.friends.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class UniversalModule {
    public static String listToString(List<?> list){
        Gson gson = new Gson();
        String s = gson.toJson(list);
        return s;
    }
    public static List<String>  stringToList(String jsonString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>(){}.getType();
        List<String> stringList = gson.fromJson(jsonString, listType);
        return stringList;
    }

    public static boolean canConvertToInt(String str) {
        try {
            Integer.parseInt(str);  // 尝试将字符串解析为整数
            return true;  // 如果没有异常，说明可以转换为整数
        } catch (NumberFormatException e) {
            return false;  // 如果抛出异常，说明无法转换为整数
        }
    }


}
