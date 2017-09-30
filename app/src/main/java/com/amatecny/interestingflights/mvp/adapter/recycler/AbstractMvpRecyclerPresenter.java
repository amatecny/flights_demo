package com.amatecny.interestingflights.mvp.adapter.recycler;

import android.support.annotation.NonNull;

import com.amatecny.interestingflights.mvp.adapter.base.AbstractMvpBaseAdapterPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for {@link AbstractMvpRecyclerAdapter} and {@link android.support.v7.widget.RecyclerView.Adapter}
 * It allows to perform basic CRUD operations on a single list of data
 *
 * <M> the model class to be used with this presenter
 *
 * Created by amatecny on 16/02/2017.
 */
public abstract class AbstractMvpRecyclerPresenter<M, T extends MvpRecyclerAdapter, V extends MvpViewHolder>
        extends AbstractMvpBaseAdapterPresenter<M, T, V>
        implements MvpRecyclerPresenter<T, V> {

    public AbstractMvpRecyclerPresenter( @NonNull List<M> data, @NonNull T adapterView ) {
        super( data, adapterView );
    }

    /**
     * Safely adds new item, updates observers
     */
    public void addItem( @NonNull M item ) {
        if ( mData == null ) {
            mData = new ArrayList<>();
        }
        mData.add( item );

        getAdapter().onItemAdded( mData.size() - 1 );
    }

    /**
     * Safely removes an item, updates observers
     */
    public void removeItem( @NonNull M item ) {
        if ( mData != null ) {
            int position = mData.indexOf( item );
            if ( position >= 0 ) {
                mData.remove( item );
                getAdapter().onItemRemoved( position );
            }
        }
    }

    /**
     * Safely updates an item, updates observers
     *
     * @return true if an item was updated
     */
    public boolean updateItem( @NonNull M item ) {
        if ( mData != null ) {
            int position = mData.indexOf( item );
            if ( position >= 0 ) {
                mData.set( position, item );
                getAdapter().onItemUpdated( position );
                return true;
            }
        }

        return false;
    }

    /**
     * Safely updates or adds an item, updates observers
     */
    public void addOrUpdateItem( @NonNull M item ) {
        boolean updated = updateItem( item );
        if ( !updated ) {
            addItem( item );
        }
    }
}
