package it.smasini.utility.library.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simone Masini on 05/07/2016.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {

    final protected Context mContext;
    final private int layoutId;
    private OnClickHandler mClickHandler;
    private View mEmptyView;
    protected OnSwapData<T> onSwapData;

    private List<T> viewModels = new ArrayList<>();

    public BaseAdapter(Context context, View emptyView, OnClickHandler clickHandler, int layoutId) {
        this.mContext = context;
        this.mClickHandler = clickHandler;
        this.mEmptyView = emptyView;
        this.layoutId = layoutId;
    }

    public BaseAdapter(Context context, int layoutId){
        this.mContext = context;
        this.layoutId = layoutId;
    }

    public BaseAdapter(Context context, View emptyView, int layoutId){
        this.mContext = context;
        this.layoutId = layoutId;
        this.mEmptyView = emptyView;
    }

    public BaseAdapter(Context context, OnClickHandler clickHandler, int layoutId){
        this.mContext = context;
        this.layoutId = layoutId;
        this.mClickHandler = clickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewGroup instanceof RecyclerView) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            view.setFocusable(true);
            return getViewHolder(view);
        }else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(BaseAdapter.ViewHolder holder, int position) {
        T viewModel = viewModels.get(position);
        onBindCustomViewHolder(holder, position, viewModel);
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    public void setOnClickHandler(OnClickHandler onClickHandler) {
        this.mClickHandler = onClickHandler;
    }

    public void setOnSwapData(OnSwapData<T> onSwapData) {
        this.onSwapData = onSwapData;
    }

    public abstract void onBindCustomViewHolder(ViewHolder viewHolder, int position, T viewModel);

    public abstract ViewHolder getViewHolder(View view);

    @Override
    public int getItemCount() {
        if(viewModels==null)
            return 0;
        return viewModels.size();
    }

    public void swapData(List<T> newList){
        viewModels = newList;
        notifyDataSetChanged();
        if(mEmptyView!=null)
            mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
        if(onSwapData!=null)
            onSwapData.onSwap(newList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mClickHandler!=null) {
                int adapterPosition = getAdapterPosition();
                T viewModel = viewModels.get(adapterPosition);
                mClickHandler.onClick(viewModel);
            }
        }
    }
    public interface OnClickHandler<T> {
        void onClick(Object model);
    }

    public interface OnSwapData<T> {
        void onSwap(List<T> list);
    }
}
