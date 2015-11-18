package com.alorma.myapplication;

import java.util.List;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FragmentUsers extends BaseFragment<User> {

  @Override
  public void addItem(User user) {
    adapter.add(user.login);
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
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(
            new Action1<UsersSearch>() {
          @Override
          public void call(UsersSearch usersSearch) {
            adapter.clear();
          }
        })
        .map(new Func1<UsersSearch, List<User>>() {
          @Override
          public List<User> call(UsersSearch UsersSearch) {
            return UsersSearch.items;
          }
        })
        .flatMap(new Func1<List<User>, Observable<User>>() {
          @Override
          public Observable<User> call(List<User> users) {
            return Observable.from(users);
          }
        })
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
