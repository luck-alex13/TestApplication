package com.android.testapplication.adapters;

import android.content.Context;

import com.android.testapplication.dataModels.GalleryModel;

import io.realm.RealmResults;

public class RealmGalleryAdapter extends RealmModelAdapter<GalleryModel> {

    public RealmGalleryAdapter(Context context, RealmResults<GalleryModel> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}