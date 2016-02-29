package com.example.tiagoferreira.androidwear_sensorfusion;

/**
 * Created by Tiago on 29/02/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SimpleListActivity extends Activity implements WearableListView.ClickListener{

    private WearableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen);
       // final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
       // stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
           // @Override
         //   public void onLayoutInflated(WatchViewStub stub) {
                mListView = (WearableListView) findViewById(R.id.listView1);
                mListView.setAdapter(new MyAdapter(SimpleListActivity.this));
                mListView.setClickListener(SimpleListActivity.this);
           // }
        //});
    }

    private static ArrayList<String> listItems;
    static {
        listItems = new ArrayList<String>();
        listItems.add("Train");
        listItems.add("Test");

    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        switch (viewHolder.getPosition()) {
            case 0:
                //Toast.makeText(SimpleListActivity.this, "Train", Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(SimpleListActivity.this, MainActivity.class);
                SimpleListActivity.this.startActivity(myIntent);
                break;
            case 1:
                //Toast.makeText(SimpleListActivity.this, "Test", Toast.LENGTH_LONG).show();
                Intent myIntent2 = new Intent(SimpleListActivity.this, TestActivity.class);
                SimpleListActivity.this.startActivity(myIntent2);
                break;
        }

    }

    @Override
    public void onTopEmptyRegionClick() {

    }

    private class MyAdapter extends WearableListView.Adapter {
        private final LayoutInflater mInflater;

        private MyAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WearableListView.ViewHolder(
                    mInflater.inflate(R.layout.row_simple_item_layout, null));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
            TextView view = (TextView) holder.itemView.findViewById(R.id.textView);
            view.setText(listItems.get(position).toString());
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }
    }
}