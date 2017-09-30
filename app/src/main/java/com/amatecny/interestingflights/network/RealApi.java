package com.amatecny.interestingflights.network;

import com.amatecny.interestingflights.network.model.FlightsResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Kiwi's real API
 * <p>
 * Created by amatecny on 27/09/2017
 */
class RealApi extends Api {

    private final RealFlightApiCalls flightCalls;

    private interface RealFlightApiCalls {

        @GET("flights")
        Observable<Response<FlightsResponse>> getFlights( @QueryMap Map<String, String> params );
    }

    RealApi( Retrofit retrofit ) {
        flightCalls = retrofit.create( RealFlightApiCalls.class );
    }

    @Override
    public Single<Response<FlightsResponse>> downloadFlights( Date firstDateOfFLight, Date lastDateOfFLight, int limit, int offset ) {

        Map<String, String> params = new HashMap<>();
        params.put( "v", "3" );
        params.put( "curr", "EUR" );
        params.put( "sort", "price" );
        params.put( "asc", "1" );
        params.put( "locale", "us" );
        params.put( "flyFrom", "49.2-16.61-250km" );
        params.put( "to", "anywhere" );
        params.put( "featureName", "aggregateResults" );
        params.put( "dateFrom", DATE_FORMAT.format( firstDateOfFLight ) ); //"27/09/2017"
        params.put( "dateTo", DATE_FORMAT.format( lastDateOfFLight ) );
        params.put( "typeFlight", "oneway" );
        params.put( "adults", "1" );
        params.put( "limit", String.valueOf( limit ) );
        params.put( "offset", String.valueOf( offset ) );

        return flightCalls.getFlights( params )
                .firstOrError()
                .subscribeOn( Schedulers.io() );
    }

}
