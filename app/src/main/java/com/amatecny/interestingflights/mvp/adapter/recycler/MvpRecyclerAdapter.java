package com.amatecny.interestingflights.mvp.adapter.recycler;

import com.amatecny.interestingflights.mvp.adapter.MvpAdapter;

/**
 * Represents MVP adapter for {@link android.support.v7.widget.RecyclerView}
 * Created by amatecny on 17/02/2017.
 */
public interface MvpRecyclerAdapter extends MvpAdapter {

    void onItemAdded( int where );

    void onItemRemoved( int where );

    void onItemUpdated( int where );

}
