package com.amatecny.interestingflights.mvp.adapter;

/**
 * The generic presenter interface for a work with adapters
 * <p>
 * Created by amatecny on 15/03/2017
 */
public interface MvpAdapterPresenter<T extends MvpAdapter> {

    T getAdapter();

    void setAdapter( T adapter );

    int getItemCount();

}
