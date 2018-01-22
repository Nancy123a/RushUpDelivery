package me.zeroandone.technology.rushupdelivery.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationIDs {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "NotificationIDS";

    List< Map<String, String> > ids=new ArrayList<>();

    public NotificationIDs(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void addValuetoArrayList(Map<String, String>  hashmap){
      List<Map<String,String>>readAllValues= readAllValuesFromSharedPreference();
      if(readAllValues.size()>0) {
          for(Map<String,String> map:readAllValues){
              Log.d("HeroJongi","Values: "+map.get("key")+" "+map.get("value"));
          }
          ids.addAll(readAllValues);
      }
      ids.add(hashmap);
       Gson gson = new Gson();
       String json= gson.toJson(ids);
       editor.putString("Ids_array",json);
       editor.apply();
    }

    private List<Map<String,String>> readAllValuesFromSharedPreference() {
        List<Map<String,String>> Values=new ArrayList<>();
        String json = pref.getString("Ids_array","");
        if(!json.equalsIgnoreCase("")) {
            Gson gson = new Gson();
            Values = gson.fromJson(json, new TypeToken<List<Map<String, String>>>() {}.getType());
        }
        return Values;
    }

    public List<Map<String,String>> getValuesFromSharedPreference(String key){
      List<Map<String,String>> values=new ArrayList<>();
        String json = pref.getString("Ids_array","");
        if(!json.equalsIgnoreCase("")) {
            Gson gson = new Gson();
            List<Map<String,String>> Values = gson.fromJson(json, new TypeToken<List<Map<String, String>>>() {
            }.getType());
            for (Map<String,String> val : Values) {
                if(val.get("key").equalsIgnoreCase(key)){
                  values.add(val);
                }
            }
        }
        return values;
    }

    public void DeleteFromNotificationIds(String notification_id){
        List<Map<String, String>> values=new ArrayList<>();
        String json = pref.getString("Ids_array","");
        if(!json.equalsIgnoreCase("")) {
            Gson gson = new Gson();
            List<Map<String, String>>  Values = gson.fromJson(json, new TypeToken<List<Map<String, String>>>() {}.getType());
            for(Map<String, String>val:Values){
                if(!val.get("value").equalsIgnoreCase(notification_id)){
                   values.add(val);
                }
            }
            clearSharedPreference();
            String jsonarray= gson.toJson(values);
            editor.putString("Ids_array",jsonarray);
            editor.apply();
        }
    }

    public void clearSharedPreference(){
        if(pref!=null) {
            pref.edit().remove("Ids_array").apply();
        }
    }

}
