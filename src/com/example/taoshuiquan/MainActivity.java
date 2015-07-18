package com.example.taoshuiquan;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements android.view.GestureDetector.OnGestureListener
{
	
	GestureDetector detector;
	Handler handler;
	SoundPool sp;
	private float volume;
	private HashMap<Integer, Integer> hm;
	private int currentID;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initSoundPool();
        
        Button bright = (Button)findViewById(R.id.Bright);
        Button bleft = (Button)findViewById(R.id.Bleft);
        
        RelativeLayout mainlayout = (RelativeLayout)findViewById(R.id.root);
        final MyView myview = new MyView(this);
        
        
        
        //创建手势检测器
        detector = new GestureDetector(this,this);
  
       //new Thread(myview,"shuiquan").start();                
        mainlayout.addView(myview);
     
        // 只有主线程能invalidate 它的view
         handler = new Handler()
        {
        	public void handleMessage(Message msg)
        	{
        		if(msg.what == 0x123)
        			myview.invalidate();
        		//if(msg.what == 0x124)
        		//	myview.ballaccR();
        	}
        };
          Timer timer = new Timer();
	     timer.schedule(new TimerTask()
	     {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				myview.ref();
				handler.sendEmptyMessage(0x123);
			}
	     }, 0,80);
	     
	     bright.setOnClickListener(new OnClickListener() 
	     {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    playSound(1, 0);
				myview.ballaccR();
			
			}
		});
	     bleft.setOnClickListener(new OnClickListener() 
	     {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				playSound(1, 0);
				myview.ballaccL();
			}
		});
    }

    private void initSoundPool() {
        sp=new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        hm =new HashMap<Integer, Integer>();
        hm.put(1, sp.load(getApplicationContext(), R.raw.qiexigua, 1));
    }
    private void playSound(int num,int loop){
        AudioManager am=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        float currentSound=am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxSound=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume=currentSound/maxSound;
        currentID=sp.play(hm.get(num), volume, volume, 1, loop, 1.0f);
   }
    
	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}


	@Override
	protected void onPause()
	{
		super.onPause();
	}
    @Override
    public boolean onTouchEvent(MotionEvent me)
    {
    	return detector.onTouchEvent(me);
    }
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "onDown", Toast.LENGTH_LONG).show();
		//调用MyView喷水方法
		//handler.sendEmptyMessage(0x124);		
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}	
}
