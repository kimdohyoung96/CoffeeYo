package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;

public class CafeInfo {
    public int manager_id;
    public String cafe_name;
    public double cafe_longitude;
    public double cafe_latitude;
    public String menu1;
    public String menu2;
    public String menu3;
    public String menu4;
    public String menu5;

    public CafeInfo() {

    }

    public CafeInfo(int manager_id, String cafe_name, double cafe_longitude, double cafe_latitude, String menu1, String menu2, String menu3, String menu4, String menu5) {
        this.manager_id = manager_id;
        this.cafe_name = cafe_name;
        this.cafe_longitude = cafe_longitude;
        this.cafe_latitude = cafe_latitude;
        this.menu1 = menu1;
        this.menu2 = menu2;
        this.menu3 = menu3;
        this.menu4 = menu4;
        this.menu5 = menu5;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("manger_id", manager_id);
        result.put("cafe_name", cafe_name);
        result.put("cafe_longitude", cafe_longitude);
        result.put("cafe_latitude", cafe_latitude);
        result.put("menu1", menu1);
        result.put("menu2", menu2);
        result.put("menu3", menu3);
        result.put("menu4", menu4);
        result.put("menu5", menu5);
        return result;
    }
}
