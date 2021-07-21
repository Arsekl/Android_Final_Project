package com.example.myapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.net.MalformedURLException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewPageHolder extends RecyclerView.ViewHolder{
    public LikeButton likeButton;
    public ImageView share;
    public ImageView comment;
    public TextView shareNum;
    public TextView commentNum;
    public TextView likeNum;
    public CircleImageView avatar;
    public TextView nickname;
    public TextView description;
    public VideoView videoView;
    public ImageView pause;
    public ImageView f1pic;
    public Uri VideoPath;
    private final MediaMetadataRetriever retriever=new MediaMetadataRetriever();

    public ViewPageHolder(@NonNull  View itemView) {
        super(itemView);
        likeButton = itemView.findViewById(R.id.likeBtn);
        share = itemView.findViewById(R.id.iv_share);
        comment = itemView.findViewById(R.id.iv_comment);
        shareNum = itemView.findViewById(R.id.tv_share);
        commentNum = itemView.findViewById(R.id.tv_comment);
        likeNum = itemView.findViewById(R.id.tv_like);
        avatar = itemView.findViewById(R.id.ci_avatar);
        nickname = itemView.findViewById(R.id.tv_nickname);
        description = itemView.findViewById(R.id.tv_description);
        pause=itemView.findViewById(R.id.iv_pause);
        videoView=itemView.findViewById(R.id.vv);
        f1pic=itemView.findViewById(R.id.iv_1framepic);
    }

    public void Bind(final VideoMessage message, Context context, final GestureDetector gesture) throws MalformedURLException {
        likeNum.setText(TransformLikeNum(message.getLikeCount()));
        nickname.setText("@"+message.getNickName());
        description.setText(message.getDescription());
        VideoPath=Uri.parse("android.resource://com.example.myapplication" + "/raw/" + message.getVideoName().substring(0, 5));
        Uri avatarPath = Uri.parse("file:///" + context.getExternalFilesDir(null) + "/pic/" + message.getPicName());
        avatar.setImageURI(avatarPath);
        videoView.setVideoURI(VideoPath);
        Glide.with(videoView)
                .setDefaultRequestOptions(
                        new RequestOptions()
                            .frame(0).centerCrop()
                )
                .load("android.resource://com.example.myapplication" + "/raw/" + message.getVideoName().substring(0, 5))
                .into(f1pic);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickAnimator(share);
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickAnimator(comment);
            }
        });
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gesture.onTouchEvent(event);
                return false;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        f1pic.setVisibility(View.INVISIBLE);
                        return true;
                    }
                });
            }
        });
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                int nums=message.getLikeCount();
                nums=nums+1;
                message.setLikeCount(nums);
                likeNum.setText(TransformLikeNum(nums));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                int nums=message.getLikeCount();
                nums=nums-1;
                message.setLikeCount(nums);
                likeNum.setText(TransformLikeNum(nums));
            }
        });;
    }

    private void ClickAnimator(ImageView imageView)
    {
        ObjectAnimator scalex=ObjectAnimator.ofFloat(imageView,"scaleX",0.8f,1.2f);
        ObjectAnimator scaley=ObjectAnimator.ofFloat(imageView,"scaleY",0.8f,1.2f);
        ObjectAnimator scalex2=ObjectAnimator.ofFloat(imageView,"scaleX",1.2f,1f);
        ObjectAnimator scaley2=ObjectAnimator.ofFloat(imageView,"scaleY",1.2f,1f);
        AnimatorSet animatorSet=new AnimatorSet();
        AnimatorSet animatorSet2=new AnimatorSet();
        animatorSet.playTogether(scalex,scaley);
        animatorSet2.playTogether(scalex2,scaley2);
        animatorSet.setDuration(200);
        animatorSet2.setDuration(200);
        animatorSet.start();
        animatorSet2.start();
    }

    public static String TransformLikeNum(int num)
    {
        int likenum=num;
        if(num/10000!=0)
        {
            likenum=likenum/10000;
            return likenum+"w";
        }
        else
            return String.valueOf(num);

    }
}
