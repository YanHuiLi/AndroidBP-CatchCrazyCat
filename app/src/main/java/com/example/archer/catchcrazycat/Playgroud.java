package com.example.archer.catchcrazycat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Archer on 2016/5/24.
 */

//绘制界面
public class Playgroud extends SurfaceView {

    private static    int WIDTH=72;
    private static  final  int ROW=10;
    private static  final  int COL=10;
    //设置路障初始化为10
    private static  final  int BLOCK=15;
    private   Dot matrix[][];
    private Dot cat;


    public Playgroud(Context context) {
        super(context);

        getHolder().addCallback(callback);
        matrix=new Dot[ROW][COL];

        for (int i =0;i<ROW;i++){
            for (int j=0;j<COL;j++){
                //恰恰相反，注意
                matrix[i][j]=new Dot(j,i);
            }
        }

        //点创建完成以后调用
            initGame();
    }

    private Dot getDot(int x, int y){

        return matrix[y][x];
    }

    private void redraw() {
        Canvas canvas = getHolder().lockCanvas();
        //绘制青色
        canvas.drawColor(Color.LTGRAY);
        Paint paint=new Paint();

        //抗锯齿，优化屏幕
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        for (int  i=0;i<ROW;i++){

            int offset=0;
            //指定缩进
            if (i%2!=0){
                offset=WIDTH/2;
            }
            for (int j=0; j<COL;j++){
              Dot one=  getDot(j,i);
                switch (one.getStatus()){
                    case Dot.STATIC_OFF:

                        paint.setColor(0xFFEEEEEE);
                        break;
                    case Dot.STATIC_IN:

                        paint.setColor(0xFFFF0000);
                        break;
                    case Dot.STATIC_ON:
                        paint.setColor(0XFFFFAA00);

                        break;
                }

                canvas.drawOval(new RectF(one.getX()*WIDTH+offset,one.getY()*WIDTH,(one.getX()+1)*WIDTH+offset,(one.getY()+1)*WIDTH),paint);
            }
        }
        getHolder().unlockCanvasAndPost(canvas);

    }

    SurfaceHolder.Callback callback=new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            redraw();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    WIDTH=width/(COL+1);
            redraw();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

//游戏初始化
    private void initGame(){
        for (int i=0;i<ROW;i++){
            for (int  j=0;j<COL;j++){
                matrix[i][j].setStatus(Dot.STATIC_OFF);
            }
        }

        cat=new Dot(4,5);
        getDot(4,5).setStatus(Dot.STATIC_IN);
        for (int i=0;i<BLOCK;){
            int x= (int) ((Math.random()*1000)%COL);
            int y= (int) ((Math.random()*1000)%ROW);
            if (getDot(x,y).getStatus()==Dot.STATIC_OFF){
                getDot(x,y).setStatus(Dot.STATIC_ON);
                i++;
                System.out.println("Block"+i);
            }
        }
    }
}