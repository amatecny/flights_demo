package com.amatecny.interestingflights.network;

import com.amatecny.interestingflights.network.model.FlightsResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Common functionality of both real and mock implementations
 * <p>
 * Created by amatecny on 27/09/2017
 */
public abstract class Api {

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat( "dd/MM/yyyy", Locale.US );

    /**
     * Get the list of flights based on provided parameters.
     * Start location would be situated in a 250km radius around Brno. With a destination set to anywhere
     *
     * Already does this job on {@link Schedulers#io() I/O } thread.
     *
     * @param firstDateOfFLight first date of a range in which to search for flights
     * @param lastDateOfFLight last date of a range in which to search for flights
     * @param limit        number of result to be returned
     * @param offset       how many results should be skipped (from the beginning of the result)
     */
    public abstract Single<Response<FlightsResponse>> downloadFlights( Date firstDateOfFLight, Date lastDateOfFLight, int limit, int offset );

}
