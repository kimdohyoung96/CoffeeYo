package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;

public class Cafenamedatabase {

    public String cafe_name;


    public Cafenamedatabase(){

    }

    public Cafenamedatabase(String cafe_name) {

        this.cafe_name = cafe_name;


    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("cafe_name", cafe_name);
        return result;
    }
}