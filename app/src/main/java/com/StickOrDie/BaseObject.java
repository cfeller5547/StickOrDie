package com.StickOrDie;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class BaseObject {
    protected int x, y;
    protected int width, height;
    private Bitmap bm;
    protected Rect rect;

    public BaseObject(){}

    public BaseObject(Bitmap bm, int x, int y, int width, int height){
        this.bm = bm;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }


    public int getY() {
        return y;
    }

    public int getY2(){
        return getY() + getHeight();
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public Rect getRect(){
        return new Rect((int)this.x, (int)this.y, (int)this.x+this.width,(int)this.y + this.height);
    }
    public int getRectTop(){
        return this.getRect().top;
    }

    public int getRectLeft(){
        return this.getRect().left;
    }

    public int getRectRight(){
        return this.getRect().right;
    }


    public int getRectBottom(){
        return this.getRect().bottom;
    }

    public void setRect(Rect rect){
    this.rect = rect;
    }
}
