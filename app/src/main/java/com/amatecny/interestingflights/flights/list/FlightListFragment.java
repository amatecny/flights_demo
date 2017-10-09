package com.amatecny.interestingflights.flights.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.amatecny.interestingflights.R;
import com.amatecny.interestingflights.flights.list.adapter.FlightListAdapter;
import com.amatecny.interestingflights.flights.model.Flight;
import com.amatecny.interestingflights.mvp.view.MvpFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * Holds the ui related stuff of a list of flights. Error is handled bys Snackbar
 * <p>
 * Created by amatecny on 25/09/2017
 */
public class FlightListFragment extends MvpFragment<FlightListContract.Presenter> implements FlightListContract.View {

    @BindView(R.id.flights_recycler) RecyclerView recycler;
    @BindView(R.id.flight_progress_bar) ProgressBar progressBar;
    private FlightListAdapter adapter;

    @Override
    public void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        //preparation for config changes, not fully used in this occasion
        setRetainInstance( true );
    }

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View root = inflater.inflate( R.layout.fragment_flight_list, container, false );
        ButterKnife.bind( this, root );

        //initiate the adapter before viewCreated is called on presenter
        adapter = new FlightListAdapter( getContext(), null );

        return root;
    }

    @Override
    public void onViewCreated( View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        recycler.setLayoutManager( new LinearLayoutManager( view.getContext() ) );
        recycler.setAdapter( adapter );
    }

    @Override
    public void displayFlights( List<Flight> flights ) {
        adapter.getAdapterPresenter().setData( flights );
    }

    @Override
    public void displayLoadingIndicator() {
        progressBar.setVisibility( View.VISIBLE );
    }

    @Override
    public void hideProgressIndicator() {
        progressBar.setVisibility( View.GONE );
    }

    @Override
    public void displayDownloadingFailed() {
        Snackbar.make( recycler, R.string.downloading_error, Snackbar.LENGTH_INDEFINITE )
                .setAction( R.string.retry_download, view -> presenter.retryDownloadClicked() )
                .show();
    }

    @Override
    public void openIntent( Intent selectedFlightIntent ) {
        startActivity( selectedFlightIntent );
    }

    @Override
    public Observable<Integer> getItemClickObservable() {
        return adapter.getOnClickObservable();
    }
}
