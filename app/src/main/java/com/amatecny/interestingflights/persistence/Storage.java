package com.amatecny.interestingflights.persistence;

import android.support.annotation.NonNull;

import com.amatecny.interestingflights.flights.model.Flight;

import java.util.List;

import io.reactivex.Single;

/**
 * Represents the persistent storage
 * <p>
 * Created by amatecny on 27/09/2017
 */
public abstract class Storage {

    /**
     * Stores the flight to a persistent storage
     * @param flights list to be stored
     */
    public abstract void storeFlights( @NonNull List<Flight> flights );

    /**
     * Retrieves the flight stored in persistent storage, or empty list if there are no flights stored
     * @return single emitting the list of flights
     */
    @NonNull
    public abstract Single<List<Flight>> retrieveFlights();

    /**
     * Returns the time of the last update of flights, returns {@code 0L} if there was no update yet
     */
    public abstract long getLastUpdateTime();

    /**
     * Removes everything stored in storage
     */
    public abstract void clearStoredData();

}
