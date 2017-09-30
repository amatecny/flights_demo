package com.amatecny.interestingflights.mvp.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amatecny.interestingflights.mvp.view.MvpView;


/**
 * This class represents mvp implementation of {@link android.support.v7.widget.RecyclerView.ViewHolder}
 * <p>
 * Created by amatecny on 17/02/2017.
 */
public class MvpViewHolder extends RecyclerView.ViewHolder implements MvpView {

    public MvpViewHolder( View itemView ) {
        super( itemView );
    }

    @Override
    public Context getContext() {
        return itemView.getContext();
    }
}
