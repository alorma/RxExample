package com.alorma.myapplication;

public class FragmentUsers extends BaseFragment<Integer> {

  @Override
  public void add(Integer integer) {
    adapter.add(String.valueOf(integer));
  }
}
