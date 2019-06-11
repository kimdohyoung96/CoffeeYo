package mobileApp.project.CoffeeYo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter <MenuAdapter.ViewHolder> {

    private ArrayList<menuitem> items = new ArrayList<>();
    Context context;

    private ItemClick itemClick;
    public interface ItemClick {
        public void onClick(View view,int position);
    }

    //아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menuitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        context = parent.getContext();



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder viewHolder, final int position) {

        menuitem item = items.get(position);

        viewHolder.menuName.setText(item.getMenu()+"  ["+item.getPrice()+"원]");

        final int[] i = {0};
        final TextView count = viewHolder.count;
        count.setText(String.valueOf(i[0]));

        viewHolder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick != null){
                    itemClick.onClick(v, position);
                    i[0]++;
                    count.setText(String.valueOf(i[0]));
                }
            }
        });

        viewHolder.subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick != null) {
                    itemClick.onClick(v, position);
                    if (i[0] != 0) {
                        i[0]--;
                        count.setText(String.valueOf(i[0]));
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<menuitem> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton addButton;
        ImageButton subButton;
        TextView menuName;
        TextView count;
        final View mView;

        ViewHolder(View itemView) {
            super(itemView);

            addButton = itemView.findViewById(R.id.imageadd);
            subButton = itemView.findViewById(R.id.imagesub);
            count = itemView.findViewById(R.id.count);
            menuName = itemView.findViewById(R.id.menutext);
            mView = itemView;
        }
    }
}

