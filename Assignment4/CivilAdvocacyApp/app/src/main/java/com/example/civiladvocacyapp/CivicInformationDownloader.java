package com.example.civiladvocacyapp;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CivicInformationDownloader implements Runnable{
    private static final String TAG = "CivicInformationDownloader";
    //https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyA2CseaGZWMrDtGitPp_0vnk6iolOTgnY0&address=Chicago%2CIL
    private static final String URL = "https://www.googleapis.com/civicinfo/v2/representatives";
    private static final String APIKey = "AIzaSyA2CseaGZWMrDtGitPp_0vnk6iolOTgnY0";
    private final MainActivity mainActivity;
    private final String address;

    public CivicInformationDownloader(MainActivity mainActivity, String address) {
        this.mainActivity = mainActivity;
        this.address = address;
    }
    @Override
    public void run() {
        Uri.Builder buildURL = Uri.parse(URL).buildUpon();
        buildURL.appendQueryParameter("key", APIKey);
        buildURL.appendQueryParameter("address", address);

        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "run: " + urlToUse.toString());
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(
                        Request.Method.GET,
                        urlToUse,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                CivicInformation objCivicInformation = parseJSON(response.toString());
                                mainActivity.runOnUiThread(() -> mainActivity.updateData(objCivicInformation));
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "onErrorResponse: ", error);
                            }
                        });
        RequestQueue queue = Volley.newRequestQueue(mainActivity);
        queue.add(jsonObjectRequest);
    }

    private CivicInformation parseJSON(String s) {
        try {
            JSONObject jobjMain = new JSONObject(s);

            //Get normalizedInput data
            JSONObject normalizedInput = jobjMain.getJSONObject("normalizedInput");
            String normalizedInput_line1=normalizedInput.getString("line1");
            String normalizedInput_city=normalizedInput.getString("city");
            String normalizedInput_state=normalizedInput.getString("state");
            String normalizedInput_zip=normalizedInput.getString("zip");

            //Get Offices Data
            JSONArray offices=jobjMain.getJSONArray("offices");
            ArrayList<Offices> lstObjOffices = new ArrayList<>();
            for(int i=0;i<offices.length();i++){
                JSONObject jObjOffice=(JSONObject) offices.get(i);
                String jObjOffice_name = jObjOffice.has("name")?jObjOffice.getString("name"):"";

                JSONArray jObjOffice_officialIndices = jObjOffice.getJSONArray("officialIndices");
                ArrayList<Integer> lstofficialIndices = new ArrayList<>();
                for(int j=0;j<jObjOffice_officialIndices.length();j++){
                    lstofficialIndices.add(jObjOffice_officialIndices.getInt(j));
                }
                lstObjOffices.add(new Offices(jObjOffice_name,lstofficialIndices));
            }

            //Get Officials Data
            JSONArray officials = jobjMain.getJSONArray("officials");
            ArrayList<Officials> lstObjOfficials = new ArrayList<>();
            for(int k=0;k<officials.length();k++){
                JSONObject jObjOfficials = (JSONObject) officials.get(k);
                //name
                String jObjOfficials_name = jObjOfficials.has("name")?jObjOfficials.getString("name"):"";
                //address
                String address_city = "";
                String address_state = "";
                String address_zip = "";
                String address_line = "";
                if(jObjOfficials.has("address")) {
                    JSONArray jObjOfficials_addresses = jObjOfficials.getJSONArray("address");
                    if (jObjOfficials_addresses.length() > 0) {
                        JSONObject jObjOfficials_address = (JSONObject) jObjOfficials_addresses.get(0);
                        String address_line1 = jObjOfficials_address.has("line1") ? jObjOfficials_address.getString("line1") : "";
                        String address_line2 = jObjOfficials_address.has("line2") ? jObjOfficials_address.getString("line2") : "";
                        String address_line3 = jObjOfficials_address.has("line3") ? jObjOfficials_address.getString("line3") : "";
                        address_city = jObjOfficials_address.has("city") ? jObjOfficials_address.getString("city") : "";
                        address_state = jObjOfficials_address.has("state") ? jObjOfficials_address.getString("state") : "";
                        address_zip = jObjOfficials_address.has("zip") ? jObjOfficials_address.getString("zip") : "";
                        address_line = address_line1 + " " + address_line2 + " " + address_line3;
                    }
                }
                //Party
                String jObjOfficials_party=jObjOfficials.has("party")?jObjOfficials.getString("party"):"";
                //Phones
                JSONArray jObjOfficials_phones=jObjOfficials.has("phones")?jObjOfficials.getJSONArray("phones"):null;
                String jObjOfficials_phone="";
                if(jObjOfficials_phones!=null){
                    jObjOfficials_phone = jObjOfficials_phones.get(0).toString();
                }
                //Urls
                JSONArray jObjOfficials_urls=jObjOfficials.has("urls")?jObjOfficials.getJSONArray("urls"):null;
                String jObjOfficials_url="";
                if(jObjOfficials_urls!=null){
                    jObjOfficials_url = jObjOfficials_urls.get(0).toString();
                }
                //Emails
                JSONArray jObjOfficials_emails=jObjOfficials.has("emails")?jObjOfficials.getJSONArray("emails"):null;
                String jObjOfficials_email="";
                if(jObjOfficials_emails!=null){
                    jObjOfficials_email = jObjOfficials_emails.get(0).toString();
                }
                //photoUrl
                String jObjOfficials_photoUrl = jObjOfficials.has("photoUrl")?jObjOfficials.getString("photoUrl"):"";
                //Channels
                JSONArray jObjOfficials_channels=jObjOfficials.has("channels")?jObjOfficials.getJSONArray("channels"):null;
                ArrayList<Channels> lstObjChannels=null;
                if(jObjOfficials_channels!=null)
                {
                    lstObjChannels = new ArrayList<>();
                    for(int l=0;l<jObjOfficials_channels.length();l++)
                    {
                        JSONObject jObjOfficials_channel =(JSONObject) jObjOfficials_channels.get(l);
                        String jObjOfficials_channels_type = jObjOfficials_channel.has("type")?jObjOfficials_channel.getString("type"):"";
                        String jObjOfficials_channels_id = jObjOfficials_channel.has("id")?jObjOfficials_channel.getString("id"):"";
                        lstObjChannels.add(new Channels(jObjOfficials_channels_type, jObjOfficials_channels_id));
                    }
                }
                lstObjOfficials.add(new Officials(jObjOfficials_name,address_line,address_city,address_state,address_zip,
                        jObjOfficials_party,jObjOfficials_phone,jObjOfficials_url,jObjOfficials_email,jObjOfficials_photoUrl,lstObjChannels));

            }
            return new CivicInformation(normalizedInput_line1,normalizedInput_city,normalizedInput_state,normalizedInput_zip,lstObjOffices,lstObjOfficials);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "error: ", e);
        }
        return null;
    }
}
