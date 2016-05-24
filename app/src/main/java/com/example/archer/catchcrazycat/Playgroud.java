package com.example.archer.catchcrazycat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.util.Vector;

/**
 * Created by Archer on 2016/5/24.
 */

//绘制界面
public class Playgroud extends SurfaceView implements View.OnTouchListener {

    private static int WIDTH = 72;
    private static final int ROW = 10;
    private static final int COL = 10;
    //设置路障初始化为10
    private static final int BLOCK = 15;
    private Dot matrix[][];
    private Dot cat;


    public Playgroud(Context context) {
        super(context);

        getHolder().addCallback(callback);
        matrix = new Dot[ROW][COL];

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                //恰恰相反，注意
                matrix[i][j] = new Dot(j, i);
            }
        }

        //点创建完成以后调用
        initGame();
        setOnTouchListener(this);
    }

    private Dot getDot(int x, int y) {

        return matrix[y][x];
    }

    private boolean isAtEge(Dot dot) {

        //判断一个点是否是边界

        if (dot.getX() * dot.getY() == 0 || dot.getX() + 1 == COL || dot.getY() + 1 == ROW) {
            return true;
        }

        return false;
    }


    //得到周围的点，平行左边第一个点,顺时针
    private Dot getNrighbour(Dot one, int dir) {

        switch (dir) {
            case 1:
                return getDot(one.getX() - 1, one.getY());

            case 2:

                if (one.getY() % 2 == 0) {
                    return getDot(one.getX() - 1, one.getY() - 1);
                } else {
                    return getDot(one.getX(), one.getY() - 1);
                }

            case 3:
                if (one.getY() % 2 == 0) {
                    return getDot(one.getX(), one.getY() - 1);

                } else {
                    return getDot(one.getX() + 1, one.getY() - 1);
                }

            case 4:
                return getDot(one.getX() + 1, one.getY());


            case 5:
                if (one.getY() % 2 == 0) {
                    return getDot(one.getX(), one.getY() + 1);

                } else {
                    return getDot(one.getX() + 1, one.getY() + 1);
                }

            case 6:
                if (one.getY() % 2 == 0) {

                    return getDot(one.getX() - 1, one.getY() + 1);
                } else {
                    return getDot(one.getX(), one.getY() + 1);
                }
            default:
                break;
        }
        return null;
    }


private int getDistance(Dot one,int dir){

    int distance=0;

    Dot ori=one,next;

    while(true){

        next=getNrighbour(ori,dir);
        //遇到路障
        if (next.getStatus()==Dot.STATIC_ON){
            return distance*-1;
        }
        if (isAtEge(next)){
            distance++;
            return  distance;
        }
        distance++;
        ori=next;
    }


}

    private void MoveTo(Dot one){

        one.setStatus(Dot.STATIC_IN);
        getDot(cat.getX(),cat.getY()).setStatus(Dot.STATIC_OFF);
        cat.setXY(one.getX(),one.getY());

    }

    private void  move(){
        if (isAtEge(cat)){
            lose();
            return;
        }
        Vector<Dot> avaliable=new Vector<>();
        for (int i=1;i<7;i++){
           Dot n= getNrighbour(cat,i);
            if (n.getStatus()==Dot.STATIC_OFF){
                avaliable.add(n);
            }

        }
        if (avaliable.size()==0){
            win();
        }else {
            MoveTo(avaliable.get(0));
        }
    }

    private void lose(){

        Toast.makeText(getContext(),"Lose the game",Toast.LENGTH_LONG).show();

    }

    private void win(){

        Toast.makeText(getContext(),"Win the game",Toast.LENGTH_LONG).show();

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

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction()==MotionEvent.ACTION_UP){
//            Toast.makeText(getContext(), event.getX()+" "+event.getY(),Toast.LENGTH_LONG).show();
            int x,y;

            y= (int) (event.getY()/WIDTH);

            if (y%2==0){
                x= (int) (event.getX()/WIDTH);
            }else {
//偏移了半个元素的宽度
                x= (int) ((event.getX()-WIDTH/2)/WIDTH);
                //路障模式

            }
            if (x+1>COL||y+1>ROW){

                initGame();


            }else if ( getDot(x,y).getStatus()==Dot.STATIC_OFF){


                getDot(x,y).setStatus(Dot.STATIC_ON);

                move();

            }

            redraw();
        }




        return true;
    }
}