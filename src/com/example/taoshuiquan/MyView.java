package com.example.taoshuiquan;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.style.LineHeightSpan.WithDensity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyView extends View 
{
	int height ,width;      //屏幕大小   
	public static int stopF = 0;
	public static int NUM_BALL = 10;
	public static double WATER_F = 0.5;
	public static double G = 0.98;
	public int inFN = 1;
	
	static ball[] objball = new ball[NUM_BALL];
	
	Paint paint = new Paint();
	
	//构造函数
	public MyView(Context context)
	{
		super(context);
		//new thread of ball
		for(int i = 0;i<NUM_BALL; i++)
		{
			objball[i] = new ball();
			Log.v("初始化圈", "第"+i+ "个：x="+objball[i].x +"; y="+objball[i].y);
		}
	}
	public MyView(Context context, AttributeSet set)
	{
		super(context, set);
		//new thread of ball
	}
	
	@Override  
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)     
	{   
	    height = View.MeasureSpec.getSize(heightMeasureSpec)*7/8;    
	    width = View.MeasureSpec.getSize(widthMeasureSpec);    
	    setMeasuredDimension(width,height);  //这里面是原始的大小，需要重新计算可以修改本行   
	    
	  //dosomething   
	} 
	@Override
	
	// 重写该方法，进行绘图
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
/*			// 把整张画布绘制成BLACK
         canvas.drawColor(Color.BLACK);
		 InputStream is = getResources().openRawResource(R.drawable.underseaworld);   
	     //decode   
	     Bitmap mBitmap = BitmapFactory.decodeStream(is);   
	     Paint mPaint = new Paint();   
	     canvas.drawBitmap(mBitmap, 0, 0, null);   */
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.underseaworld);  
        Matrix matrix=new Matrix();
        matrix.postScale(1.2f, 1.3f);
        Bitmap dstbmp=Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
        canvas.drawColor(Color.BLACK);  
        canvas.drawBitmap(dstbmp, 10, 10, null);  
	
		// 去锯齿
		paint.setAntiAlias(true);
		
		
		//paint.setColor(COLOR[(int) (Math.random()*3)]);  //不停变色的套环

		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(12);
		
		//画套针
		paint.setColor(Color.CYAN);
		canvas.drawLine(width/2, height/2 - 80, width/2, height/2+30, paint);// 画线  
		canvas.drawCircle(width/2, height/2+90, 60, paint);
		
		// 绘制圆形
		int[] COLOR = {Color.RED,Color.BLUE,Color.GREEN};
		for(int i = 0;i<NUM_BALL; i++)
		{
			paint.setColor(COLOR[(int) (i%3)]);
			Log.e("第一次画的时候", "第"+i+ "个：x="+objball[i].x+"; y="+objball[i].y);
			Log.v("heightWidth", "heigth = "+height +"; width = "+width);
			//canvas.drawCircle(objball[i].x, objball[i].y, objball[i].radius, paint);
			RectF oval2 = new RectF(objball[i].x -objball[i].radius, objball[i].y-objball[i].radiusN, objball[i].x+objball[i].radius, objball[i].y+objball[i].radiusN);// 设置个新的长方形，扫描测量  
			canvas.drawOval(oval2, paint);
		}
                  
		//画出入套针的范围
		//canvas.drawRect(width/2-30,height/2 -80 -10, width/2+30,height/2 -80 +10, paint);
		invalidate();  
        //Log.v("main", "invaliate");  
	}
	
//	重置圈大小
	protected void ref()
	{
		for(int i = 0;i<NUM_BALL; i++)
		{
			
			objball[i].reball();
		}
	}
	
