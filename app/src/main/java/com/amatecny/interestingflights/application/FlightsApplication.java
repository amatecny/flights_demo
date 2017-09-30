package com.amatecny.interestingflights.application;

import android.app.Activity;
import android.app.Application;

import com.amatecny.interestingflights.application.di.DaggerFlightsApplicationComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

/**
 * Contains necessary initializations on an App level
 * <p>
 * Created by amatecny on 27/09/2017
 */
public class FlightsApplication extends Application implements HasActivityInjector{

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        //di
        DaggerFlightsApplicationComponent.builder()
                .application( this )
                .build()
                .inject( this );

        //logging
        Timber.plant( new Timber.DebugTree() );

    }
}
