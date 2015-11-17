package com.alorma.myapplication;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.mikepenz.materialize.MaterializeBuilder;
import java.util.List;
import java.util.concurrent.TimeUnit;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

  private FragmentRepos fragmentRepos;
  private FragmentUsers fragmentUsers;
  private GitHubService service;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

    fragmentRepos = new FragmentRepos();
    fragmentUsers = new FragmentUsers();

    ft.replace(R.id.frame1, fragmentRepos);
    ft.replace(R.id.frame2, fragmentUsers);

    ft.commit();

    EditText editText = (EditText) findViewById(R.id.edit_query);

    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    service = retrofit.create(GitHubService.class);

    Observable<String> obs = RxTextView.afterTextChangeEvents(editText)
        .map(new Func1<TextViewAfterTextChangeEvent, String>() {
          @Override
          public String call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
            return textViewAfterTextChangeEvent.editable().toString();
          }
        })
        .throttleLast(100, TimeUnit.MILLISECONDS)
        .debounce(200, TimeUnit.MILLISECONDS)
        .filter(new Func1<String, Boolean>() {
          @Override
          public Boolean call(String s) {
            return s.length() >= 3;
          }
        })
        .observeOn(AndroidSchedulers.mainThread());

    searchRepos(obs);
  }

  private void searchRepos(Observable<String> obs) {
    obs.flatMap(new Func1<String, Observable<ReposSearch>>() {
      @Override
      public Observable<ReposSearch> call(String s) {
        return service.repos(s);
      }
    })
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation())
        .takeLast(100, TimeUnit.MILLISECONDS)
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
        .subscribe(new Subscriber<Repo>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(Repo repo) {
            fragmentRepos.add(repo);
          }
        });
  }
}
