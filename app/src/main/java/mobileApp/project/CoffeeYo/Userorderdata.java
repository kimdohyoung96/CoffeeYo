package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;
public class Userorderdata {
    public String cafe_name;
    public String order;
    public String state;
    public String take;

    public Userorderdata(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }
    public Userorderdata(String cafe_name, String order, String state, String take) {
        this.cafe_name = cafe_name;
        this.order = order;
        this.state = state;
        this.take = take;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cafe_name", cafe_name);
        result.put("order", order);
        result.put("state", state);
        result.put("take", take);
        return result;
    }
}