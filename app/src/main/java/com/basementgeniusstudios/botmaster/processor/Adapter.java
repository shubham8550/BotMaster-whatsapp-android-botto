package com.basementgeniusstudios.botmaster.processor;

import android.app.PendingIntent;
import android.content.Context;

import com.basementgeniusstudios.botmaster.bean.BasicRequrement;
import com.basementgeniusstudios.botmaster.config.Res;
import com.basementgeniusstudios.botmaster.config.conf;
import com.basementgeniusstudios.botmaster.processor.dicegame.startdicegame;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import models.Action;

public class Adapter extends BasicRequrement {
    String rawmsg; String rawsender; String rawpackage_name; Action rawaction; Context rawcontext;
    JSONObject config;
    //configs
    boolean isPollEnabled;
    boolean isGameEnabled;
    boolean isLinkWarnEnabled;
    boolean isAnimeSearchEnabled;
    String admins;

    public Adapter(String msg, String sender, String package_name, Action action, Context context) throws JSONException {
        try {
            config=new conf(context).getFullConfig();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(config== null){
            l("no Config Defined");
            return;
        }
        isPollEnabled=config.getString(Res.poll).equals("true");
        isGameEnabled=config.getString(Res.game).equals("true");
        isLinkWarnEnabled=config.getString(Res.warnHTTP).equals("true");
        isAnimeSearchEnabled=config.getString(Res.animeSearch).equals("true");
        admins=config.getString(Res.admins);

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

        if(msg.startsWith("/vote") && isPollEnabled ){
            voteadapter();
        }else if(msg.startsWith("/poll") && isPollEnabled){
            getpoll();
        }else if(msg.startsWith("/g") && isGameEnabled){
            new startdicegame().init(rawmsg,rawsender,rawpackage_name,rawaction,rawcontext);
        }else if(msg.startsWith("/resetpoll") && isPollEnabled){
            try {
                adminpollreset(orimsg.substring(10));
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }else if(msg.startsWith("/add") && isPollEnabled){
            addcandidate(orimsg.substring(4));
        }else if(msg.startsWith("/anime") && isAnimeSearchEnabled){
            new animesearch(orimsg.substring(6),action,context).start();
        }else if(msg.startsWith("/manga") && isAnimeSearchEnabled){
            new mangasearch(orimsg.substring(6),action,context).start();
        }else if(msg.startsWith("/character") && isAnimeSearchEnabled){
               new characterSearch(orimsg.substring(10),action,context).start();
        }else if(msg.startsWith("/help")){
            help();
        }else if(msg.startsWith("/replace") && isPollEnabled){
            adminreplace(msg.substring(10));
        }else if(msg.startsWith("/createpoll") && isPollEnabled){
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
        String op="Welcome to BotMaster\n" +
                "Basic Command List\n" +
                "1) */poll*\n" +
                "(For Polls)\n" +
                "2) */createpoll title*\n" +
                "(For Creation of Poll)\n" +
                "(Example */createpoll top 10 hero* )" +
                "3) */g play title*\n" +
                "(For Snake And Ladder game beta)\n" +
                "4) */anime animename*\n" +
                "(For anime Search)\n" +
                "5) */manga manganame*\n" +
                "(For manga Search)\n" +
                "6) */character animecharactername*\n" +
                "(For anime character Search)\n" +
                "\n\n" +
                "(^-^) BotMasterK12";
        reply(op);
    }

    void adminpollreset(String polltitle) throws IOException, PendingIntent.CanceledException, JSONException {
        if( admins.contains(sender) ) {
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
            reply("*You Don't Have Rights To Use This Command [^-^]*");
        }
    }

    void adminreplace(String candi) throws IOException, PendingIntent.CanceledException, JSONException {
        if( admins.contains(sender) ){
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
            reply("*You Don't Have Rights To Use This Command [^-^]*");
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
            op="*BotMasterK12*\n*Title : "+data.getString("title")+"*\n No candidates Added \n use (*\\add candidate-name*) to add candidate";
        }else {
            op="*BotMasterK12*\n*Title : "+data.getString("title")+"*\n";
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
        op="*Voted successfully on "+data.getJSONArray("candis").getJSONObject(num).getString("name")+"*\n*BotMasterK12*\n*Title : "+data.getString("title")+"*\n";
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
        if( admins.contains(sender) ){

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
