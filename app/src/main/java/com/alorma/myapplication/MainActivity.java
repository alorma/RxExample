package com.alorma.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import java.util.concurrent.TimeUnit;
import rx.Observable;
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

    Observable<String> obs = RxTextView.textChanges(editText)
        .throttleLast(100, TimeUnit.MILLISECONDS)
        .debounce(200, TimeUnit.MILLISECONDS)
        .filter(new Func1<CharSequence, Boolean>() {
          @Override
          public Boolean call(CharSequence s) {
            return s.length() >= 3;
          }
        })
        .map(new Func1<CharSequence, String>() {
          @Override
          public String call(CharSequence charSequence) {
            return charSequence.toString();
          }
        });

    fragmentRepos.search(obs);
    fragmentUsers.search(obs);
  }

}
