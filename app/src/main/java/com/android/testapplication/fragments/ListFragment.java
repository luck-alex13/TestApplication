package com.android.testapplication.fragments;


import android.os.Bundle;
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
import com.android.testapplication.adapters.RealmGalleryAdapter;
import com.android.testapplication.dataModels.GalleryModel;
import com.android.testapplication.dataModels.TempGallery;
import com.android.testapplication.io_package.AppUtil;
import com.android.testapplication.io_package.Constants;
import com.android.testapplication.database.RealmController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;
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
    private Realm realm;


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
        realm = RealmController.with(this).getRealm();

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
                RealmController.getInstance().clearAll(GalleryModel.class);
                runNetworkTask();
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorAccent);
        startTask();
        return view;
    }

    private void initRecView(RealmResults<TempGallery> realmResults) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAdapter = new GalleryRVAdapter(getContext());
        recyclerView.setAdapter(rvAdapter);
        RealmGalleryAdapter realmAdapter = new RealmGalleryAdapter(getContext(), realmResults, true);
        // Set the data and tell the RecyclerView to draw
        rvAdapter.setRealmAdapter(realmAdapter);
        rvAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    private void startTask() {

        if (RealmController.getInstance().hasGalleryModels()) {
            readDB();
        } else {
            runNetworkTask();
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
                                    List<GalleryModel> booksList = response.body();
                                    realm.beginTransaction();
                                    realm.copyToRealm(booksList);
                                    realm.commitTransaction();

                                    initRecView(getRandomList(RealmController.getInstance().getGalleryModels()));

                                    Log.d(LOG_TAG, Constants.SUCCESS_SAVE_DB);
                                } else {
                                    Log.d(LOG_TAG, "errorBody -> " + response.errorBody());
                                    Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_LONG).show();
                                    swipeContainer.setRefreshing(false);
                                }
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

    private RealmResults<TempGallery> getRandomList(RealmResults<GalleryModel> originalList) {
        Log.d(LOG_TAG, "getRandomList()");
        RealmController.getInstance().clearAll(TempGallery.class);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            TempGallery temp = TempGallery.copyFrom(originalList.get(random.nextInt(originalList.size())));
            while (!RealmController.getInstance().isExist(TempGallery.class, temp.getId())) {
                temp = TempGallery.copyFrom(originalList.get(random.nextInt(originalList.size())));
            }
            realm.beginTransaction();
            realm.copyToRealm(temp);
            realm.commitTransaction();
            Log.d(LOG_TAG, "TempGallery" + temp.getId());
        }
        return RealmController.getInstance().getTempGalleryModels();
    }

    private void readDB() {
        swipeContainer.setRefreshing(true);
        Log.d(LOG_TAG, "readDB()");
        initRecView(getRandomList(RealmController.getInstance().getGalleryModels()));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        RealmController.getInstance().getRealm().close();
    }
}
