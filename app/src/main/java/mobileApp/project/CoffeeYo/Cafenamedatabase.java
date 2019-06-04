package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;

public class Cafenamedatabase {

    public String cafe_id;
    public String cafe_name;


    public Cafenamedatabase(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public Cafenamedatabase(String cafe_id, String cafe_name) {

        this.cafe_id = cafe_id;
        this.cafe_name = cafe_name;


    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cafe_id", cafe_id);
        result.put("cafe_name", cafe_name);
        return result;
    }
}