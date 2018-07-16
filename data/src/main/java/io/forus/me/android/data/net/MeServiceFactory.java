package io.forus.me.android.data.net;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import io.forus.me.android.data.repository.account.datasource.local.AccountLocalDataSource;
import io.forus.me.android.domain.converters.BooleanSerializer;
import io.forus.me.android.domain.converters.DoubleSerializer;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeServiceFactory {

    private static AccountLocalDataSource accountLocalDataSource;



    private static MeServiceFactory ourInstance;

    public static MeServiceFactory getInstance() {
        return ourInstance;
    }

    private MeServiceFactory(AccountLocalDataSource accountLocalDataSource) {
        this.accountLocalDataSource = accountLocalDataSource;
    }

    public static void init(Context context, AccountLocalDataSource accountLocalDataSource){
        ourInstance = new MeServiceFactory(accountLocalDataSource);
    }



    public <T> T createRetrofitService(final Class<T> clazz, final String endPoint) {

        okhttp3.OkHttpClient.Builder httpClient = new okhttp3.OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(chain -> {

            Request original = chain.request();

            HttpUrl url = original.url()
                    .newBuilder()
                    .build();

            String token =  accountLocalDataSource.getTokenString();
            Request request = original.newBuilder()
                    .url(url)
                    .header("Authorization", token == null || token.isEmpty() ? "" :  "Bearer " + accountLocalDataSource.getTokenString() )
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                  //  .header("Origin", Constants.INSTANCE.getBASE_SERVICE_ENDPOINT())
                //    .header("User-agent", Constants.getUserAgent())
                 //   .header("X-USER-OS", "Android")
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        });

//        for (okhttp3.Interceptor item : HttpInterceptors.getInstance().getInterceptors()){
//            httpClient.addInterceptor(item);
//        }

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(Boolean.class,new BooleanSerializer())
                .registerTypeAdapter(Double.class,new DoubleSerializer())
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setLenient()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting()
                .create();


        httpClient.readTimeout(1, TimeUnit.MINUTES);
        httpClient.connectTimeout(1, TimeUnit.MINUTES);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endPoint)

                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .client(httpClient.build())
                .build();


        T service = retrofit.create(clazz);

        return service;
    }





}
