package com.amatecny.interestingflights.flights.activity;

import android.os.Bundle;

import com.amatecny.interestingflights.R;
import com.amatecny.interestingflights.flights.list.FlightListFragment;

import dagger.android.support.DaggerAppCompatActivity;

public class FlightsActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        //let the system restore the state on config state
        if ( savedInstanceState == null ) {
            getSupportFragmentManager().beginTransaction().replace( R.id.fragment_root, new FlightListFragment() ).commit();
        }
    }
}
