package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;
public class Orderfirebase {
    public String cafe_name;
    public String order;
    public String state;

    public Orderfirebase(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public Orderfirebase(String cafe_name, String order, String state) {

        this.cafe_name = cafe_name;
        this.order = order;
        this.state = state;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cafe_name", cafe_name);
        result.put("order", order);
        result.put("state", state);
        return result;
    }
}