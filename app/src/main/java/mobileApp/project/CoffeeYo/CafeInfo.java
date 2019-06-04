package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;

public class CafeInfo {
    public String cafe_name;
    public String cafe_longitude;
    public String cafe_latitude;

    public CafeInfo() {

    }

    public CafeInfo(String cafe_name, String cafe_longitude, String cafe_latitude) {
        this.cafe_name = cafe_name;
        this.cafe_longitude = cafe_longitude;
        this.cafe_latitude = cafe_latitude;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("cafe_name", cafe_name);
        result.put("cafe_longitude", cafe_longitude);
        result.put("cafe_latitude", cafe_latitude);
        return result;
    }
}
