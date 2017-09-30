package com.amatecny.interestingflights.network;

import com.amatecny.interestingflights.BuildConfig;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Contains the providers for everything related to networking
 * <p>
 * Created by amatecny on 27/09/2017
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        //for debug purpose log the contents of the requests and responses
        if ( BuildConfig.DEBUG ) {
            HttpLoggingInterceptor interceptorLog = new HttpLoggingInterceptor();
            interceptorLog.setLevel( HttpLoggingInterceptor.Level.BODY );
            clientBuilder.addInterceptor( interceptorLog );
        }

        return clientBuilder.build();
    }

    @Provides
    @Singleton
    static GsonConverterFactory provideGsonConverterFactory( ) {
        return GsonConverterFactory.create(
                new GsonBuilder()
                        .registerTypeAdapterFactory( AutoValueGsonConvertorFactory.create() )
                        .create() );
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit( OkHttpClient client, GsonConverterFactory gsonConverterFactory ) {
        return new Retrofit.Builder()
                .baseUrl( "https://api.skypicker.com/" )
                .client( client )
                .addConverterFactory( gsonConverterFactory )
                .addCallAdapterFactory( RxJava2CallAdapterFactory.create() )
                .build();
    }

    @Provides
    @Singleton
    static Api provideApi( Retrofit retrofit ) {
        return new RealApi( retrofit );
    }
}
