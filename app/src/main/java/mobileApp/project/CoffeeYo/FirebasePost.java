package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;
public class FirebasePost {
    public String email;
    public String name;
    public String uid;
    public String money;
    public String cafe_id;
    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }
    public FirebasePost(String email, String name, String uid, String money, String cafe_id) {
        this.email = email;
        this.name = name;
        this.uid = uid;
        this.money = money;
        this.cafe_id = cafe_id;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("name", name);
        result.put("uid", uid);
        result.put("money", money);
        result.put("cafe_id", cafe_id);
        return result;
    }
}