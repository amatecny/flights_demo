package com.amatecny.interestingflights.application.di;

import com.amatecny.interestingflights.flights.activity.FlightsActivity;
import com.amatecny.interestingflights.flights.activity.di.FlightsFragmentBuilderModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by amatecny on 27/09/2017
 */
@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = {
            FlightsFragmentBuilderModule.class
    })
    abstract FlightsActivity bindFlightsActivity();

}
