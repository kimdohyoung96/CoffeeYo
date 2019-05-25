package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;

public class CafeInfo {
    public int cafe_id;
    public String cafe_name;
    public double cafe_longitude;
    public double cafe_latitude;
    public int menu_cnt;
    public String menu1;
    public String menu2;
    public String menu3;

    public CafeInfo() {

    }

    public CafeInfo(int cafe_id, String cafe_name, double cafe_longitude, double cafe_latitude, int menu_cnt, String menu1, String menu2, String menu3) {
        this.cafe_id = cafe_id;
        this.cafe_name = cafe_name;
        this.cafe_longitude = cafe_longitude;
        this.cafe_latitude = cafe_latitude;
        this.menu_cnt = menu_cnt;
        this.menu1 = menu1;
        this.menu2 = menu2;
        this.menu3 = menu3;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("cafe_id", cafe_id);
        result.put("cafe_name", cafe_name);
        result.put("cafe_longitude", cafe_longitude);
        result.put("cafe_latitude", cafe_latitude);
        result.put("menu_cnt", menu_cnt);
        result.put("menu1", menu1);
        result.put("menu2", menu2);
        result.put("menu3", menu3);
        return result;
    }
}
