package com.amatecny.interestingflights.flights.model;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Object representing a single Flight
 * <p>
 * Created by amatecny on 26/09/2017
 */
@AutoValue
public abstract class Flight implements Parcelable{

    public static Flight create( String id, String mapIdfrom, String mapIdto, String flightDuration, String cityTo, Country countryTo, String cityFrom, long departureTime,long arrivalTime, String price ) {
        return new AutoValue_Flight( id, mapIdfrom, mapIdto, flightDuration, cityTo, countryTo, cityFrom, departureTime, arrivalTime, price );
    }

    public static TypeAdapter<Flight> typeAdapter( Gson gson ) {
        return new AutoValue_Flight.GsonTypeAdapter( gson );
    }

    @NonNull
    @SerializedName("id")
    public abstract String id();

    /**
     * ID of the city of departure
     */
    @NonNull
    @SerializedName("mapIdfrom")
    public abstract String mapIdfrom();

    /**
     * ID of the city of arrival
     */
    @NonNull
    @SerializedName("mapIdto")
    public abstract String mapIdto();

    /**
     * Pre-formatted duration of the flight
     */
    @NonNull
    @SerializedName("fly_duration")
    public abstract String flightDuration();

    /**
     * Destination city
     */
    @NonNull
    @SerializedName("cityTo")
    public abstract String cityTo();

    /**
     * Destination country
     */
    @NonNull
    @SerializedName("countryTo")
    public abstract Country countryTo();

    /**
     * Departure city
     */
    @NonNull
    @SerializedName("cityFrom")
    public abstract String cityFrom();

    /**
     * Time of the departure in millis since the start of epoch
     */
    @SerializedName("dTimeUTC")
    public abstract long departureTime();

    /**
     * Time of the arrival to destination in millis since the start of epoch
     */
    @SerializedName("aTimeUTC")
    public abstract long arrivalTime();

    /**
     * Price in EUR
     */
    @NonNull
    @SerializedName("price")
    public abstract String price();

}
