package com.alorma.myapplication;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface GitHubService {

  //Async
  @GET("/search/repositories")
  Observable<ReposSearch> repos(@Query("q") String query);

  @GET("/search/users")
  Observable<UsersSearch> users(@Query("q") String query);
}
