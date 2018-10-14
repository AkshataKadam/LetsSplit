package com.example.anuswa.letssplit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class lay extends AppCompatActivity {

    String[] names={"Akshu","Swarali","Pallavi"};
    int[] cost={100,200,300};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay);

        ListView list=(ListView)findViewById(R.id.list_id);

        custadapter cust = new custadapter();
        list.setAdapter(cust);

    }

    class custadapter extends BaseAdapter{


        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.layout,null);

            TextView text_name = (TextView)view.findViewById(R.id.name_id);
            TextView text_cost = (TextView)view.findViewById(R.id.cost_id);

            text_name.setText(names[i]);
            text_cost.setText(cost[i]);
            return view;
        }
    }
}
