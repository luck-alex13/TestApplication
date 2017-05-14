package com.android.testapplication.io_package;

import com.android.testapplication.dataModels.MyDataModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Restoman
 * Created by Alex on 12.05.2017.
 * contact on luck.alex13@gmail.com
 * © Alexander Novikov 2017
 */

public interface ServerApi {

    @GET(Constants.URL_API_DATA)
    Call<List<MyDataModel>> getPhotos();

}
