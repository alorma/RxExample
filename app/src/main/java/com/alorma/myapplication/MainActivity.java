package com.alorma.myapplication;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

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

    fragmentRepos.search(obs);
    fragmentUsers.search(obs);
  }
}
