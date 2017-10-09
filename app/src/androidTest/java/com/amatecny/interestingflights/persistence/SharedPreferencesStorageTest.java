package com.amatecny.interestingflights.persistence;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.amatecny.interestingflights.flights.list.adapter.FlightListAdapterPresenterTest;
import com.amatecny.interestingflights.flights.model.Flight;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class SharedPreferencesStorageTest {

    private SharedPreferencesStorage storage;

    @Before
    public void setup() {
        //alternatively create a mock of chain of objects - prefs, editor, ...
        storage = new SharedPreferencesStorage( InstrumentationRegistry.getTargetContext().getSharedPreferences( "test", Context.MODE_PRIVATE ) );
    }

    @Test
    public void storeAndRetrieveFlights() throws Exception {
        List<Flight> flights = FlightListAdapterPresenterTest.prepareFlights();
        storage.storeFlights( flights );
        assertThat( storage.retrieveFlights().blockingGet() ).containsAll( flights );
        assertThat( storage.retrieveFlights().blockingGet() ).hasSameSizeAs( flights );
    }

    @Test
    public void clearFlights() throws Exception {
        List<Flight> flights = FlightListAdapterPresenterTest.prepareFlights();
        storage.storeFlights( flights );
        assertThat( storage.retrieveFlights().blockingGet() ).containsAll( flights );

        storage.clearStoredData();
        assertThat( storage.retrieveFlights().blockingGet() ).isEmpty();
        assertThat( storage.getLastUpdateTime()).isZero();
    }

    @After
    public void tearDown() {
        storage.clearStoredData();
    }

}