package com.android.testapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.testapplication.MyApp;
import com.android.testapplication.R;
import com.android.testapplication.dataModels.GalleryModel;
import com.android.testapplication.dataModels.TempGallery;
import com.android.testapplication.database.RealmController;
import com.android.testapplication.io_package.Constants;
import com.squareup.picasso.Callback;

import java.util.List;

import io.realm.Realm;

/**
 * NewFitGid
 * Created by Александр on 26.07.2016.
 * Contact on luck.alex13@gmail.com
 * Copyright Aleksandr Novikov 2016
 */
public class RaitingRVAdapter extends RealmRecyclerViewAdapter<TempGallery> {

    private static final String LOG_TAG = "LOG_TAG_RVA";
    private List<Object> adapterList;
    private Context context;
    private Realm realm;

    public RaitingRVAdapter(Context context, List<Object> list) {
        adapterList = list;
        this.context = context;
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ProgressBar progressView;
        private TextView showDescrTV;
        private ImageView imageView;
        private TempGallery dataModel;

        public GalleryViewHolder(final View itemView, final ViewGroup viewGroup) {
            super(itemView);


        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new GalleryViewHolder(view, parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

    }



    @Override
    public int getItemCount() {
        //return adapterList.size();

        return adapterList.size();
    }

}
