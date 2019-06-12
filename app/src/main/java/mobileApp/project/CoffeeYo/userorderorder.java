package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;
public class userorderorder {
    public String cafe_menu;
    public String count;


    public userorderorder(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }
    public userorderorder(String cafe_menu, String count) {
        this.cafe_menu = cafe_menu;
        this.count = count;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cafe_menu",cafe_menu);
        result.put("count",count);
        return result;
    }
}