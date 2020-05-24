package com.basementgeniusstudios.botmaster.processor;

import android.app.PendingIntent;
import android.content.Context;

import com.basementgeniusstudios.botmaster.bean.BasicRequrement;
import com.basementgeniusstudios.botmaster.processor.dicegame.startdicegame;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import models.Action;

public class Adapter extends BasicRequrement {
    String rawmsg; String rawsender; String rawpackage_name; Action rawaction; Context rawcontext;

    public Adapter(String msg, String sender, String package_name, Action action, Context context) {
        rawaction=action;
        rawsender=sender;
        rawcontext=context;
        rawmsg=msg;
        rawpackage_name=package_name;
        l(sender);
        if(sender.contains(groupdevider)){
            String[] parts =sender.split(groupdevider);
            this.sender=parts[1];
            this.groupname=parts[0];

            isFromGroup=true;

        }else{
            this.sender = sender;
            isFromGroup=false;
        }
        this.package_name=package_name;
        this.msg = msg.toLowerCase();
        this.orimsg = msg;
        this.action=action;
        this.context=context;

        //com.whatsapp

        if(package_name.equals("com.whatsapp")){
            try {
                this.Start();
            } catch (JSONException | IOException | PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }

    }

   public void Start() throws JSONException, IOException, PendingIntent.CanceledException {
      // l(package_name+" | "+groupname+": "+sender);
      //  l(todaysdate);

        if(msg.startsWith("/vote")){
            voteadapter();
        }else if(msg.startsWith("/poll")){
            getpoll();
        }else if(msg.startsWith("/g")){
            new startdicegame().init(rawmsg,rawsender,rawpackage_name,rawaction,rawcontext);
        }else if(msg.startsWith("/resetpoll")){
            try {
                adminpollreset(orimsg.substring(10));
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }else if(msg.startsWith("/add")){
            addcandidate(orimsg.substring(4));
        }else if(msg.startsWith("/anime")){
            new animesearch(orimsg.substring(6),action,context).start();
        }else if(msg.startsWith("/manga")){
            new mangasearch(orimsg.substring(6),action,context).start();
        }else if(msg.startsWith("/character")){
               new characterSearch(orimsg.substring(10),action,context).start();
        }else if(msg.startsWith("/help")){
            help();
        }else if(msg.startsWith("/replace")){
            adminreplace(msg.substring(10));
        }else if(msg.startsWith("/createpoll")){
            try {
                adminpollreset(orimsg.substring(11));;
               // pollcreate(msg.substring(11));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            //here loggin
            //new logging().init(rawmsg,rawsender,rawpackage_name,rawaction,rawcontext);
        }

    }

    private void help() {
        String op="Welcome to *Anime Guild of Maharashtra*\n" +
                "Command List\n" +
                "1) */poll*\n" +
                "(For Polls)\n" +
                "2) */createpoll title*\n" +
                "(For Creation of Poll)\n" +
                "(Example */createpoll top 10 hero* )";
        reply(op);
    }

    void adminpollreset(String polltitle) throws IOException, PendingIntent.CanceledException, JSONException {
        if( (sender.contains("Bholu") || sender.contains("aizen") || sender.contains("meme")) ) {
            savefile(todaysdate+".json",getFile(pollfile));

            JSONObject base = new JSONObject();
            base.put("title", polltitle);
            base.put("polldate",todaysdate);
            base.put("candis", "null");
            //l(base.toString());
            savefile(pollfile,base.toString());

            action.sendReply(context,"*Poll Created Successfully*\n Title : "+polltitle+"\n use (\\add candidate-name) to add candidate");

            //voterresetter
            JSONObject data = new JSONObject();
            JSONArray a=new JSONArray();
            a.put("newestentry");
            data.put("list",a);

            savefile(voterslist,data.toString());

        }else {
            reply("*You dont have what it takes to use this command bro/sis..*");
        }
    }

    void adminreplace(String candi) throws IOException, PendingIntent.CanceledException, JSONException {
        if( (sender.contains("Bholu") || sender.contains("aizen") || sender.contains("meme")) ){
            int index= 55;
            for (int i = 0; i < 9; i++) {
                if(msg.contains(Integer.toString(i+1))){
                    //l("jumping on vote "+(i));
                    index=i;
                    break;
                }
            }
            if(index==55){
                reply("wrong format");
                return;
            }

            JSONObject data = new JSONObject(getFile(pollfile));


            data.getJSONArray("candis").getJSONObject(index).put("name",candi);

            //l(base.toString());
            savefile(pollfile,data.toString());
            reply("Poll Candidate "+candi+" Changed Successfully");


        }else {
            reply("*You dont have what it takes to use this command bro/sis..*");
        }
    }
    void pollcreate(String polltitle) throws JSONException, PendingIntent.CanceledException, IOException {
      try {
          if(new JSONObject(getFile(pollfile)).getString("polldate").contains(todaysdate)){
              reply("*Todays Poll Already created*");
              return;
          }

      }catch (Exception e){
          e.printStackTrace();
      }
        savefile(todaysdate+".json",getFile(pollfile));
        JSONObject base = new JSONObject();
        base.put("title", polltitle);
        base.put("polldate",todaysdate);
        base.put("candis", "null");
        //l(base.toString());
        savefile(pollfile,base.toString());
        action.sendReply(context,"*Poll Created Successfully*\n Title : "+polltitle+"\n use (\\add candidate-name) to add candidate");

        //voterresetter
        JSONObject data = new JSONObject();
        JSONArray a=new JSONArray();
        a.put("newestentry");
        data.put("list",a);

        savefile(voterslist,data.toString());
    }


    void getpoll() throws JSONException {
        JSONObject data = new JSONObject(getFile(pollfile));
        l(data.getString("candis")+"************");
        String op="";
        if(data.getString("candis").equals("null")){
            op="*Anime Guild of Maharashtra*\n*Title : "+data.getString("title")+"*\n No candidates Added \n use (*\\add candidate-name*) to add candidate";
        }else {
            op="*Anime Guild of Maharashtra*\n*Title : "+data.getString("title")+"*\n";
            String ls="";
            JSONArray arr=data.getJSONArray("candis");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject cd=arr.getJSONObject(i);
                ls= ls+(Integer.toString(i+1))+")"+cd.getString("name")+" : ["+cd.getInt("votes")+" Votes] \n";

            }

            op=op+ls;
            op=op+"\n For voting use command */vote candidate-number* \n Example */vote 2*";

        }

        reply(op);

    }

    private void voteadapter() throws JSONException, IOException {
        if (isvoted()){
            reply("*you already voted for this poll*");
            return;
        }
        if (!isFromGroup){
            reply("*you can ony vote from Group*");
            return;
        }
        JSONObject data = new JSONObject(getFile(pollfile));

        if(data.getString("candis").equals("null")) {
            reply("No candidates Added In Poll");
            return;
        }

        JSONArray arr=data.getJSONArray("candis");
        for (int i = 0; i < arr.length(); i++) {
            if(msg.contains(Integer.toString(i+1))){
                l("jumping on vote "+(i+1));
                addvote(i);
                return;
            }
        }
        reply("Wrong Format");
    }

    void addvote(int num) throws JSONException, IOException {


        JSONObject data = new JSONObject(getFile(pollfile));

        int vts=data.getJSONArray("candis").getJSONObject(num).getInt("votes");

        vts=vts+1;

        data.getJSONArray("candis").getJSONObject(num).remove("votes");
        data.getJSONArray("candis").getJSONObject(num).put("votes",vts);

        savefile(pollfile,data.toString());

        String op;
        op="*Voted successfully on "+data.getJSONArray("candis").getJSONObject(num).getString("name")+"*\n*Anime Guild of Maharashtra*\n*Title : "+data.getString("title")+"*\n";
        String ls="";
        JSONArray arr=data.getJSONArray("candis");
        for (int i = 0; i < arr.length(); i++) {
            JSONObject cd=arr.getJSONObject(i);
            ls= ls+(Integer.toString(i+1))+")"+cd.getString("name")+" : ["+cd.getInt("votes")+" Votes] \n";

        }

        op=op+ls;
        op=op+"\n For voting use command */vote candidate-number* \n Example */vote 2*";
        reply(op);
        addvotedlog();


    }


    void addcandidate(String candi) throws JSONException, IOException {
        if( (sender.contains("Bholu") || sender.contains("aizen") || sender.contains("meme")) ){

        }else {
            reply("Ask Admin to add "+candi);
            return;
        }
        JSONObject data = new JSONObject(getFile(pollfile));

        if(data.getString("candis").equals("null")){

            JSONArray array = new JSONArray();
            JSONObject cd = new JSONObject();
            cd.put("name",candi);
            cd.put("votes",0);
            array.put(cd);
            data.remove("candis");
            data.put("candis",array);

        }else {
            if(data.getJSONArray("candis").length() >= 9){
                reply("*you cant add more than 9 candidates in poll*");
                return;
            }

            JSONObject cd = new JSONObject();
            cd.put("name",candi);
            cd.put("votes",0);

            data.getJSONArray("candis").put(cd);

        }




        //l(base.toString());
        savefile(pollfile,data.toString());
        reply("Poll Candidate "+candi+" Added Successfully");

    }




//Init for extras
    String pollfile="polldata.json";
    String voterslist="voterslist.json";



    //------------------extras


    boolean isvoted() throws JSONException {
        JSONObject data = new JSONObject(getFile(voterslist));

        return data.getJSONArray("list").toString().contains(sender);

    }
    void addvotedlog() throws JSONException, IOException {
        JSONObject data = new JSONObject(getFile(voterslist));
        data.getJSONArray("list").put(sender);
        savefile(voterslist,data.toString());
    }




    void rule1(){

//        if (action != null && (text.toLowerCase().contains("corona") || text.toLowerCase().contains("covid-19")) ) {
//            try {
//                action.sendReply(context,"*[Warning]use of restricted word* \n Sharing/discussion related to Corona/covid-19 is not allowed sender will be banned from the group for 3 days or permanent");
//            } catch (PendingIntent.CanceledException e) {
//                e.printStackTrace();
//            }
//
//        }
    }

}
