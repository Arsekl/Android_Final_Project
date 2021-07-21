package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.like.LikeButton;

import java.util.List;

import static com.example.myapplication.ViewPageHolder.TransformLikeNum;

public class ViewPageActivity extends AppCompatActivity {
    class GestureListener extends GestureDetector.SimpleOnGestureListener{


        public GestureListener() {
            super();
        }
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            LikeButton likeButton= viewPageAdapter.viewPageHolders.get(position).likeButton;
            if (likeButton.isLiked())
            {
                likeButton.setLiked(false);
                position=viewPager2.getCurrentItem();
                int nums=messageList.get(position).getLikeCount();
                nums=nums-1;
                messageList.get(position).setLikeCount(nums);
                viewPageAdapter.viewPageHolders.get(position).likeNum.setText(TransformLikeNum(nums));
            }
            else
            {
                likeButton.setLiked(true);
                position=viewPager2.getCurrentItem();
                int nums=messageList.get(position).getLikeCount();
                nums=nums+1;
                messageList.get(position).setLikeCount(nums);
                viewPageAdapter.viewPageHolders.get(position).likeNum.setText(TransformLikeNum(nums));
            }
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            ImageView pauseImage= viewPageAdapter.viewPageHolders.get(position).pause;
            videoView= viewPageAdapter.viewPageHolders.get(position).videoView;
            if(state)
            {
                videoView.pause();
                pauseImage.setVisibility(View.VISIBLE);
            }
            else
            {
                videoView.start();
                pauseImage.setVisibility(View.INVISIBLE);
            }
            state=!state;
            return super.onSingleTapConfirmed(e);
        }
    }
    public int position;
    public List<VideoMessage> messageList;
    public GestureDetector gesture;
    public VideoView videoView;
    public boolean state=true;
    ViewPager2 viewPager2;
    ViewPageAdapter viewPageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        Intent intent = this.getIntent();
        messageList=(List<VideoMessage>)intent.getSerializableExtra("key");
        gesture=new GestureDetector(this,new GestureListener());
        viewPageAdapter=new ViewPageAdapter(this,messageList,gesture);
        viewPager2=findViewById(R.id.viewpager);
        viewPager2.setAdapter(viewPageAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                position=viewPager2.getCurrentItem();
                videoView=viewPageAdapter.viewPageHolders.get(position).videoView;
                videoView.setVisibility(View.VISIBLE);
                videoView.start();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                position=viewPager2.getCurrentItem();
                videoView=viewPageAdapter.viewPageHolders.get(position).videoView;
                switch (state) {
                    case ViewPager2.SCROLL_STATE_DRAGGING:
                        videoView.pause();
                        break;
                    case ViewPager2.SCROLL_STATE_IDLE:
                    case ViewPager2.SCROLL_STATE_SETTLING:
                        break;
                }
            }
        });
        gesture.setOnDoubleTapListener(new GestureListener());
    }
}