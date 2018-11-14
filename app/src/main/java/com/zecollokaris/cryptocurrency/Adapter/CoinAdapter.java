package com.zecollokaris.cryptocurrency.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zecollokaris.cryptocurrency.R;
import com.squareup.picasso.Picasso;
import com.zecollokaris.cryptocurrency.Interface.ILoadMore;
import com.zecollokaris.cryptocurrency.Model.CoinModel;


import java.util.List;

//THIS WILL BE USED TO INFLATE THE DISPLAY!
public class CoinAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ILoadMore iLoadMore;
    boolean isLoading;
    Activity activity;
    List<CoinModel> items;

    int visibleThreshold = 5, lastVisibleItem,totalItemCount;

    public CoinAdapter(RecyclerView recyclerView,Activity activity, List<CoinModel> items) {
        this.activity = activity;
        this.items = items;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem+visibleThreshold)){
                    if(iLoadMore != null)
                        iLoadMore.onLoadMore();
                    isLoading =true;
                }
            }
        });
    }


    public void setiLoadMore(ILoadMore iLoadMore) {
        this.iLoadMore = iLoadMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.coin_layout,viewGroup, false);
        return new CoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CoinModel item = items.get(position);
        CoinViewHolder holderItem = (CoinViewHolder)holder;

        holderItem.coin_name.setText(item.getName());
        holderItem.coin_symbol.setText(item.getSymbol());
        holderItem.coin_price.setText(item.getPrice_usd());
        holderItem.one_hour_change.setText(item.getPercentage_change_1h()+"%");
        holderItem.twenty_hours_change.setText(item.getPercentage_change_24h()+"%");
        holderItem.seven_days_change.setText(item.getPercentage_change_7d()+"%");

        //Load Images (Picasso)
        Picasso.with(activity)
                .load(new StringBuilder("https://res.cloudinary.com/dxi90ksom/image/upload/")
                .append(item.getSymbol().toLowerCase()).append(".png").toString())
                .into(holderItem.coin_icon);

        holderItem.one_hour_change.setTextColor(item.getPercentage_change_1h().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("32CD32"));
        holderItem.twenty_hours_change.setTextColor(item.getPercentage_change_1h().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("32CD32"));
        holderItem.seven_days_change.setTextColor(item.getPercentage_change_1h().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("32CD32"));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLoaded() {isLoading = true;}

    public void updateData(List<CoinModel> coinModels){

        this.items = coinModels;
        notifyDataSetChanged();
    }
}