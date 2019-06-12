package mobileApp.project.CoffeeYo;


import java.util.HashMap;
import java.util.Map;

public class CafemenuCount {
    public String cafe_menu;
    public String count;

    public CafemenuCount(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }
    public CafemenuCount(String cafe_menu, String count) {
        this.cafe_menu = cafe_menu;
        this.count = count;

    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cafe_menu", cafe_menu);
        result.put("count", count);
        return result;
    }
}