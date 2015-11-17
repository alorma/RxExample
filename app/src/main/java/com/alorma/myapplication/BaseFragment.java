package com.alorma.myapplication;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

public abstract class BaseFragment<K> extends ListFragment {

  protected ArrayAdapter<String> adapter;

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    adapter =
        new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
            new ArrayList<String>());
    setListAdapter(adapter);
  }

  public abstract void add(K k);
}
