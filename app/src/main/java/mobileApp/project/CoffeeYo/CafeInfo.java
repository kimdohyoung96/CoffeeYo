package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;

public class CafeInfo {
    public String cafe_name;
    public String cafe_longitude;
    public String cafe_latitude;
    public String menu_cnt;

    public CafeInfo() {

    }

    public CafeInfo(String cafe_name, String cafe_longitude, String cafe_latitude, String menu_cnt) {
        this.cafe_name = cafe_name;
        this.cafe_longitude = cafe_longitude;
        this.cafe_latitude = cafe_latitude;
        this.menu_cnt = menu_cnt;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("cafe_name", cafe_name);
        result.put("cafe_longitude", cafe_longitude);
        result.put("cafe_latitude", cafe_latitude);
        result.put("menu_cnt", menu_cnt);
        return result;
    }
}
