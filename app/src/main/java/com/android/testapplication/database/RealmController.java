package com.android.testapplication.database;


import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.android.testapplication.dataModels.GalleryModel;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from GalleryModel.class
    public void clearAll() {

        realm.beginTransaction();
        realm.clear(GalleryModel.class);
        realm.commitTransaction();
    }

    //find all objects in the GalleryModel.class
    public RealmResults<GalleryModel> getGalleryModels() {

        return realm.where(GalleryModel.class).findAllSorted("filename", Sort.DESCENDING);
    }

    //query a single item with the given id
    public GalleryModel getGalleryModel(String id) {

        return realm.where(GalleryModel.class).equalTo("id", id).findFirst();
    }

    public void saveGalleryItem(GalleryModel galleryModel) {
        realm.beginTransaction();
        realm.copyToRealm(galleryModel);
        realm.commitTransaction();
    }

    //check if GalleryModel.class is empty
    public boolean hasGalleryModels() {

        return !realm.allObjects(GalleryModel.class).isEmpty();
    }


}
