package com.amatecny.interestingflights.persistence;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.amatecny.interestingflights.flights.model.Flight;
import com.amatecny.interestingflights.network.AutoValueGsonConvertorFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Single;

/**
 * Simple storage implementation based on file(s).
 * <p>
 * Created by amatecny on 28/09/2017
 */
class SharedPreferencesStorage extends Storage {

    private static final String KEY_STORED_FLIGHTS = "storedFlights";
    private static final String KEY_LAST_UPDATE = "timeOfTheLastUpdate";

    private final Gson gson;
    private final SharedPreferences prefs;

    /**
     * @param prefs shared preferences to use
     */
    SharedPreferencesStorage( SharedPreferences prefs ) {
        gson = new GsonBuilder()
                .registerTypeAdapterFactory( AutoValueGsonConvertorFactory.create() )
                .create();

        this.prefs = prefs;
    }

    @Override
    public void storeFlights( @NonNull List<Flight> flights ) {
        //serialize and replace the content
        String flightsJson = gson.toJson( flights );

        prefs.edit()
                .putString( KEY_STORED_FLIGHTS, flightsJson ) //content
                .putLong( KEY_LAST_UPDATE, Calendar.getInstance().getTimeInMillis() ) //note the time
                .apply();
    }

    @NonNull
    @Override
    public Single<List<Flight>> retrieveFlights() {
        return Single.defer( () -> {
            //retrieve and deserialize
            String storedFlightsJson = prefs.getString( KEY_STORED_FLIGHTS, "" );

            Type type = new TypeToken<List<Flight>>() {}.getType();
            List<Flight> flights = gson.fromJson( storedFlightsJson, type );
            return Single.just( flights == null ? new ArrayList<>() : flights );
        } );
    }

    @Override
    public long getLastUpdateTime() {
        return prefs.getLong( KEY_LAST_UPDATE, 0L );
    }

    @Override
    public void clearStoredData() {
        prefs.edit().clear().apply();
    }
}
