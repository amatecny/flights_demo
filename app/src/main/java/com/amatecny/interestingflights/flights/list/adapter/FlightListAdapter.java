package com.amatecny.interestingflights.flights.list.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amatecny.interestingflights.R;
import com.amatecny.interestingflights.flights.model.Flight;
import com.amatecny.interestingflights.mvp.adapter.recycler.AbstractMvpRecyclerListAdapter;
import com.amatecny.interestingflights.mvp.adapter.recycler.MvpViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter responsible for gluing {@link FlightListAdapterPresenter} and {@link FlightItemViewHolder}
 * <p>
 * Created by amatecny on 26/09/2017
 */
public class FlightListAdapter extends AbstractMvpRecyclerListAdapter<Flight, FlightListAdapter.FlightItemViewHolder, FlightListAdapterPresenter> {

    public FlightListAdapter( Context context, List<Flight> data ) {
        super( context, data );
    }

    @Override
    protected FlightListAdapterPresenter createPresenter( List<Flight> data ) {
        return new FlightListAdapterPresenter( data, this );
    }

    @Override
    public FlightItemViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        //only one type of view(holder)
        return new FlightItemViewHolder( mInflater.inflate( R.layout.view_flight_item, parent, false ) );
    }

    @Override
    public void onBindViewHolder( FlightItemViewHolder holder, int position ) {
        getAdapterPresenter().bindViewHolder( holder, position );

    }
    /**
     * Single flight representation in a list/recycler
     */
    static class FlightItemViewHolder extends MvpViewHolder {

        @BindView(R.id.flight_detail_price_text) TextView priceText;
        @BindView(R.id.flight_detail_from_to_text) TextView routeText;
        @BindView(R.id.flight_detail_departure_date_text) TextView departureText;
        @BindView(R.id.flight_detail_duration_date_text) TextView durationText;
        @BindView(R.id.flight_detail_arrival_date_text) TextView arrivalText;

        @BindView(R.id.flight_detail_destination_image) ImageView destinationImage;

        FlightItemViewHolder( View itemView ) {
            super( itemView );
            ButterKnife.bind( this, itemView );
        }

        void displayFlightDetails( String cityFrom, String cityTo, String countryTo, String price, String departureTime, String duration, String arrivalTime, String destinationImageUrl ) {
            priceText.setText( String.format( getContext().getString( R.string.flight_price ), price ) );
            routeText.setText( String.format( getContext().getString( R.string.flight_route ), cityFrom, cityTo, countryTo ) );
            departureText.setText( departureTime );
            durationText.setText( duration );
            arrivalText.setText( arrivalTime );

            //load the image from url, use placeholder in the meantime and for cases when an image does not exist
            Picasso.with( getContext() ).load( destinationImageUrl )
                    .placeholder( R.drawable.city_outline )
                    .into( destinationImage );
        }
    }

}
