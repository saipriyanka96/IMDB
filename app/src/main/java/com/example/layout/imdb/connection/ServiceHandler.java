package com.example.layout.imdb.connection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Pri on 10/21/2017.
 */

public class ServiceHandler {
//class for service handling
    private static String response = null;
    //string value is null
    public final static int GET = 1;
    private final static int POST = 2;


    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }
//makeServiceCall() makes http call to particular url and fetches the response
    private String makeServiceCall(String url, int method, List<NameValuePair> params) {
        try {
            // http client created
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            //An entity that can be sent or received with an HTTP message
            //Basic implementation of an HTTP response that can be modified
            HttpResponse httpResponse = null;

            // Checking http request method type
            /*
            URL Encoding POST data
Before making HTTP request you need to encode the post data in order to convert all string data into valid url format.
             */
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    // Url Encoding the POST parameters
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = httpClient.execute(httpPost);
                //execute the post

            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    // URLEncodedUtils-A collection of utilities for encoding URLs.
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
//get the url and execute the url
                httpResponse = httpClient.execute(httpGet);

            }
            if (httpResponse != null) {
                httpEntity = httpResponse.getEntity();
                /*
                Obtains the message entity of this response, if any. The entity is provided by calling setEntity.
                    Returns-the response entity, or null if there is none
                 */
                //if it not equal to null
            }
            if (httpEntity != null) {
                response = EntityUtils.toString(httpEntity);
                /*
                toString()-Returns a string containing a concise, human-readable description of this object.
                 */
            }

        } catch (UnsupportedEncodingException | ClientProtocolException e) {
            /*
            UnsupportedEncodingException()-Constructs an UnsupportedEncodingException without a detail message
             ClientProtocolException-Signals an error in the HTTP protocol.
             */
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
//returns the response
    }
}