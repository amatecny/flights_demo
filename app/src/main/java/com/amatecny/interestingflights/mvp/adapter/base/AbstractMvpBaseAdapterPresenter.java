package com.amatecny.interestingflights.mvp.adapter.base;

import android.support.annotation.NonNull;

import com.amatecny.interestingflights.mvp.adapter.AbstractMvpAdapterPresenter;
import com.amatecny.interestingflights.mvp.adapter.MvpAdapter;
import com.amatecny.interestingflights.mvp.adapter.recycler.MvpViewHolder;

import java.util.List;

/**
 * Abstract implementation of {@link MvpBaseAdapterPresenter}
 * <M> the model class to be used with this presenter
 * <p>
 * Created by amatecny on 15/03/2017
 */
public abstract class AbstractMvpBaseAdapterPresenter<M, T extends MvpAdapter, VH extends MvpViewHolder>
        extends AbstractMvpAdapterPresenter<M, T>
        implements MvpBaseAdapterPresenter<T, VH> {

    public AbstractMvpBaseAdapterPresenter( @NonNull List<M> data, @NonNull T adapterView ) {
        super( data, adapterView );
    }
}
