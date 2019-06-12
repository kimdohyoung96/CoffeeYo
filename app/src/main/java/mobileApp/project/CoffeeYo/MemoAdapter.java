package mobileApp.project.CoffeeYo;

import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class MemoAdapter extends BaseAdapter {
    LayoutInflater inflater;
    private ArrayList<MemoItem> items;

    public MemoAdapter(Context context, ArrayList<MemoItem> memos) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = memos;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public MemoItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_layout, viewGroup, false);
        }

        MemoItem item = items.get(i);

        TextView tv1 = (TextView) view.findViewById(R.id.cafe_name_tv);
        TextView tv2 = (TextView) view.findViewById(R.id.menu_tv);
        TextView tv3 = (TextView) view.findViewById(R.id.take_tv);

        tv1.setText(item.getCafe_name());
        tv2.setText(item.getMenu());
        tv3.setText(item.getTake());

        return view;
    }
}


