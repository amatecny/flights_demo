package com.amatecny.interestingflights.mvp.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Extension of {@link RecyclerView.Adapter} implementing {@link MvpRecyclerAdapter}.
 * This implementation helps to deal with creation of presenter from a list of data in a consistent way
 *
 * @param <M>  type of data.
 * @param <VH> viewholder class to be backed by presenter.
 * @param <P>  backing presenter.
 * @see AbstractMvpRecyclerPresenter
 * <p>
 * Created by amatecny on 20/02/2017
 */
public abstract class AbstractMvpRecyclerListAdapter<M, VH extends MvpViewHolder, P extends AbstractMvpRecyclerPresenter> extends AbstractMvpRecyclerAdapter<VH, P> {

    public AbstractMvpRecyclerListAdapter( Context context, List<M> data ) {
        super( context );
        setDataPresenter( createPresenter( data ) );
    }

    /**
     * @return presenter instance for this adapter
     */
    protected abstract P createPresenter( List<M> data );
}
