package com.android.testapplication;

import android.app.Application;
import android.content.Context;

import com.android.testapplication.database.DBHelper;
import com.android.testapplication.io_package.Constants;
import com.android.testapplication.io_package.PicassoSingleton;
import com.android.testapplication.io_package.ServerApi;
import com.squareup.picasso.Picasso;

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
    private DBHelper dbHelperInstance;

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
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serverApi = retrofit.create(ServerApi.class);
    }

    public ServerApi getServerApi() {
        return serverApi;
    }

    public DBHelper getDbHelperInstance() {
        if (dbHelperInstance == null) {
            dbHelperInstance = new DBHelper(getApplicationContext());
            return dbHelperInstance;
        } else return dbHelperInstance;
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
        dbHelperInstance.close();
        super.onTerminate();
    }
}
