package com.amatecny.interestingflights.network.model;

import com.amatecny.interestingflights.flights.model.Flight;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amatecny on 28/09/2017
 */
@AutoValue
public abstract class FlightsResponse {

    public static FlightsResponse create( List<Flight> flights ) {
        return new AutoValue_FlightsResponse( flights );
    }

    public static TypeAdapter<FlightsResponse> typeAdapter( Gson gson ) {
        return new AutoValue_FlightsResponse.GsonTypeAdapter( gson );
    }

    @SerializedName("data")
    public abstract List<Flight> flights();
}
