package com.amatecny.interestingflights.flights.list;


import android.support.test.InstrumentationRegistry;

import com.amatecny.interestingflights.ImmediateSchedulersRule;
import com.amatecny.interestingflights.flights.model.Flight;
import com.amatecny.interestingflights.network.Api;
import com.amatecny.interestingflights.network.model.FlightsResponse;
import com.amatecny.interestingflights.persistence.Storage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class FlightListPresenterTest {

    /**
     * Make all threads run on current thread
     */
    @Rule
    public final ImmediateSchedulersRule schedulers = new ImmediateSchedulersRule();

    @Mock
    private FlightListContract.View mockView;

    @Mock
    private Api mockApi;

    @Mock
    private Storage mockStorage;

    private FlightListPresenter uut;

    @Before
    public void setUp() {
        uut = new FlightListPresenter( mockView, mockApi, mockStorage );
    }

    @Test
    public void testDownloadFlightsSuccessful() throws Exception {
        //prepare mock data
        List<Flight> mockFlights = prepareOrigMockFlights();

        //mock the chain of calls
        FlightsResponse mockFlightsResponse = mock( FlightsResponse.class );
        when( mockFlightsResponse.flights() ).thenReturn( mockFlights );

        //can't mock this one....
        Response<FlightsResponse> response = Response.success( mockFlightsResponse );

        //nothing in storage
        when( mockStorage.getLastUpdateTime() ).thenReturn( 0L );
        when( mockStorage.retrieveFlights() ).thenReturn( Single.just( Collections.emptyList() ) );

        when( mockApi.downloadFlights( any(), any(), anyInt(), anyInt() ) ).thenReturn( Single.just( response ) );

        uut.viewCreated();

        verify( mockView ).displayLoadingIndicator();

        //wait for async rx calls to finish
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        //download flights called first with our mocked response
        verify( mockApi ).downloadFlights( any(), any(), anyInt(), anyInt() );

        //mocked response send to storage
        verify( mockStorage ).storeFlights( mockFlights );

        //and to view, finished loading
        verify( mockView ).hideProgressIndicator();
        verify( mockView ).displayFlights( mockFlights );
    }

    @SuppressWarnings("unchecked") // raw types of List :(
    @Test
    public void testDownloadRepeatingFlightsFiltering() throws Exception {
        //prepare mock data
        List<Flight> mockFlights = prepareOrigMockFlights();
        List<Flight> candidateFlights = prepareRepeatingMockFlights();

        //mock the chain of calls
        FlightsResponse mockFlightsResponse = mock( FlightsResponse.class );
        when( mockFlightsResponse.flights() ).thenReturn( candidateFlights );

        //can't mock this one....
        Response<FlightsResponse> response = Response.success( mockFlightsResponse );

        //stale data
        when( mockStorage.getLastUpdateTime() ).thenReturn( 0L );
        when( mockStorage.retrieveFlights() ).thenReturn( Single.just( mockFlights ) );
        //our mocked response
        when( mockApi.downloadFlights( any(), any(), anyInt(), anyInt() ) ).thenReturn( Single.just( response ) );

        uut.viewCreated();

        //wait for async rx calls to finish
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass( List.class );
        verify( mockView ).displayFlights( argumentCaptor.capture() );
        List<Flight> capturedFlights = argumentCaptor.getValue();

        assertThat( capturedFlights ).doesNotHaveDuplicates();
        assertThat( capturedFlights ).doesNotContainAnyElementsOf( mockFlights );
        assertThat( capturedFlights ).containsExactly( candidateFlights.get( 5 ), candidateFlights.get( 6 ), candidateFlights.get( 7 ), candidateFlights.get( 8 ), candidateFlights.get( 9 ) );

    }

    @Test
    public void testDownloadFailure() throws Exception {
        // no stored data
        when( mockStorage.getLastUpdateTime() ).thenReturn( 0L );
        when( mockStorage.retrieveFlights() ).thenReturn( Single.just( Collections.emptyList() ) );

        //do the request, but this time with exception
        Response<FlightsResponse> response = Response.error( 400, ResponseBody.create( MediaType.parse("application/json; charset=utf-8"), "{}" ) );
        when( mockApi.downloadFlights( any(), any(), anyInt(), anyInt() ) ).thenReturn( Single.just( response ) );

        uut.viewCreated();

        //wait for async rx calls to finish
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        verify( mockView ).hideProgressIndicator();
        verify( mockView ).displayDownloadingFailed();

    }

    @Test
    public void testRetrieveFromStorage() {
        List<Flight> mockFlights = prepareOrigMockFlights();

        //return now
        when( mockStorage.getLastUpdateTime() ).thenReturn( Calendar.getInstance().getTimeInMillis() );
        //flights from storage
        when( mockStorage.retrieveFlights() ).thenReturn( Single.just( mockFlights ) );

        uut.viewCreated();

        verify( mockView ).displayLoadingIndicator();

        //wait for async rx calls to finish
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        //mockFLights are displayed correctly
        verify( mockView ).hideProgressIndicator();
        verify( mockView ).displayFlights( mockFlights );
    }


    private static List<Flight> prepareOrigMockFlights() {
        Flight mockFlight1 = mock( Flight.class );
        when( mockFlight1.cityTo() ).thenReturn( "cityTo1" );
        Flight mockFlight2 = mock( Flight.class );
        when( mockFlight2.cityTo() ).thenReturn( "cityTo2" );
        Flight mockFlight3 = mock( Flight.class );
        when( mockFlight3.cityTo() ).thenReturn( "cityTo3" );
        Flight mockFlight4 = mock( Flight.class );
        when( mockFlight4.cityTo() ).thenReturn( "cityTo4" );
        Flight mockFlight5 = mock( Flight.class );
        when( mockFlight5.cityTo() ).thenReturn( "cityTo5" );

        List<Flight> mockFlights = new ArrayList<>();
        mockFlights.add( mockFlight1 );
        mockFlights.add( mockFlight2 );
        mockFlights.add( mockFlight3 );
        mockFlights.add( mockFlight4 );
        mockFlights.add( mockFlight5 );
        return mockFlights;
    }

    private static List<Flight> prepareRepeatingMockFlights() {
        Flight mockFlight1 = mock( Flight.class );
        when( mockFlight1.cityTo() ).thenReturn( "cityTo1" );
        Flight mockFlight2 = mock( Flight.class );
        when( mockFlight2.cityTo() ).thenReturn( "cityTo2" );
        Flight mockFlight3 = mock( Flight.class );
        when( mockFlight3.cityTo() ).thenReturn( "cityTo3" );
        Flight mockFlight4 = mock( Flight.class );
        when( mockFlight4.cityTo() ).thenReturn( "cityTo4" );
        Flight mockFlight5 = mock( Flight.class );
        when( mockFlight5.cityTo() ).thenReturn( "cityTo5" );
        Flight mockFlight6 = mock( Flight.class );
        when( mockFlight6.cityTo() ).thenReturn( "cityTo8" );
        Flight mockFlight7 = mock( Flight.class );
        when( mockFlight7.cityTo() ).thenReturn( "cityTo9" );
        Flight mockFlight8 = mock( Flight.class );
        when( mockFlight8.cityTo() ).thenReturn( "cityTo10" );
        Flight mockFlight9 = mock( Flight.class );
        when( mockFlight9.cityTo() ).thenReturn( "cityTo11" );
        Flight mockFlight10 = mock( Flight.class );
        when( mockFlight10.cityTo() ).thenReturn( "cityTo12" );

        List<Flight> mockFlights = new ArrayList<>();
        mockFlights.add( mockFlight1 );
        mockFlights.add( mockFlight2 );
        mockFlights.add( mockFlight3 );
        mockFlights.add( mockFlight4 );
        mockFlights.add( mockFlight5 );
        mockFlights.add( mockFlight6 );
        mockFlights.add( mockFlight7 );
        mockFlights.add( mockFlight8 );
        mockFlights.add( mockFlight9 );
        mockFlights.add( mockFlight10 );
        return mockFlights;
    }


}