package com.amatecny.interestingflights.flights.list.adapter;

import com.amatecny.interestingflights.ImmediateSchedulersRule;
import com.amatecny.interestingflights.flights.model.Country;
import com.amatecny.interestingflights.flights.model.Flight;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class FlightListAdapterPresenterTest {

    /**
     * Make all threads run on current thread
     */
    @Rule
    public final ImmediateSchedulersRule schedulers = new ImmediateSchedulersRule();

    @Mock
    private FlightListAdapter mockAdapter;

    private FlightListAdapterPresenter uut;

    @Before
    public void setUp() {
        List<Flight> data = prepareFlights();
        uut = new FlightListAdapterPresenter( data, mockAdapter );
    }

    //pointless
    @Test
    public void testBindViewHolder() throws Exception {
        FlightListAdapter.FlightItemViewHolder mockHolder = mock( FlightListAdapter.FlightItemViewHolder.class );
        uut.bindViewHolder( mockHolder, 0 );

        verify( mockHolder ).displayFlightDetails( anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString() );
        verify( mockHolder ).setOnClickListener( any() );
    }

    public static List<Flight> prepareFlights() {
        Flight flight = Flight.create( "id", "mapIdFrom", "mapIdTo", "2h30m", "cityTo", Country.create( "CO", "Country" ), "cityFrom", Country.create( "CO2", "Country2" ), 15000000, 15000001, "119" );
        List<Flight> mockFlights = new ArrayList<>();
        mockFlights.add( flight );
        return mockFlights;
    }

}