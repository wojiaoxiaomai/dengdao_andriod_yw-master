package com.choucheng.dengdao2.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class AnimationUtil{
	private Context context;
	private int i;
	private Animation animation;
	
	public AnimationUtil(Context context){
		this.context=context;
	}
	
	/**
	 * 执行xml实现的动画
	 * @param animationId
	 * @param view
	 */
	public void runAnimation(int animationId,View view){
		animation = AnimationUtils.loadAnimation(context, animationId);
	     view.setAnimation(animation);
	}

	/**
	 * 连续执行2个动画
	 * @param animatioids
	 * @param view
	 */
    public void runMoreAniamtion(final int[] animatioids,final View view){
       
    	final Animation.AnimationListener animationListener=new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            	if(i<animatioids.length){
            		animation=AnimationUtils.loadAnimation(context, animatioids[i]);
            		if(animation!=null){
            			view.startAnimation(animation);
            		}
            	
            	}
                
             }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        };
        
    	animation= AnimationUtils.loadAnimation(context, animatioids[i]);
    	if(animation!=null){
    		animation.setAnimationListener(animationListener);
    		view.startAnimation(animation);
    		i++;
    	}

    }	


    /**
     * 开始动画
     */
    public static void startforResultAnimation(Activity beforeActivity, Intent intent,int anima1,int animal2,int requestcode) {
        beforeActivity.startActivityForResult(intent, requestcode);
        beforeActivity.overridePendingTransition(anima1, animal2);
    }

    
    /**
     * 开始动画
     */
    public static void startAnimation(Activity beforeActivity, Intent intent,int anima1,int animal2) {
        beforeActivity.startActivity(intent);
        beforeActivity.overridePendingTransition(anima1, animal2);
    }


    /**
     * 退出动画
     */
    public static void finishAnimation(Activity beforeActivity,int anima1,int anima2) {
        beforeActivity.overridePendingTransition(anima1,anima2);
    }


	
}
