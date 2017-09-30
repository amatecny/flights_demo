package com.amatecny.interestingflights.application.di;

import android.content.Context;

import com.amatecny.interestingflights.application.FlightsApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amatecny on 27/09/2017
 */
@Module
public class FlightsApplicationModule {

    @Provides
    @Singleton
    Context provideContext( FlightsApplication application ) {
        return application.getApplicationContext();
    }
}
