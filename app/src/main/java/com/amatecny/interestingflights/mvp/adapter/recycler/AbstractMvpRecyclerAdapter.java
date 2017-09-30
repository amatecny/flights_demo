package com.amatecny.interestingflights.mvp.adapter.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

/**
 * Extension of {@link android.support.v7.widget.RecyclerView.Adapter} implementing {@link MvpRecyclerAdapter}
 *
 * @param <VH> viewholder class to be backed by presenter.
 * @param <P>  backing presenter.
 * @see AbstractMvpRecyclerPresenter
 * <p>
 * Created by amatecny on 20/02/2017
 */
public abstract class AbstractMvpRecyclerAdapter<VH extends MvpViewHolder, P extends AbstractMvpRecyclerPresenter> extends RecyclerView.Adapter<VH> implements MvpRecyclerAdapter {

    private P mDataPresenter;
    protected final LayoutInflater mInflater;

    public AbstractMvpRecyclerAdapter( Context context ) {
        mInflater = LayoutInflater.from( context );
        //now we depend on setDataPresenter
    }

    @Override
    public int getItemCount() {
        checkDataPresenterSet();
        return mDataPresenter.getItemCount();
    }

    /**
     * DataPresenter is responsible for all manipulation with underlying data.
     *
     * @return dataPresenter responsible for manipulation with data
     */
    public P getAdapterPresenter() {
        checkDataPresenterSet();
        return mDataPresenter;
    }

    protected void setDataPresenter( @NonNull P dataPresenter ) {
        mDataPresenter = dataPresenter;

        checkDataPresenterSet();
    }

    //methods called from presenter

    @Override
    public void onDataSetChanged() {
        notifyDataSetChanged();
    }

    @Override
    public void onItemAdded( int where ) {
        notifyItemInserted( where );
    }

    @Override
    public void onItemRemoved( int where ) {
        notifyItemRemoved( where );
    }

    @Override
    public void onItemUpdated( int where ) {
        notifyItemChanged( where );
    }

    private void checkDataPresenterSet() {
        if ( mDataPresenter == null ) {
            throw new NullPointerException( "dataPresenter is null, did you forget to initialize it?" );
        }
    }

}
