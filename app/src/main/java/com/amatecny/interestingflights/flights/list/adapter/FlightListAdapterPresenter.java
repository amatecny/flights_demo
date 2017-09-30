package com.amatecny.interestingflights.flights.list.adapter;

import android.support.annotation.NonNull;

import com.amatecny.interestingflights.flights.model.Flight;
import com.amatecny.interestingflights.mvp.adapter.recycler.AbstractMvpRecyclerPresenter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by amatecny on 26/09/2017
 */
public class FlightListAdapterPresenter extends AbstractMvpRecyclerPresenter<Flight, FlightListAdapter, FlightListAdapter.FlightItemViewHolder> {

    private static final DateFormat DETAIL_DATE_FORMAT = new SimpleDateFormat( "EEE, MMM dd, yyyy HH:mm a", Locale.US );
    private static final String IMAGE_HOST_URL_FORMAT = "https://images.kiwi.com/photos/600/%s.jpg";

    public FlightListAdapterPresenter( @NonNull List<Flight> data, @NonNull FlightListAdapter adapterView ) {
        super( data, adapterView );
    }

    @Override
    public void bindViewHolder( FlightListAdapter.FlightItemViewHolder holder, int position ) {
        Flight flight = getItem( position );

        //times will in device default timeZone, as destination's time zone is not easily obtainable


        String departureDateString = formatTime( flight.departureTime() );
        String arrivalDateString = formatTime( flight.arrivalTime() );

        //display various flight details
        holder.displayFlightDetails(
                flight.cityFrom(),
                flight.cityTo(),
                flight.countryTo().getCode(),
                flight.price(),
                departureDateString,
                flight.flightDuration(),
                arrivalDateString,
                String.format( IMAGE_HOST_URL_FORMAT, flight.mapIdto() ) );
    }


    private String formatTime( long timeInSeconds ) {
        //convert to milliseconds first
        Date departure = new Date( timeInSeconds * 1000 );
        return DETAIL_DATE_FORMAT.format( departure );
    }

    @Override
    public void unbindViewHolder( FlightListAdapter.FlightItemViewHolder holder ) {
        //do nothing
    }
}
