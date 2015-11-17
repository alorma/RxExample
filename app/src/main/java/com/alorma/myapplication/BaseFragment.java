package com.alorma.myapplication;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;

public abstract class BaseFragment<K> extends ListFragment {

  protected ArrayAdapter<String> adapter;
  protected GitHubService service;

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    adapter =
        new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
            new ArrayList<String>());
    setListAdapter(adapter);


    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    service = retrofit.create(GitHubService.class);

  }

  public abstract void addItem(K k);

  public abstract void search(Observable<String> obs);
}
