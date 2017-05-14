package com.android.testapplication.adapters;

import android.app.FragmentManager;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.testapplication.MyApp;
import com.android.testapplication.R;
import com.android.testapplication.dataModels.MyDataModel;
import com.android.testapplication.io_package.Constants;
import com.squareup.picasso.Callback;

import java.util.List;

/**
 * NewFitGid
 * Created by Александр on 26.07.2016.
 * Contact on luck.alex13@gmail.com
 * Copyright Aleksandr Novikov 2016
 */
public class GalleryRVAdapter extends RecyclerView.Adapter<GalleryRVAdapter.GalleryViewHolder> {

    private static final String LOG_TAG = "LOG_TAG_RVA";
    private final List<MyDataModel> restoransList;
    private String currentDate;
    FragmentManager fragmentManager;

    public GalleryRVAdapter(List<MyDataModel> list) {
        this.restoransList = list;
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ProgressBar progressView;
        private final ViewGroup parent;
        private View descriptionView;
        private TextView showDescrTV;
        private ImageView imageView;
        private MyDataModel dataModel;

        public GalleryViewHolder(final View itemView, final ViewGroup viewGroup) {
            super(itemView);
            parent = viewGroup;
            showDescrTV = (TextView) itemView.findViewById(R.id.image_descr_tv);
            imageView = (ImageView) itemView.findViewById(R.id.image_view) ;
            progressView = (ProgressBar) itemView.findViewById(R.id.progressBarCads);

        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card, parent, false);
        return new GalleryViewHolder(view, parent);
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, int pos) {
        holder.dataModel = restoransList.get(pos);
        holder.showDescrTV.setText(restoransList.get(pos).getAuthor());
        
        MyApp.getInstance().getPicassoInstance()
                .load(Constants.BASE_URL + restoransList.get(pos).getFilename())
                .fit()
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressView.setVisibility(View.GONE);
                        //Log.d(LOG_TAG, " onSuccess");
                    }

                    @Override
                    public void onError() {
                        holder.progressView.setVisibility(View.GONE);
                        Log.d(LOG_TAG, " onError");
                    }
                });
    }

    @Override
    public int getItemCount() {
        return restoransList.size();
    }

    public MyDataModel getItem(int pos) {
        return restoransList.get(pos);
    }
}