//   受喷水里加速
	protected void ballaccR() {
		// TODO Auto-generated method stub
		for(int i = 0;i<NUM_BALL; i++)
		{
			objball[i].allaccR();
		}
		
	}
	protected void ballaccL() {
		// TODO Auto-generated method stub
		for(int i = 0;i<NUM_BALL; i++)
		{
			objball[i].allaccL();
		}
	}
	
	//！！！！球对象内部类  
	private class ball{
	  private int radius = 50;     //椭圆外径大小
	  private int radiusN = 40;    //椭圆内径大小
	  double vR = 3;                //椭圆翻滚速度
	  
	  int x = (int)(2000*(Math.random()));
	  int y = (int)(1600*(Math.random()));      //初始位置在屏幕中央
	  
	  double vX=8,vY=10;            //初始化速度为零	
	  final double minvY = 15;
	  int inF = 0;  //记录是否进套针
	  	  
	  void reball()
	  {
	    //1.判断是否进入套针内，是的话将inF 进行标记
		if((x>(width/2-30))&&(x<(width/2+30))&&(y>(height/2 -80 -10))&&(y>(height/2-80 +10))&&vY>0&&(inF==0))
		{
			inF = inFN;
			inFN++;
		}
		//2.进入套针的情况
	    if(inF!=0)
	    {   
	    	radiusN = 10;
	    	if(y>=height/2+30-10)//2.1 是否到达套针底部，到底则静止不动
	    	{
	    	   y = height/2+30-10 + 20-inF*2;//;
	    	   
	    	}else//2.2 没到底时继续向下运动
	    	{
	    		y+=3;
	    	}
	    }else{
	    //3.没进入套针的情况
		//3.1 是否速度下降到与水阻力平衡？y速度为负，且小于等于min（为负）.则Vy不变
	      if(vY>=minvY||stopF==1)
	      {//1.1. 当vY = min，且y小于radius时，球触底。vX 及vY置零
		    if(y>=height)
		    {
		    }else{
			  vX =vX==0?(vX):(vX>0?(vX - WATER_F):(vX +  WATER_F));//水阻力，x方向速度衰减
			  Log.v("reball _ x = ", " "+x);
			  x+=vX;
			  y+=vY;
		     }
	       }else{
	      //3.2. 非1.状态时，则一直给予其g的加速度。使其受重力影响而运动。
		   vX = (vX==0?(vX):(vX>0?(vX - WATER_F):(vX +  WATER_F)));//水阻力，x方向速度衰减
		   vY += G; // 只要不是与水阻力平衡迅速下降状态，都需要有重力加速度
		   x+=vX;
		   y+=vY;
	       }
	   
	    //3.3 边界检测 
	     if(x>=width)
		 {
		  vX = -vX*8/10;   //撞墙后减速
		  x = width;
		 }else if(x<=radius)
		 {
		  vX = -vX*8/10;
		  x = radius+1;
		 }
	     
		 if(y>=height)
		 {
		  vY = 0;
		 }else if(y < radius )
		 {
			 vY = 2;
			 y = radius+1;
		 }
	   
		//3.4 椭圆形状改变
		if(radiusN>=radius)
		{
			vR = -Math.abs(vY/15);
		}else if(radiusN <= 0)
		{
			vR = Math.abs(vY/15);
		}		
		radiusN += vR;
	    }//inF!=0 else
	  }// reball()  END
	  //受喷水，加速度
	  void allaccR()
	  {
		  stopF = 0;
		  double accR =(width +height-(Math.abs(x - width)+ Math.abs(y - height)))/40;
		 
		 /* Log.e("x,y,height,width", x+" , "+ y+" , "+height +" , " + width);
		  Log.e("accR", accR +"");*/
		  
		  vY += -accR;
		  vX += -accR*7/10;
	  }//allaccR()  END
	  void allaccL()
	  {
		  stopF = 0;
		  double accL =(width + height-(Math.abs(x) + Math.abs(y - height)))/40;		  
		  Log.e("height", height +"");
		  Log.e("width", width +"");
/*		  Log.e("accR", accL +"");*/
		  
		  vY += -accL;
		  vX += accL*7/10;
	  }//allaccL()   END
	}// ball类end
  }