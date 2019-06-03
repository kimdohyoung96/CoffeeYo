package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;

public class CafeMenu {
    public String menu;
    public int menu_cnt;

    public CafeMenu(){
    }

    public CafeMenu(String menu, int menu_cnt){
        this.menu = menu;
        this.menu_cnt = menu_cnt;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("menu"+menu_cnt, menu);
        return result;
    }
}
