package mobileApp.project.CoffeeYo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyService extends Service {
    DatabaseReference mPostReference;
    String uid;
    int cnt_all = 0, cnt_all_before = 0, cnt_current = 0, cnt_current_before = 0;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPostReference = FirebaseDatabase.getInstance().getReference();
        uid = intent.getStringExtra("uid");

        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cnt_all_before = cnt_all;
                cnt_current_before = cnt_current;
                cnt_all = 0;
                cnt_current = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    cnt_all++;
                    Orderfirebase get = snapshot.getValue(Orderfirebase.class);
                    String state = get.state;
                    if(state.equals("current")){
                        cnt_current++;
                    }
                }
                if(cnt_all == cnt_all_before && cnt_current < cnt_current_before){
                    sendNotificationMessage();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference.child("user_list/"+uid+"/order").addValueEventListener(postListener);

        return START_STICKY;
    }

    private void sendNotificationMessage(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.drawable.ic_cafemain)
                .setContentText("주문하신 메뉴가 완료되었어요!:)")
                .setContentTitle("COFFEE-YO");

        Intent intentNoti = new Intent(this, ManagerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentNoti, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        builder.setColor(Color.RED);
        Uri ringtonUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtonUri);
        long[] vibrate = {0, 100, 200, 300};
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager) getSystemService((NOTIFICATION_SERVICE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "basic", NotificationManager.IMPORTANCE_DEFAULT));
        }
        manager.notify(0, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}