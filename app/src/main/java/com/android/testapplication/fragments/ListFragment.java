package com.android.testapplication.fragments;


import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.testapplication.MainActivity;
import com.android.testapplication.MyApp;
import com.android.testapplication.R;
import com.android.testapplication.adapters.GalleryRVAdapter;
import com.android.testapplication.dataModels.GalleryModel;
import com.android.testapplication.database.DBHelper;
import com.android.testapplication.io_package.AppUtil;
import com.android.testapplication.io_package.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LOG_TAG = "LOG_TAG";


    private RecyclerView recyclerView;
    private GalleryRVAdapter rvAdapter;
    private long cacheSize;
    private ArrayList<GalleryModel> imagesList;
    private SwipeRefreshLayout swipeContainer;


    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ListFragment.
     */
    public static ListFragment newInstance(String param1) {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        //Log.d(LOG_TAG, "onStart()");
        ((MainActivity) getActivity()).setActivityTitle(R.string.gallery);
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyApp.getInstance().getDbHelperInstance().clearCache();
                runNetworkTask();
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorAccent);
        setHasOptionsMenu(true);
        startTask();
        return view;
    }

    private void initRecView(final ArrayList<GalleryModel> list, Random random) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(random != null){
            ArrayList<GalleryModel> randomList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                randomList.add(list.get(random.nextInt(list.size())));
            }
            rvAdapter = new GalleryRVAdapter(randomList);
        }else {
            rvAdapter = new GalleryRVAdapter(list);
        }

        recyclerView.setAdapter(rvAdapter);

    }

    private void startTask() {

        cacheSize = MyApp.getInstance().getDbHelperInstance().getRowsNumber(DBHelper.TABLE_GALLERY);
        if (cacheSize == 0) {
            runNetworkTask();
        } else {
            readDB();
        }

    }

    private void runNetworkTask() {
        if (AppUtil.hasConnection(MyApp.getInstance().getApplicationContext())) {
            Log.d(LOG_TAG, "runNetworkTask()");
            try {
                swipeContainer.setRefreshing(true);
                MyApp.getInstance().getServerApi().getPhotos().enqueue(
                        new Callback<List<GalleryModel>>() {
                            @Override
                            public void onResponse(Call<List<GalleryModel>> call, Response<List<GalleryModel>> response) {
                                if (response.isSuccessful()) {
                                    Log.d(LOG_TAG, " isSuccessful ");
                                    initRecView((ArrayList<GalleryModel>) response.body(), new Random());
                                    writeInDB(response.body());
                                } else {
                                    Log.d(LOG_TAG, "errorBody -> " + response.errorBody());
                                    Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_LONG).show();
                                }
                                swipeContainer.setRefreshing(false);

                            }

                            @Override
                            public void onFailure(Call<List<GalleryModel>> call, Throwable t) {
                                t.printStackTrace();
                                Log.d(LOG_TAG, "onFailure() " + t);
                                swipeContainer.setRefreshing(false);
                                Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_LONG).show();

                            }
                        }
                );

            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "NullPointerException() " + e);

            }
        } else {
            Log.d(LOG_TAG, "NoConnection");
            Toast.makeText(getContext(), R.string.error_nointernet, Toast.LENGTH_LONG).show();
        }
    }

    private void readDB() {
        swipeContainer.setRefreshing(true);

        final Thread thread = new Thread() {
            @Override
            public void run() {
                String response;
                Log.d(LOG_TAG, "readDB()");
                try {
                    imagesList = MyApp.getInstance().getDbHelperInstance().query().readGalleryItems();
                    response = Constants.SUCCESS_READ_DB;
                } catch (SQLiteException ex) {
                    ex.printStackTrace();
                    response = Constants.ERROR_READ_DB;
                }
                Message message = handler.obtainMessage(1, response);
                handler.sendMessage(message);
            }
        };
        thread.setPriority(5);
        thread.start();
    }

    private void writeInDB(final List<GalleryModel> body) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "writeInDB()");
                String response;
                try {
                    for (GalleryModel dataModel :
                            body) {
                        MyApp.getInstance().getDbHelperInstance().query().saveGalleryItem(dataModel);
                    }
                    response = Constants.SUCCESS_SAVE_DB;
                } catch (SQLiteException ex) {
                    ex.printStackTrace();
                    response = Constants.ERROR_SAVE_DB;
                }
                Message message = handler.obtainMessage(1, response);
                handler.sendMessage(message);
            }
        };
        thread.setPriority(5);
        thread.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            final String result = (String) message.obj;
            Log.d(LOG_TAG, " handleMessage() " + result);
            if (result.equals(Constants.SUCCESS_READ_DB)) {
                initRecView(imagesList, null);
                swipeContainer.setRefreshing(false);
            } else if (result.equals(Constants.ERROR_READ_DB)) {
                swipeContainer.setRefreshing(false);
                Toast.makeText(getContext(), R.string.database_error, Toast.LENGTH_LONG).show();

            } else if (result.equals(Constants.SUCCESS_SAVE_DB)) {

            } else if (result.equals(Constants.ERROR_SAVE_DB)) {

            }


        }
    };

}
