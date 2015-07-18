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
	int height ,width;      //��Ļ��С   
	public static int stopF = 0;
	public static int NUM_BALL = 10;
	public static double WATER_F = 0.5;
	public static double G = 0.98;
	public int inFN = 1;
	
	static ball[] objball = new ball[NUM_BALL];
	
	Paint paint = new Paint();
	
	//���캯��
	public MyView(Context context)
	{
		super(context);
		//new thread of ball
		for(int i = 0;i<NUM_BALL; i++)
		{
			objball[i] = new ball();
			Log.v("��ʼ��Ȧ", "��"+i+ "����x="+objball[i].x +"; y="+objball[i].y);
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
	    setMeasuredDimension(width,height);  //��������ԭʼ�Ĵ�С����Ҫ���¼�������޸ı���   
	    
	  //dosomething   
	} 
	@Override
	
	// ��д�÷��������л�ͼ
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
/*			// �����Ż������Ƴ�BLACK
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
	
		// ȥ���
		paint.setAntiAlias(true);
		
		
		//paint.setColor(COLOR[(int) (Math.random()*3)]);  //��ͣ��ɫ���׻�

		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(12);
		
		//������
		paint.setColor(Color.CYAN);
		canvas.drawLine(width/2, height/2 - 80, width/2, height/2+30, paint);// ����  
		canvas.drawCircle(width/2, height/2+90, 60, paint);
		
		// ����Բ��
		int[] COLOR = {Color.RED,Color.BLUE,Color.GREEN};
		for(int i = 0;i<NUM_BALL; i++)
		{
			paint.setColor(COLOR[(int) (i%3)]);
			Log.e("��һ�λ���ʱ��", "��"+i+ "����x="+objball[i].x+"; y="+objball[i].y);
			Log.v("heightWidth", "heigth = "+height +"; width = "+width);
			//canvas.drawCircle(objball[i].x, objball[i].y, objball[i].radius, paint);
			RectF oval2 = new RectF(objball[i].x -objball[i].radius, objball[i].y-objball[i].radiusN, objball[i].x+objball[i].radius, objball[i].y+objball[i].radiusN);// ���ø��µĳ����Σ�ɨ�����  
			canvas.drawOval(oval2, paint);
		}
                  
		//����������ķ�Χ
		//canvas.drawRect(width/2-30,height/2 -80 -10, width/2+30,height/2 -80 +10, paint);
		invalidate();  
        //Log.v("main", "invaliate");  
	}
	
//	����Ȧ��С
	protected void ref()
	{
		for(int i = 0;i<NUM_BALL; i++)
		{
			
			objball[i].reball();
		}
	}
	
//   ����ˮ�����
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
	
	//��������������ڲ���  
	private class ball{
	  private int radius = 50;     //��Բ�⾶��С
	  private int radiusN = 40;    //��Բ�ھ���С
	  double vR = 3;                //��Բ�����ٶ�
	  
	  int x = (int)(2000*(Math.random()));
	  int y = (int)(1600*(Math.random()));      //��ʼλ������Ļ����
	  
	  double vX=8,vY=10;            //��ʼ���ٶ�Ϊ��	
	  final double minvY = 15;
	  int inF = 0;  //��¼�Ƿ������
	  	  
	  void reball()
	  {
	    //1.�ж��Ƿ���������ڣ��ǵĻ���inF ���б��
		if((x>(width/2-30))&&(x<(width/2+30))&&(y>(height/2 -80 -10))&&(y>(height/2-80 +10))&&vY>0&&(inF==0))
		{
			inF = inFN;
			inFN++;
		}
		//2.������������
	    if(inF!=0)
	    {   
	    	radiusN = 10;
	    	if(y>=height/2+30-10)//2.1 �Ƿ񵽴�����ײ���������ֹ����
	    	{
	    	   y = height/2+30-10 + 20-inF*2;//;
	    	   
	    	}else//2.2 û����ʱ���������˶�
	    	{
	    		y+=3;
	    	}
	    }else{
	    //3.û������������
		//3.1 �Ƿ��ٶ��½�����ˮ����ƽ�⣿y�ٶ�Ϊ������С�ڵ���min��Ϊ����.��Vy����
	      if(vY>=minvY||stopF==1)
	      {//1.1. ��vY = min����yС��radiusʱ���򴥵ס�vX ��vY����
		    if(y>=height)
		    {
		    }else{
			  vX =vX==0?(vX):(vX>0?(vX - WATER_F):(vX +  WATER_F));//ˮ������x�����ٶ�˥��
			  Log.v("reball _ x = ", " "+x);
			  x+=vX;
			  y+=vY;
		     }
	       }else{
	      //3.2. ��1.״̬ʱ����һֱ������g�ļ��ٶȡ�ʹ��������Ӱ����˶���
		   vX = (vX==0?(vX):(vX>0?(vX - WATER_F):(vX +  WATER_F)));//ˮ������x�����ٶ�˥��
		   vY += G; // ֻҪ������ˮ����ƽ��Ѹ���½�״̬������Ҫ���������ٶ�
		   x+=vX;
		   y+=vY;
	       }
	   
	    //3.3 �߽��� 
	     if(x>=width)
		 {
		  vX = -vX*8/10;   //ײǽ�����
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
	   
		//3.4 ��Բ��״�ı�
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
	  //����ˮ�����ٶ�
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
	}// ball��end
  }