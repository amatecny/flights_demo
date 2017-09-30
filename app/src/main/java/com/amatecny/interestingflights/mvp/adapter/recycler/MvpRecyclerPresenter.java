package com.amatecny.interestingflights.mvp.adapter.recycler;


import com.amatecny.interestingflights.mvp.adapter.base.MvpBaseAdapterPresenter;

/**
 * Presenter to be used with {@link MvpRecyclerAdapter}
 * <p>
 * Created by amatecny on 17/01/2017.
 */
public interface MvpRecyclerPresenter<T extends MvpRecyclerAdapter, V extends MvpViewHolder> extends MvpBaseAdapterPresenter<T, V> {

    void unbindViewHolder( V holder );
}
