package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;
public class UserMenudata {
    public String menu1;
    public String menu2;


    public UserMenudata(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }
    public UserMenudata(String menu1, String menu2) {
        this.menu1 = menu1;
        this.menu2 = menu2;

    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("menu1", menu1);
        result.put("menu2", menu2);

        return result;
    }
}