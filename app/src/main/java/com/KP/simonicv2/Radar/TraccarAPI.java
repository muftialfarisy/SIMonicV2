package com.KP.simonicv2.Radar;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.KP.simonicv2.Radar.Constant.credentials;
import static com.KP.simonicv2.Radar.Constant.serverDevicesList;

public class TraccarAPI {
    static MCrypt mCrypt=new MCrypt();
    static String decrypted = null;

    public static void getData(final Context context, RequestQueue requestQueue) {
        serverDevicesList = new ArrayList<>();

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, Constant.ULRservergetpostdevices, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Constant.statusserver = false;
                try {
                    decrypted = null;
                    //Constant.statusserverdevices=false;
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String data = jsonObject.getString("uniqueId");
                        String plaintext = data.replace("-", "");
                        //decrypt uuid
                        try {
                            decrypted = new String(mCrypt.decrypt(plaintext));
                            String[] dataparse = decrypted.split("-");
                            if(dataparse[0].equals(Constant.prevname))
                                serverDevicesList.add(data);
                            //Constant.statusserverdevices=true;}
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                catch (JSONException e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constant.statusserver = true;
                try{
                    Toast.makeText(context,Constant.errorserver,Toast.LENGTH_LONG).show();
                }catch (Exception e){}
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String,String>();
                String auth = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization",auth);
                return headers;
            }
        };

        requestQueue.add(mStringRequest);
    }

    public static void sendData(final Context context, RequestQueue requestQueue, String name, String id) {

        JSONObject object = new JSONObject();
        try {
            object.put("name",name);
            object.put("uniqueId",id);
        } catch (JSONException e){

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constant.ULRservergetpostdevices, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Constant.statusserver = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constant.statusserver = true;
                try{
                    Toast.makeText(context,Constant.errorserver,Toast.LENGTH_LONG).show();
                }catch (Exception e){}
            }
        })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> headers = new HashMap<String,String>();
                String auth = "Basic "+ Base64.encodeToString(credentials.getBytes(),Base64.NO_WRAP);
                headers.put("Authorization",auth);
                headers.put("Content-Type","application/json");

                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}
