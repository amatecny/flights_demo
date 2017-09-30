package com.amatecny.interestingflights.flights.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a country of a city of arrival or departure
 * <p>
 * Created by amatecny on 28/09/2017
 */
@AutoValue
public abstract class Country implements Parcelable {

    public static Country create( String code, String name ) {
        return new AutoValue_Country( code, name );
    }

    public static TypeAdapter<Country> typeAdapter( Gson gson ) {
        return new AutoValue_Country.GsonTypeAdapter( gson );
    }

    @SerializedName("code")
    public abstract String getCode();

    @SerializedName("name")
    public abstract String getName();
}
