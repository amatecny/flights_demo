package com.amatecny.interestingflights.mvp.adapter;

import com.amatecny.interestingflights.mvp.adapter.base.AbstractMvpBaseAdapter;
import com.amatecny.interestingflights.mvp.adapter.recycler.AbstractMvpRecyclerAdapter;

/**
 * This interaface represents a view part in MVP of an generic adapter
 *
 * Sample implementations:
 * @see AbstractMvpBaseAdapter
 * @see AbstractMvpRecyclerAdapter
 * <p>
 * Created by amatecny on 15/03/2017
 */
public interface MvpAdapter {

    void onDataSetChanged();

}
