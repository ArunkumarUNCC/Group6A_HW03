package com.group6a_hw03.group6a_hw03;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class RequestParams {
    String fMethod, fBaseURL;
    HashMap<String, String> fParams = new HashMap<>();

    public void addParam(String aKey, String aValue){
        fParams.put(aKey, aValue);
    }

    public RequestParams(String aMethod, String aBaseURL) {
        this.fMethod = aMethod;
        this.fBaseURL = aBaseURL;
    }

    public String getEncodedParams(){
        StringBuilder lBuilder = new StringBuilder();
        String lValue;
        for(String lKey : fParams.keySet()){
            try {
                lValue = URLEncoder.encode(fParams.get(lKey), "UTF-8");
                if(lBuilder.length() > 0){
                    lBuilder.append("&");
                }
                lBuilder.append(lKey + "=" +  lValue);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return lBuilder.toString();
    }

    public String getEncodedURL(){
        return this.fBaseURL + "?" + getEncodedParams();
    }

    public HttpURLConnection setUpConnection() throws IOException {

        if(fMethod.equals("GET")){
            URL lUrl = new URL(getEncodedURL());
            HttpURLConnection lConnection = (HttpURLConnection) lUrl.openConnection();
            lConnection.setRequestMethod("GET");
            return lConnection;
        }else{//POST
            URL lUrl = new URL(this.fBaseURL);
            HttpURLConnection lConnection = (HttpURLConnection) lUrl.openConnection();
            lConnection.setRequestMethod("POST");
            lConnection.setDoOutput(true);
            OutputStreamWriter lWriter = new OutputStreamWriter(lConnection.getOutputStream());
            lWriter.write(getEncodedParams());
            lWriter.flush();
            return lConnection;
        }

    }
}
