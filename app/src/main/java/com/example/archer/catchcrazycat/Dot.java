package com.example.archer.catchcrazycat;

/**
 * Created by Archer on 2016/5/24.
 */
public class Dot {

    int x,y;
    int status;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void  setXY(int x, int y){
        this.x=x;
        this.y=y;
    }


    public static final int STATIC_ON=1;
    public static final int STATIC_OFF=0;
    public static final int STATIC_IN=9;

    public Dot(int x , int y){
        super();
        this.x=x;
        this.y=y;

        status=STATIC_OFF;

    }

}
