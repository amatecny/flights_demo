package com.amatecny.interestingflights.mvp.adapter.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.amatecny.interestingflights.mvp.adapter.MvpAdapter;

import java.util.List;

/**
 * Represents MVP implementation of {@link BaseAdapter} used in {@link android.widget.ListView} or {@link android.widget.GridView}
 *
 * <T> type of the model used
 * <p>
 * Created by amatecny on 15/03/2017
 */
public abstract class AbstractMvpBaseAdapter<T, P extends AbstractMvpBaseAdapterPresenter> extends BaseAdapter implements MvpAdapter {

    protected LayoutInflater mInflater;
    protected P mDataPresenter;

    public AbstractMvpBaseAdapter( @NonNull Context context, @NonNull List<T> data ) {
        mInflater = LayoutInflater.from( context );
        setDataPresenter( createPresenter( data ) );
    }

    @Override
    public int getCount() {
        return mDataPresenter.getItemCount();
    }

    /**
     * Default implementation returns position. If items can be moved, extending class should return stable id instead
     */
    @Override
    public long getItemId( int position ) {
        return position;
    }


    /**
     * DataPresenter is responsible for all manipulation with underlying data.
     *
     * @return dataPresenter responsible for manipulation with data
     */
    public P getAdapterPresenter() {
        return mDataPresenter;
    }

    protected void setDataPresenter( @NonNull P dataPresenter ) {
        mDataPresenter = dataPresenter;
    }

    /**
     * @return presenter instance for this adapter
     */
    @NonNull
    protected abstract P createPresenter( List<T> data );

    //methods called from presenter

    @Override
    public void onDataSetChanged() {
        notifyDataSetChanged();
    }

}
