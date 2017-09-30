package com.amatecny.interestingflights.flights.activity.di;

import com.amatecny.interestingflights.flights.list.FlightListFragment;
import com.amatecny.interestingflights.flights.list.FlightListModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by amatecny on 27/09/2017
 */
@Module
public abstract class FlightsFragmentBuilderModule {

    @ContributesAndroidInjector(modules = FlightListModule.class)
    abstract FlightListFragment bindFlightList();
}
