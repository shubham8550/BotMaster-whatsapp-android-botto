package com.basementgeniusstudios.botmaster.processor.dicegame;

import android.app.PendingIntent;

import com.basementgeniusstudios.botmaster.bean.BasicRequrement;
import com.basementgeniusstudios.botmaster.bean.BasicStructure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;
//    boolean isFromGroup;//list of wat we have
//            String groupname;
//            String msg;
//            String orimsg;
//            String sender;
//            String groupdevider=":";//@ or :
//            String package_name;
//            Action action;
//            Context context;
//            String todaysdate

public class startdicegame extends BasicRequrement implements BasicStructure {
    String gameStatFile="game_stat.json";
 //   String gameRoundStatFile="gameroundstat.json";

    @Override
    public void Start() throws JSONException, IOException, PendingIntent.CanceledException {
        if(msg.contains("play")){
            adminStartGame();
        }else if(msg.contains("participate")){
            participate();
        }else if(msg.contains("roll")){
            rolldice();
        }else if(msg.contains("start")){
            startgameplay();
        }
    }
    void adminStartGame() throws IOException, PendingIntent.CanceledException, JSONException {
        if( (sender.contains("Bholu") || sender.contains("aizen") || sender.contains("meme")) ) {

            isalreadywonned= false;
            String winnername="null";

            JSONObject player = new JSONObject();
            player.put(sender,1);

            JSONObject base = new JSONObject();
            base.put("players", player);
            base.put("rolled",new JSONArray());
            base.put("rollednum",0);
            base.put("isStarted","false");

            savefile(gameStatFile,base.toString());

            action.sendReply(context,"*-Setting up New Game*\n" +
                    " use ( */g participate* ) to participate in game \n\n" +
                    " Note:you cant participate in middle of the game so participate now\n \n" +
                    "*How To Play* : After admin Starts Game using command (/g start)" +
                    "there will be rounds each round ends after all participants done with rolling dice" +
                    " you can roll dice using command ( */g roll* ) and you can't roll dice multiple times in same round\n\n" +
                    " -1st person to reach 100th square will Win\n" +
                    "*All rules are similar to Snake And Ladders* " +
                    "\n\n *BotMasterK12[^-^]*\n");




        }else {
            reply("*You Don't Have Rights To Use This Command [^-^]*");
        }
    }
    int getrandom(){
        return new Random().nextInt(20)+5;
    }

    void participate() throws JSONException, IOException {
        JSONObject data= getJSONObjectFromFileName(gameStatFile);
      if(data.getString("isStarted").equals("false")){
          data.getJSONObject("players").put(sender,1);
          savefile(gameStatFile,data.toString());

          reply("*@"+sender+" Participated in Game Successfully");
      }else {
          reply("* @"+sender+"Game Already Started you have to Wait for next game*");
      }

    }

    void startgameplay() throws JSONException, IOException {
        if( (sender.contains("Bholu") || sender.contains("aizen") || sender.contains("meme")) ) {
            JSONObject data = getJSONObjectFromFileName(gameStatFile);
            data.put("isStarted", "true");
            savefile(gameStatFile, data.toString());

            reply("*Round Started* Use Command ( */g roll* ) to roll you dice");
        }else {
            reply("*You don't have what it takes to use this command bro/sis..*");
        }
    }

    void rolldice() throws JSONException, IOException {
        if(isalreadywonned){
            reply("* @"+winnername+" Already Won Game*");
            return;
        }

        JSONObject data=getJSONObjectFromFileName(gameStatFile);
        if(data.getString("isStarted").equals("true")){
            if(data.getJSONObject("players").toString().contains(sender)){
                if( ! isRolled(data.getJSONArray("rolled"))){


                    //main logic after checkup
                    rolling(data);


                }else {
                    reply("*@"+sender+" is Already rolled dice in this round*");
                }

            }else {
                reply(sender+" you are not participant ");
            }
        }else {
            reply("*Game not Started Wait for admin to start game*");
        }
    }

    private void rolling(JSONObject data) throws JSONException, IOException {

        int previousposition=data.getJSONObject("players").getInt(sender);
        int random=getrandom();
        data.getJSONObject("players").put(sender,previousposition+random);
        data.getJSONArray("rolled").put(sender);
        data.put("rollednum",data.getInt("rollednum")+1);
        savefile(gameStatFile,data.toString());
        postionrules(random,random+previousposition,data);

    }
    void setcustomposition(int pos,JSONObject data) throws JSONException, IOException {

        data.getJSONObject("players").put(sender,pos);
        savefile(gameStatFile,data.toString());
    }
    public void postionrules(int diceno,int position,JSONObject data) throws IOException, JSONException {
        if(position >99){
            winnerreply();
            return;
        }
        String notic="";
        if(data.getJSONObject("players").length() <= data.getInt("rollednum")){
            notic="\n\n*-------------------------*\n*This Round Ended Re roll now you can your Dice*";
            data.put("rollednum",0);
            data.put("rolled",new JSONArray());
            savefile(gameStatFile,data.toString());
        }
        switch (position){
            case 10:
                setcustomposition(20,data);
                reply("*@"+sender+" Rolled dice and Walked "+diceno+" Steps*\n " +
                        "But Aqua helped @"+sender+" using Portal Magic to teliport to position 20\n\n" +
                        "Current Position : "+20);
                break;
            case 11:
                setcustomposition(2,data);
                reply("*@"+sender+" Rolled dice and Walked "+diceno+" Steps*\n " +
                        "But Cute Loli Beaten @"+sender+" To the Death so now He's in position 2\n\n" +
                        "Current Position : "+2);
                break;
             case 50:
                setcustomposition(40,data);
                reply("*@"+sender+" Rolled dice and Walked "+diceno+" Steps*\n " +
                        "But Cute Loli Beaten @"+sender+" To the Death so now He's in position 40\n\n" +
                        "Current Position : "+40);
                break;
            case 90:
                setcustomposition(81,data);
                reply("*@"+sender+" Rolled dice and Walked "+diceno+" Steps*\n " +
                        "But Emilia Eaten @"+sender+" Alive so now He's in position 81\n\n" +
                        "Current Position : "+81);
                break;
            case 70:
                setcustomposition(80,data);
                reply("*@"+sender+" Rolled dice and Walked "+diceno+" Steps*\n " +
                        "But everyones's favorite Rem Helped @"+sender+"So He's in position 80 What a lucky Bastard\n\n" +
                        "Current Position : "+80);
                break;
            default:
                reply("*@"+sender+" Rolled dice and Walked "+diceno+"*\n " +
                        "Current Position : "+position+notic);
                break;

        }


    }
    static public boolean isalreadywonned= false;
    static public String winnername="null";
    void winnerreply(){



        isalreadywonned=true;
        winnername=sender;
        reply("*@"+sender+" We got our Winner *");
    }

    boolean isRolled(JSONArray array){
        return array.toString().contains(sender);
    }


}
