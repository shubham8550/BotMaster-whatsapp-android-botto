package com.basementgeniusstudios.botmaster.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.basementgeniusstudios.botmaster.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

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
    public void removeRule(int idnex) throws IOException, JSONException {
        JSONObject base=getJSONObjectFromFileName(getString(R.string.customRulesFile));
        base.getJSONArray("quries").remove(idnex);
        savefile(getString(R.string.customRulesFile),base.toString());
    }

    public JSONObject DumpData() throws JSONException {

        JSONObject base=getJSONObjectFromFileName(getString(R.string.customRulesFile));
        if(base==null){
            base=new JSONObject();
//            data1.put("rule","Contains");
//            data1.put("query","hii");
//            data1.put("reply","how r u");
        }
        return base;

//        JSONObject data1=new JSONObject();
//        data1.put("rule","Contains");
//        data1.put("query","hii");
//        data1.put("reply","how r u");
//
//        JSONArray arr=new JSONArray();
//        arr.put(data1);
//
//        base.put("quries",arr);
//        return base;
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

                    try {
                        removeRule(position);
                        jsonObject=DumpData();

                        jsonArray=jsonObject.getJSONArray("quries");
                        Toast.makeText(context, "Rule Deleted Successfully", Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        Toast.makeText(context, "Unknown Error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    Log.d("pokemon"," removed on "+String.valueOf(position));
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


//extras
    public JSONObject getJSONObjectFromFileName(String ffname){
        File rootFolder = CustomCommandsActivity.this.getExternalFilesDir(null);
        File jsonFile = new File(rootFolder, ffname);
        String json = null;
        try {
            InputStream is = new FileInputStream(jsonFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            //l("Error on converting text to json obj in getJSONObjectFromFileName from file "+ffname);
        }
        return null;
    }
    public boolean isFileExist(String filss){
        File rootFolder = CustomCommandsActivity.this.getExternalFilesDir(null);
        File jsonFile = new File(rootFolder, filss);

        if(jsonFile.exists()){
            return true;
        }
        return false;
    }
    public void savefile(String fl, String jsonString) throws IOException {
        File rootFolder = CustomCommandsActivity.this.getExternalFilesDir(null);
        File jsonFile = new File(rootFolder, fl);
        FileWriter writer = new FileWriter(jsonFile);
        writer.write(jsonString);
        writer.close();
        //or IOUtils.closeQuietly(writer);

    }
}
