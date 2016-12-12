package com.example.abc.api;

import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Server {
        static OkHttpClient client;
        
        static {
                CookieManager cookieManager = new CookieManager();
                cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
                
                client = new OkHttpClient.Builder()
                                .cookieJar(new JavaNetCookieJar(cookieManager))
                                .build();
        }
        
        public static OkHttpClient getSharedClient(){
                return client;
        }
        
        public static Request.Builder requestBuilderWithApi(String api){
                return new Request.Builder()
                                .url("http://172.27.0.5:8080/membercenter/api/" + api);
        }
}
