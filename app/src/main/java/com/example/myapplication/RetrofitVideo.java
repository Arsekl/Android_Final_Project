package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitVideo {
    @GET("api/invoke/video/invoke/video")
    Call<List<VideoMessage>> getVideo();
}
