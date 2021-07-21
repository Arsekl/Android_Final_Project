package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.List;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final int FILE_DOWNLOAD_FAILED = 1;
    private static final int FILE_DOWNLOAD_SUCCESS = 0;

//    public int downloadMp4(VideoMessage videoMessage) {
//        String Mp4Dirname = this.getExternalFilesDir(null) + "/mp4/";
//        File mp4dir = new File(Mp4Dirname);
//        if (!mp4dir.exists()) {
//            mp4dir.mkdir();
//        }
//        File mp4 = new File(Mp4Dirname + videoMessage.getVideoName());
//        if (mp4.exists()) {
//            mp4.delete();
//        }
//        OkHttpClient client = new OkHttpClient();
//        final Request request = new Request.Builder()
//                .get()
//                .url(videoMessage.getVideoUrl())
//                .build();
//        okhttp3.Call call = client.newCall(request);
//        call.enqueue(new okhttp3.Callback() {
//            @Override
//            public void onFailure(okhttp3.Call call, IOException e) {
//                Log.e("more", "onFailure:Video");
//            }
//
//            @Override
//            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
//                assert response.body() != null;
//                Headers responseHeaders = response.headers();
//                for (int i = 0; i < responseHeaders.size(); i++) {
//                    Log.d("LOG_TAG", responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                }
//                InputStream is =  response.body().byteStream();
//                OutputStream os = new FileOutputStream(mp4);
//                byte[] bytes = new byte[2048];
//                int len;
//                while ((len = is.read(bytes)) != -1) {
//                    os.write(bytes, 0, len);
//                }
//                os.flush();
//                os.close();
//                is.close();
//            }
//        });
//        return FILE_DOWNLOAD_SUCCESS;
//    }

    public int downloadPic(VideoMessage videoMessage) {
        String PicDirname = this.getExternalFilesDir(null) + "/pic/";
        File picdir = new File(PicDirname);
        if (!picdir.exists()) {
            picdir.mkdir();
        }
        File pic = new File(PicDirname + videoMessage.getPicName());
        if (pic.exists()) {
            pic.delete();
        }
        byte[] bytes = new byte[1024];
        int len;
        try {
            URL url = new URL(videoMessage.getAvaTorUrl());
            InputStream inputStream = url.openStream();
            OutputStream outputStream = new FileOutputStream(pic);
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            inputStream.close();
            outputStream.close();
            return FILE_DOWNLOAD_SUCCESS;
        } catch (MalformedURLException e) {
            return FILE_DOWNLOAD_FAILED;
        } catch (FileNotFoundException e) {
            return FILE_DOWNLOAD_FAILED;
        } catch (IOException e) {
            return FILE_DOWNLOAD_FAILED;
        }
    }

    private class DownLoadThread extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            int count_download = 0;
            for (int i = 0; i < messageList.size(); i++) {
//                switch (downloadMp4(messageList.get(i))) {
//                    case FILE_DOWNLOAD_SUCCESS:
//                        count_download = count_download + 1;
//                        publishProgress(count_download * TimePiece);
//                        break;
//                    case FILE_DOWNLOAD_FAILED:
//                        break;
//                }
                switch (downloadPic(messageList.get(i))) {
                    case FILE_DOWNLOAD_SUCCESS:
                        count_download = count_download + 1;
                        publishProgress(count_download * TimePiece);
                        break;
                    case FILE_DOWNLOAD_FAILED:
                        break;
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            toast = Toast.makeText(getApplicationContext(), "初始化...", Toast.LENGTH_SHORT);
            toast.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            toast = Toast.makeText(getApplicationContext(), "欢迎", Toast.LENGTH_SHORT);
            toast.show();
            startActivity(intent);
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            lottie.setProgress(values[0] / 100f);
            super.onProgressUpdate(values);
        }
    }
    public int TimePiece;
    public LottieAnimationView lottie;
    public List<VideoMessage> messageList;
    public List<VideoMessage> textList;
    public Intent intent;
    public Bundle bundle;
    public Toast toast;
    public DownLoadThread downLoadThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://beiyou.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bundle=new Bundle();
        downLoadThread=new DownLoadThread();
//        lottie=(LottieAnimationView) findViewById(R.id.progress);
        intent=new Intent(this, ViewPageActivity.class);
        RetrofitVideo retrofitVideo=retrofit.create(RetrofitVideo.class);
        retrofitVideo.getVideo().enqueue(new Callback<List<VideoMessage>>() {
            @Override
            public void onResponse(Call<List<VideoMessage>> call, Response<List<VideoMessage>> response) {
                messageList=response.body();
                if (messageList != null) {
                    TimePiece=100/(2*messageList.size());
                }
                bundle.putSerializable("key",(Serializable) messageList);
                intent.putExtras(bundle);
                downLoadThread.execute();
            }

            @Override
            public void onFailure(Call<List<VideoMessage>> call, Throwable t) {
                toast=Toast.makeText(getApplicationContext(),"获取视频信息失败"+t.getMessage(),Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}