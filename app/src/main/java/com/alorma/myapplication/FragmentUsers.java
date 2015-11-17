package com.alorma.myapplication;

import java.util.List;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FragmentUsers extends BaseFragment<User> {

  @Override
  public void addItem(User user) {
    adapter.add(user.name);
  }

  @Override
  public void search(Observable<String> obs) {
    obs.flatMap(new Func1<String, Observable<UsersSearch>>() {
      @Override
      public Observable<UsersSearch> call(String s) {
        return service.users(s);
      }
    })
        .subscribeOn(Schedulers.io())
        .map(new Func1<UsersSearch, List<User>>() {
          @Override
          public List<User> call(UsersSearch UsersSearch) {
            return UsersSearch.items;
          }
        })
        .flatMap(new Func1<List<User>, Observable<User>>() {
          @Override
          public Observable<User> call(List<User> Users) {
            return Observable.from(Users);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<User>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(User user) {
            addItem(user);
          }
        });
  }
}
