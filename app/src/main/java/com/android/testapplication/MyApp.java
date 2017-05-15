package com.android.testapplication;

import android.app.Application;
import android.content.Context;

import com.android.testapplication.io_package.Constants;
import com.android.testapplication.io_package.PicassoSingleton;
import com.android.testapplication.database.RealmController;
import com.android.testapplication.io_package.ServerApi;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * TestApplication
 * Created by Alex on 13.05.2017.
 * contact on luck.alex13@gmail.com
 * © Alexander Novikov 2017
 */

public class MyApp extends Application {

    private static Context context;
    private Picasso picassoInstance;
    private static MyApp instance;
    private Retrofit retrofit;

    private ServerApi serverApi;
    private RealmConfiguration realmConfiguration;

    public MyApp() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        picassoInstance = PicassoSingleton.getSharedInstance(getApplicationContext());
        MyApp.context = getApplicationContext();


        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setExclusionStrategies(new ExclusionStrategy() {
                            @Override
                            public boolean shouldSkipField(FieldAttributes f) {
                                return f.getDeclaringClass().equals(RealmObject.class);
                            }

                            @Override
                            public boolean shouldSkipClass(Class<?> clazz) {
                                return false;
                            }
                        })
                        .create()))
                .build();
        serverApi = retrofit.create(ServerApi.class);

        realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        //Realm.deleteRealm(realmConfiguration);
        Realm.setDefaultConfiguration(realmConfiguration);
        RealmController.with(this);
    }

    public ServerApi getServerApi() {
        return serverApi;
    }

    public void clearDataBase(){
        RealmController.getInstance().clearAll();
    }


    public static MyApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return MyApp.context;
    }

    public Picasso getPicassoInstance() {
        return picassoInstance;
    }

    @Override
    public void onTerminate() {
        RealmController.getInstance().getRealm().close();
        super.onTerminate();
    }
}
