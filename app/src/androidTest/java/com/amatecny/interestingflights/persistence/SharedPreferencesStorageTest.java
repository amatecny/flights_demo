package com.amatecny.interestingflights.persistence;

import android.support.test.InstrumentationRegistry;

import com.amatecny.interestingflights.flights.list.adapter.FlightListAdapterPresenterTest;
import com.amatecny.interestingflights.flights.model.Flight;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class SharedPreferencesStorageTest {

    @Test
    public void storeAndRetrieveFlights() throws Exception {
        SharedPreferencesStorage storage = new SharedPreferencesStorage( InstrumentationRegistry.getTargetContext(), "test" );

        List<Flight> flights = FlightListAdapterPresenterTest.prepareFlights();
        storage.storeFlights( flights );
        assertThat( storage.retrieveFlights().blockingGet() ).containsAll( flights );
        assertThat( storage.retrieveFlights().blockingGet() ).hasSameSizeAs( flights );
    }

}