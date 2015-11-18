package com.alorma.myapplication;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.concurrent.TimeUnit;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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

    final EditText editText = (EditText) findViewById(R.id.edit_query);

    Observable<String> obs =
        RxTextView.textChanges(editText)
            .subscribeOn(AndroidSchedulers.mainThread())
            .filter(new Func1<CharSequence, Boolean>() {
              @Override
              public Boolean call(CharSequence s) {
                return s.length() >= 3;
              }
            })
            .throttleLast(100, TimeUnit.MILLISECONDS)
            .debounce(400, TimeUnit.MILLISECONDS)
            .map(new Func1<CharSequence, String>() {
              @Override
              public String call(CharSequence charSequence) {
                return charSequence.toString();
              }
            })
            .doOnNext(new Action1<String>() {
              @Override
              public void call(String s) {
                Snackbar.make(editText, s, Snackbar.LENGTH_SHORT).show();
              }
            });

    fragmentRepos.search(obs);
    fragmentUsers.search(obs);
  }
}
