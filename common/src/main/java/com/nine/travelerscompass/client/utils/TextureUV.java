package com.nine.travelerscompass.client.utils;

public class TextureUV {

    private int x;
    private int y;

    public TextureUV(int x, int y){
        updatePos(x, y);
    }

    public TextureUV(){
    }

    public void updatePos(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void updatePosX(int x){
        this.x = x;
    }

    public void updatePosY(int y){
        this.y = y;
    }

    public void updatePos(TextureUV uv){
        this.x = uv.getX();
        this.y = uv.getY();
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
