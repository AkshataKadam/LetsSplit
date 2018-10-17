package com.example.anuswa.letssplit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<String> {

    String [] names;
    String [] debt;
    Context mContext;

    public MyAdapter(Context context, String[] personname,String[] persondebt)
    {
        super(context, R.layout.listview_item);
        this.names=personname;
        this.debt=persondebt;
        this.mContext=context;

    }

    @Override
    public int getCount() {
        return names.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder mViewHolder=new ViewHolder();
        if(convertView==null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listview_item, parent, false);

            mViewHolder.mName = convertView.findViewById(R.id.idpersonname);
            mViewHolder.mDebt = convertView.findViewById(R.id.idpersondebt);
            convertView.setTag(mViewHolder);
        }
        else
        {
          mViewHolder=(ViewHolder)convertView.getTag();
        }
        mViewHolder.mName.setText(names[position]);
        mViewHolder.mDebt.setText(debt[position]);
        return convertView;
    }

    static class ViewHolder
    {
        TextView mName;
        TextView mDebt;
    }
}
