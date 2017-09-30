package com.amatecny.interestingflights.flights.list;

import com.amatecny.interestingflights.network.Api;
import com.amatecny.interestingflights.persistence.Storage;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amatecny on 27/09/2017
 */
@Module
public class FlightListModule {

    @Provides
    static FlightListContract.View provideView( FlightListFragment fragment ) {
        return fragment;
    }

    @Provides
    static FlightListContract.Presenter providePresenter( FlightListContract.View view, Api networkApi, Storage storage ) {
        return new FlightListPresenter( view, networkApi, storage );
    }
}
