package io.forus.me.android.data.net;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import io.forus.me.android.data.entity.serializers.SignRecordsTypeAdapter;
import io.forus.me.android.data.entity.sign.request.SignRecords;
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
        MeServiceFactory.accountLocalDataSource = accountLocalDataSource;
    }

    public static void init(Context context, AccountLocalDataSource accountLocalDataSource){
        ourInstance = new MeServiceFactory(accountLocalDataSource);
    }


    public <T> T createRetrofitService(final Class<T> clazz, final String endPoint){
        return createRetrofitService(clazz, endPoint, null);
    }

    public <T> T createRetrofitService(final Class<T> clazz, final String endPoint, final String customAccessToken) {

        okhttp3.OkHttpClient.Builder httpClient = new okhttp3.OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(chain -> {

            Request original = chain.request();

            HttpUrl url = original.url()
                    .newBuilder()
                    .build();

            String token =  customAccessToken!= null ? customAccessToken : accountLocalDataSource.getTokenString();
            Request request = original.newBuilder()
                    .url(url)
                    .header("Authorization", token == null || token.isEmpty() ? "" :  "Bearer " + token )
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Client-Key", "general")
                    .header("Client-Type", "me_app-android")
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
                .registerTypeAdapter(SignRecords.class, new SignRecordsTypeAdapter())
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
