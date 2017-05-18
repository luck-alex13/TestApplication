package com.android.testapplication.database;


import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.android.testapplication.dataModels.GalleryModel;
import com.android.testapplication.dataModels.TempGallery;

import java.util.Random;

import io.realm.Realm;
import io.realm.RealmObject;
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
    public void clearAll(Class aClass) {

        realm.beginTransaction();
        realm.clear(aClass);
        realm.commitTransaction();
    }

    //find all objects in the GalleryModel.class
    public RealmResults<GalleryModel> getGalleryModels() {
        return realm.where(GalleryModel.class).findAll();
    }

    public RealmResults<TempGallery> getTempGalleryModels() {
        return realm.where(TempGallery.class).findAll();
    }

    //query a single item with the given id
    public RealmObject getDataById(Class aClass, int id) {
        return realm.where(aClass).equalTo("id", id).findFirst();
    }

    public boolean isExist(Class aClass, int id){
        RealmObject realmObject = getDataById(aClass, id);
        return realmObject == null;
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
    public boolean hasTempModels() {
        return !realm.allObjects(TempGallery.class).isEmpty();
    }

}
