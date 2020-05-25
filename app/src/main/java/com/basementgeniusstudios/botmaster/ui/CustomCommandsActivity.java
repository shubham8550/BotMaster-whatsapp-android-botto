package com.basementgeniusstudios.botmaster.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.basementgeniusstudios.botmaster.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomCommandsActivity extends AppCompatActivity {

    private ListView configLisV;
    private LayoutInflater layoutInflater;
    private Object configsListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_commands);

        JSONObject objectJSON = new JSONObject();
        try {
            objectJSON = DumpData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        configLisV = findViewById(R.id.configsListView);
        try {
            configsListViewAdapter = new configsListViewAdapter(
                    getApplicationContext(),
                    objectJSON
            );
            configLisV.setAdapter((ListAdapter) configsListViewAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject DumpData() throws JSONException {
        JSONObject base=new JSONObject();

        JSONObject data1=new JSONObject();
        data1.put("rule","Contains");
        data1.put("query","hii");
        data1.put("reply","how r u");

        JSONObject data2=new JSONObject();
        data2.put("rule","StartsWith");
        data2.put("query","/help");
        data2.put("reply","StartWith");

        JSONObject data3=new JSONObject();
        data3.put("rule","StartsWith");
        data3.put("query","/help");
        data3.put("reply","StartWith");

        JSONObject data4=new JSONObject();
        data4.put("rule","StartsWith");
        data4.put("query","/help");
        data4.put("reply","StartWith");


        JSONArray arr=new JSONArray();
        arr.put(data1);
        arr.put(data2);
        arr.put(data3);
        arr.put(data4);

        base.put("quries",arr);
        return base;
        //base.getJSONArray("quries").getJSONObject(0).getString("reply");

    }

    class configsListViewAdapter extends BaseAdapter {

        JSONObject jsonObject;// = DumpData();
        JSONArray jsonArray;// = jsonObject.getJSONArray("quries");
        Context context;

        public configsListViewAdapter(Context context, JSONObject jsonObject) throws JSONException {
            this.jsonObject = jsonObject;
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            jsonArray = jsonObject.getJSONArray("quries");
        }



        @Override
        public int getCount() {
            return jsonArray.length();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            ConfigListViewHolder configListViewHolder;
            if(convertView == null){
                convertView = layoutInflater.inflate(R.layout.configs_list_view_item, parent, false);
                configListViewHolder = new ConfigListViewHolder();
                configListViewHolder.rule_query_tv = convertView.findViewById(R.id.rule_query_tv);
                configListViewHolder.delete_rule = convertView.findViewById(R.id.image_del_View);
                convertView.setTag(configListViewHolder);
            }
            else {
                configListViewHolder = (ConfigListViewHolder) convertView.getTag();
            }

            try {
                configListViewHolder.rule_query_tv.setText(
                        jsonArray.getJSONObject(position).getString("rule")
                                + ":"
                                + jsonArray.getJSONObject(position).getString("query")
                );

            } catch (JSONException e) {
                e.printStackTrace();
            }

            configListViewHolder.delete_rule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // code for removing rule from list_view
                    jsonArray.remove(position);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }

    static class ConfigListViewHolder{
        TextView rule_query_tv;
        ImageView delete_rule;
    }
}
