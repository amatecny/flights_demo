package com.amatecny.interestingflights.flights.list;

import android.content.Intent;
import android.net.Uri;
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
import io.reactivex.schedulers.Schedulers;
import me.xuender.unidecode.Unidecode;
import timber.log.Timber;

/**
 * Class responsible for loading interesting flights from storage/network api if stale.
 * <p>
 * Created by amatecny on 25/09/2017
 */
class FlightListPresenter extends BaseMvpPresenter<FlightListContract.View> implements FlightListContract.Presenter {

    /**
     * Designed to be formatted with 2 parameters - cityTo and cityFrom in format "city-country"
     */
    private static final String WEB_SEARCH_URL_BASE = "https://www.kiwi.com/us/search/%1$s/%2$s";

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

        view.getItemClickObservable()
                .compose( storeDisposable() )
                .subscribe( this::onFlightItemClicked );
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
            upToDateFlights = getStoredFlights()
                    .subscribeOn( Schedulers.io() )
                    .doOnError( throwable -> {
                        Timber.d( throwable );
                        //clear storage, as cached data appears to hold some invalid data, reload them next time from web
                        storage.clearStoredData();
                    } );
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
                    .zipWith( getStoredFlights(), ( candidateFlights, previousFlights ) -> {
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

    private void onFlightItemClicked( int position ) {
        getStoredFlights()
                .subscribeOn( Schedulers.io() )
                .map( flights -> flights.get( position ) )
                .observeOn( AndroidSchedulers.mainThread() )
                .compose( storeSingleDisposable() )
                .subscribe( selectedFlight -> view.openIntent( createWebIntent( selectedFlight ) ) );
    }

    /**
     *  Create an intent which will point to a page displaying the information about the selected flight
     *
     *  opens the url in browser or kiwi app?
     */
    private Intent createWebIntent( Flight selectedFlight ) {
        //construct departure and destination identifiers "city-country"
        String from = selectedFlight.cityFrom().toLowerCase() + "-" + selectedFlight.countryFrom().getName().toLowerCase();
        String to = selectedFlight.cityTo().toLowerCase() + "-" + selectedFlight.countryTo().getName().toLowerCase();

        //intent for e.g. browser
        Intent intent = new Intent( Intent.ACTION_VIEW );

        //Unidecode - transliterate all unicode chars to US-ASCII, e.g. 'ó' to 'o'
        intent.setData( Uri.parse( String.format( Locale.US, WEB_SEARCH_URL_BASE, Unidecode.decode( from), Unidecode.decode( to) ) ) );

        return intent;
    }

    @NonNull
    private Single<List<Flight>> getStoredFlights() {
        return storage.retrieveFlights();
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

