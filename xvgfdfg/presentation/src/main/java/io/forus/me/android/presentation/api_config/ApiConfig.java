package io.forus.me.android.presentation.api_config;

import io.forus.me.android.presentation.BuildConfig;
import io.forus.me.android.presentation.helpers.SharedPref;

public class ApiConfig {



    public  static String SERVER_URL = BuildConfig.SERVER_URL;

    private static String[] apiVariants = {"https://api.forus.io/", "https://demo.api.forus.io/",
            "https://staging.api.forus.io/", "https://dev.api.forus.io/"
    };



    public static ApiType getCurrentApiType(){
        if(SERVER_URL.equals(apiVariants[0])) return ApiType.PROD;
        else if(SERVER_URL.equals(apiVariants[1])) return ApiType.DEMO;
        else if(SERVER_URL.equals(apiVariants[2])) return ApiType.STAGING;
        else if(SERVER_URL.equals(apiVariants[3])) return ApiType.DEV;
        else return ApiType.OTHER;
    }

    public static void changeApi(ApiType apiType){

        if(apiType == ApiType.PROD) SERVER_URL = apiVariants[0];
        else  if(apiType == ApiType.DEMO) SERVER_URL = apiVariants[1];
        else  if(apiType == ApiType.STAGING) SERVER_URL = apiVariants[2];
        else  if(apiType == ApiType.DEV) SERVER_URL = apiVariants[3];
        else  SERVER_URL = apiVariants[0];

    }

    public static void changeToCustomApi(String customUrl){

        SERVER_URL = customUrl;

    }



    public static ApiType stringToApiType(String str){
        if(str.equalsIgnoreCase("PROD")) return ApiType.PROD;
        if(str.equalsIgnoreCase("DEMO")) return ApiType.DEMO;
        if(str.equalsIgnoreCase("STAGING")) return ApiType.STAGING;
        if(str.equalsIgnoreCase("DEV")) return ApiType.DEV;
        else  return ApiType.OTHER;
    }

}



