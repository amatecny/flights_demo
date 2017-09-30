package com.amatecny.interestingflights.application.di;

import com.amatecny.interestingflights.application.FlightsApplication;
import com.amatecny.interestingflights.network.NetworkModule;
import com.amatecny.interestingflights.persistence.StorageModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by amatecny on 27/09/2017
 */
@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        FlightsApplicationModule.class,
        ActivityBuilderModule.class,
        NetworkModule.class,
        StorageModule.class
})
public interface FlightsApplicationComponent {

    void inject( FlightsApplication application );

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application( FlightsApplication application );

        FlightsApplicationComponent build();
    }
}
