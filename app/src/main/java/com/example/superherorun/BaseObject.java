package com.example.superherorun;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class BaseObject {
    protected int x, y;
    protected int width, height;
    private Bitmap bm;
    protected Rect rect;


    public BaseObject() {

    }

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

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    //protected Bitmap createImageAt(int row, int col){}
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

    public void setRect(Rect rect){
    this.rect = rect;
    }
}
