package com.amatecny.interestingflights.flights.list;

import com.amatecny.interestingflights.flights.model.Flight;
import com.amatecny.interestingflights.mvp.presenter.MvpPresenter;
import com.amatecny.interestingflights.mvp.view.MvpView;

import java.util.List;

/**
 * Contract between FlightListFragment and FlightListPresenter
 * <p>
 * Created by amatecny on 26/09/2017
 */
public interface FlightListContract {

    interface View extends MvpView {

        /**
         * Display the new flights
         */
        void displayFlights( List<Flight> flights );

        void displayLoadingIndicator();

        void hideProgressIndicator();

        /**
         * Display a message to a user explaining that there was a problem with downloading flight offers
         */
        void displayDownloadingFailed();
    }

    interface Presenter extends MvpPresenter<View> {

        /**
         * Called when user request to retry downloading of flights
         */
        void retryDownloadClicked();
    }

}
