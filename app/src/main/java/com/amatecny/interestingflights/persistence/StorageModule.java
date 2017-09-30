package com.amatecny.interestingflights.persistence;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Contains the providers for persistence
 * <p>
 * Created by amatecny on 27/09/2017
 */
@Module
public class StorageModule {

    @Provides
    @Singleton
    static Storage provideStorage( Context context) {
        return new SharedPreferencesStorage( context.getSharedPreferences( "flights", Context.MODE_PRIVATE ) );
    }
}
