package com.amatecny.interestingflights.mvp.adapter.base;


import com.amatecny.interestingflights.mvp.adapter.MvpAdapter;
import com.amatecny.interestingflights.mvp.adapter.MvpAdapterPresenter;
import com.amatecny.interestingflights.mvp.adapter.recycler.MvpViewHolder;

/**
 * Presenter for {@link android.widget.BaseAdapter}.
 * Although, {@link android.support.v7.widget.RecyclerView.ViewHolder} is not usually used with {@link android.widget.ListView}
 * and {@link android.widget.GridView}, it fits here perfectly providing exactly the same job as in Recycler.Adapters.
 * Usage of MvpViewHolder also improves the code consistency of various adapter implementations
 * <p>
 * Created by amatecny on 15/03/2017
 */
public interface MvpBaseAdapterPresenter<T extends MvpAdapter, V extends MvpViewHolder> extends MvpAdapterPresenter<T> {

    /**
     * Binds provided holder with data stored at provided position
     */
    void bindViewHolder( V holder, int position );
}
