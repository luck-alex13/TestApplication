package com.android.testapplication.adapters;

import android.content.Context;

import com.android.testapplication.dataModels.GalleryModel;
import com.android.testapplication.dataModels.TempGallery;

import io.realm.RealmResults;

public class RealmGalleryAdapter extends RealmModelAdapter<TempGallery> {

    public RealmGalleryAdapter(Context context, RealmResults<TempGallery> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}