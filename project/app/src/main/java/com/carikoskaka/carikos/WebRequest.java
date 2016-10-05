package com.carikoskaka.carikos;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class WebRequest {
    static String response = null;
    static String baseUrl = "http://192.168.201.1/carikoskaka/public/api/v1/";
    public final static int GET = 1;
    public final static int POST = 2;

    //Constructor with no parameter
    public WebRequest() {
    }
    /**
     * Making web service call
     *
     * @url - url to make web request
     * @requestmethod - http request method
     */
    public String makeWebServiceCall(String url, int requestmethod) {
        return this.makeWebServiceCall(url, requestmethod, null);
    }
    /**
     * Making web service call
     *
     * @url - url to make web request
     * @requestmethod - http request method
     * @params - http request params
     */
    public String makeWebServiceCall(String urladdress, int requestmethod,HashMap<String, String> params) {
        URL url;
        String response = "";
        try {
            url = new URL(baseUrl+urladdress);
            Log.d("anto", urladdress  );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15001);
            conn.setConnectTimeout(15001);
            conn.setDoInput(true);
            if (requestmethod == POST) {
                conn.setDoOutput(true);
                conn.setRequestMethod("GET");
            } else if (requestmethod == GET) {
                conn.setRequestMethod("GET");
            }

            if (params != null) {
                OutputStream ostream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(ostream, "UTF-8"));
                StringBuilder requestresult = new StringBuilder();
                boolean first = true;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (first)
                        first = false;
                    else
                        requestresult.append("&");
                    requestresult.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    requestresult.append("=");
                    requestresult.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                }
                writer.write(requestresult.toString());

                writer.flush();
                writer.close();
                ostream.close();
            }
            int reqresponseCode = conn.getResponseCode();

            if (reqresponseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                //while ((line = br.readLine()) != null) {
                //    Log.d("anto", "acuan : " + line );
                //}
                response = null;
            }
        } catch (Exception e) {
            //Log.d("anto s s", "aa"  );
            e.printStackTrace();
        }
        return response;
    }
}