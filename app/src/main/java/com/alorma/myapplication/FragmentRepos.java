package com.alorma.myapplication;

import java.util.List;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FragmentRepos extends BaseFragment<Repo> {

  @Override
  public void addItem(Repo r) {
    adapter.add(r.name);
  }

  @Override
  public void search(Observable<String> obs) {
    obs.flatMap(new Func1<String, Observable<ReposSearch>>() {
      @Override
      public Observable<ReposSearch> call(String s) {
        return service.repos(s);
      }
    })
        .subscribeOn(Schedulers.io())
        .map(new Func1<ReposSearch, List<Repo>>() {
          @Override
          public List<Repo> call(ReposSearch reposSearch) {
            return reposSearch.items;
          }
        })
        .flatMap(new Func1<List<Repo>, Observable<Repo>>() {
          @Override
          public Observable<Repo> call(List<Repo> repos) {
            return Observable.from(repos);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Repo>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(Repo repo) {
            addItem(repo);
          }
        });
  }
}
