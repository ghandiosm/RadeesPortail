package ma.mghandi.radeesportail;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jamal on 19/05/2017.
 */

public class SharedData {
    private static final String MyPREFERENCES = "MyPrefs";
    private static SharedPreferences sharedPreferences;

    public static String getKey(Context ctx, String key){
        sharedPreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public static void setKey(Context ctx, String key, String value){
        sharedPreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static Set<String> getListKey(Context ctx, String key){
        sharedPreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(key,null);
    }

    public static void setListKey(Context ctx, String key, ArrayList<String> arrayList){
        sharedPreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Set the values
        Set<String> set = new HashSet<String>();
        set.addAll(arrayList);
        editor.putStringSet(key, set);
        editor.commit();
    }

    public static void saveArrayList(Context ctx, String key,ArrayList<String> list){
        sharedPreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.commit();     // This line is IMPORTANT !!!
    }

    public static ArrayList<String> getArrayList(Context ctx, String key){
        sharedPreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
