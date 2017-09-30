package com.amatecny.interestingflights.mvp.adapter;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Base implementation of {@link MvpAdapterPresenter}
 * @param <M> the model class to be used with this presenter
 * <p>
 * Created by amatecny on 15/03/2017
 */
public abstract class AbstractMvpAdapterPresenter<M, T extends MvpAdapter> implements MvpAdapterPresenter<T> {

    protected List<M> mData;

    @NonNull
    private WeakReference<T> mAdapterView;

    public AbstractMvpAdapterPresenter( @NonNull List<M> data, @NonNull T adapterView ) {
        this.mAdapterView = new WeakReference<>( adapterView );
        mData = data;
    }

    @Override
    public T getAdapter() {
        return mAdapterView.get();
    }

    @Override
    public void setAdapter( T view ) {
        this.mAdapterView = new WeakReference<>( view );
    }

    /**
     * @return the overall number of items in the underlying data
     */
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @NonNull
    public M getItem( int position ) {
        if ( position < 0 || position > getItemCount() - 1 ) {
            throw new IllegalArgumentException( "Invalid position" );
        }

        return mData.get( position );
    }

    /**
     * Set dataset
     */
    public void setData( @NonNull List<M> data ) {
        mData = data;
        getAdapter().onDataSetChanged();
    }
}
