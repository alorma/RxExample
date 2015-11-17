package com.alorma.myapplication;

public class FragmentRepos extends BaseFragment<Repo> {

  @Override
  public void add(Repo r) {
    adapter.add(r.name);
  }
}
