package com.amatecny.interestingflights.flights.list;

import android.support.annotation.NonNull;

import com.amatecny.interestingflights.flights.model.Flight;
import com.amatecny.interestingflights.mvp.presenter.BaseMvpPresenter;
import com.amatecny.interestingflights.network.Api;
import com.amatecny.interestingflights.network.model.FlightsResponse;
import com.amatecny.interestingflights.persistence.Storage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Class responsible for loading interesting flights from storage/network api if stale.
 * <p>
 * Created by amatecny on 25/09/2017
 */
class FlightListPresenter extends BaseMvpPresenter<FlightListContract.View> implements FlightListContract.Presenter {

    // Create a DateFormatter for day comparison of timestamps
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat( "dd/MM/yyyy", Locale.US );
    private static final int NUMBER_OF_DEALS_PER_DAY = 5;

    private Api networkApi;
    private Storage storage;

    FlightListPresenter( @NonNull FlightListContract.View view, Api networkApi, Storage storage ) {
        super( view );
        this.networkApi = networkApi;
        this.storage = storage;
    }

    @Override
    public void viewCreated() {
        getAndDisplayFlightOffers();
    }

    @Override
    public void retryDownloadClicked() {
        getAndDisplayFlightOffers();
    }

    private void getAndDisplayFlightOffers() {
        //were flights already updated today?
        long currentTime = Calendar.getInstance().getTimeInMillis();
        String currentDate = getFormattedDateString( currentTime );
        String updateDate = getFormattedDateString( storage.getLastUpdateTime() );

        //start loading indication
        view.displayLoadingIndicator();

        Single<List<Flight>> upToDateFlights;
        if ( currentDate.equals( updateDate ) ) {
            //reuse what is stored
            upToDateFlights = storage.retrieveFlights();
        } else {
            //download and process new flights
            //extra items to find different locations from previous call, little bit of salt
            upToDateFlights = networkApi.downloadFlights( getTomorrowsDate(), getDateOneMonthFromNow(), 10, ( int ) ( currentTime % 10 ) )
                    .observeOn( AndroidSchedulers.mainThread() )
                    .flatMap( flightsResponse -> {  //open the response
                        if ( flightsResponse.isSuccessful() ) {
                            FlightsResponse body = flightsResponse.body();
                            if ( body != null ) {
                                return Single.just( body.flights() );
                            }
                        }
                        //signal the error and skip further processing
                        return Single.error( new RuntimeException( flightsResponse.message() ) );
                    } )
                    .zipWith( storage.retrieveFlights(), ( candidateFlights, previousFlights ) -> {
                        List<Flight> newFlights = new ArrayList<>();

                        //start filtering individual items based on destination, find 5 flights not present in previous flights and send them down
                        Iterator<Flight> candidateFlightsIterator = candidateFlights.iterator();
                        while ( newFlights.size() < NUMBER_OF_DEALS_PER_DAY && candidateFlightsIterator.hasNext() ) {
                            Flight candidateFlight = candidateFlightsIterator.next();

                            boolean isFlightRepeating = false;
                            for ( Flight previousFlight : previousFlights ) {
                                if ( candidateFlight.cityTo().equals( previousFlight.cityTo() ) ) {
                                    isFlightRepeating = true;
                                    break;
                                }
                            }

                            if ( !isFlightRepeating ) {
                                newFlights.add( candidateFlight );
                            }
                        }

                        return newFlights;
                    } )
                    .doOnSuccess( storage::storeFlights );
        }

        //now consume what the upToDateFlights has
        upToDateFlights
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( flights -> {
                            view.hideProgressIndicator();
                            view.displayFlights( flights );
                        },
                        throwable -> {
                            view.hideProgressIndicator();
                            view.displayDownloadingFailed();
                        } );
    }

    /**
     * Simply returns tomorrow's date
     */
    private static Date getTomorrowsDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add( Calendar.DAY_OF_YEAR, 1 );
        return calendar.getTime();
    }

    private static Date getDateOneMonthFromNow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add( Calendar.MONTH, 1 );
        return calendar.getTime();
    }

    /**
     * Return date in a format defined in {@link #DATE_FORMAT}
     *
     * @param milliSeconds Date in milliseconds
     * @return String representing date in specified format
     */
    private static String getFormattedDateString( long milliSeconds ) {
        // convert a calendar object from millis to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( milliSeconds );
        return DATE_FORMAT.format( calendar.getTime() );
    }
}

