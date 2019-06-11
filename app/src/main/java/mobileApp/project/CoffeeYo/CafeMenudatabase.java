package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;

public class CafeMenudatabase {

    public String menu_name;
    public String price;


    public CafeMenudatabase(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public CafeMenudatabase(String menu_name, String price) {

        this.menu_name = menu_name;
        this.price = price;


    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("menu_name", menu_name);
        result.put("price", price);

        return result;
    }
}