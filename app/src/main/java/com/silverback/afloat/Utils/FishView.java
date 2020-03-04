package com.silverback.afloat.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.silverback.afloat.R;

public class FishView extends View {

    private static final String TAG = "FishView";

    //use images in a Bitmap format
    private Bitmap backgroundImg;
    private Bitmap fish[] = new Bitmap[2];      //2 fish images to help animate the fish
    private Bitmap badGuy[] = new Bitmap[5];
    private Bitmap life[] = new Bitmap[2];      //2 life images, to show lost life
    private Bitmap weed[] = new Bitmap[5];

    //holds style and color info on how to draw text, bitmap and geometries
    private Paint paintScore = new Paint();

    //coordinates and speed of my character fish
    private int fishX=10, fishY, fishSpeed;

    //coordinates and speed of a weed
    private int weedX, weedY, weedSpeed=5;

    //coordinates and speed of a weed
    private int badFishX, badFishY, badFishSpeed=8;

    //dimensions of the mobile screen
    private int canvasWidth, canvasHeight;

    //flag that checks if a screen is tapped
    private Boolean tapScreen = false;


    //constructor
    public FishView(Context context) {
        super(context);
        Log.d(TAG, "FishView: fishView started");

        //sets all the image resources as Bitmap
        setBitmapImages();

        //how to draw the score
        paintScore.setColor(Color.WHITE);
        paintScore.setTextSize(70);
        paintScore.setTypeface(Typeface.DEFAULT_BOLD);
        paintScore.setAntiAlias(true);

        fishY = 550;    //height of fish on start
        badFishY = (int) Math.floor(Math.random()*750) + 550;
        badFishX = canvasWidth+20;
        weedX = 0;
    }       //end FishView()


    //set all images in Bitmap
    public void setBitmapImages(){

        //get the sea background image
        backgroundImg = BitmapFactory.decodeResource(getResources(), R.drawable.background);

        //get the fish image as a Bitmap image
        fish[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fish1);
        fish[1] = BitmapFactory.decodeResource(getResources(), R.drawable.fish2);

        //get the bad fish image Bitmap image
        badGuy[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bad_fish1);
        badGuy[1] = BitmapFactory.decodeResource(getResources(), R.drawable.bad_fish2);
        badGuy[2] = BitmapFactory.decodeResource(getResources(), R.drawable.bad_fish3);
        badGuy[3] = BitmapFactory.decodeResource(getResources(), R.drawable.bad_fish4);
        badGuy[4] = BitmapFactory.decodeResource(getResources(), R.drawable.bad_fish_front);

        //get the sea weeds images as Bitmap
        weed[0] = BitmapFactory.decodeResource(getResources(), R.drawable.weed1);
        weed[1] = BitmapFactory.decodeResource(getResources(), R.drawable.weed2);
        weed[2] = BitmapFactory.decodeResource(getResources(), R.drawable.weed3);
        weed[3] = BitmapFactory.decodeResource(getResources(), R.drawable.weed4);
        weed[4] = BitmapFactory.decodeResource(getResources(), R.drawable.weed5);

        //get the life images as bitmap
        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_grey);

    }       //end setBitmapImages()


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //get coordinates at max point on the canvas
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        //draw the background on the canvas
        canvas.drawBitmap(backgroundImg, 0, 0, null);

        drawLives(canvas);       //draws my life images to canvas
        drawCharacter(canvas);  //draws the main character on canvas
        drawBadFish(canvas);    //draws bad fish to canvas

        //drawWeeds(canvas);      //draws weeds to canvas

    }       //end onDraw()


    //draws my life images to canvas
    public void drawLives(Canvas canvas){

        //draws score images on canvas, with the paint properties
        canvas.drawText("Score: ", 20, 60, paintScore);     //draw the score title text on the canvas
        canvas.drawBitmap(life[0], 580, 10, null);      //draw the life icon on the canvas, X3
        canvas.drawBitmap(life[0], 680, 10, null);
        canvas.drawBitmap(life[0], 780, 10, null);

    }       //end drawLives()


    //draws my character fish to canvas
    public void drawCharacter(Canvas canvas){
        //extremes of the fish coordinates
        int minFishY = fish[0].getHeight();
        int maxFishY = canvasHeight-fish[0].getHeight();
        fishY = fishY + fishSpeed;

        //restrict fish: don't go above minimum point
        if(fishY<minFishY){
            fishY = minFishY;
        }

        //restrict fish: don't go below max point
        if(fishY>maxFishY){
            fishY = maxFishY;
        }
        fishSpeed=fishSpeed+2;

        //animate the character
        if(tapScreen){
            canvas.drawBitmap(fish[1], fishX,fishY, null);
            tapScreen=false;
        }
        else{
            canvas.drawBitmap(fish[0],fishX,fishY,null );
        }

        if(fishCaught(badFishX, badFishY)){
            int counter = 0;
            Log.i(TAG, "drawCharacter: caught: "+(++counter));
        }

    }       //end drawCharacter()


    //draws weeds to canvas
    public void drawWeeds(Canvas canvas){

        int weed0Y = canvasHeight-weed[0].getHeight();
        int weed1Y = canvasHeight-weed[1].getHeight();
        int weed2Y = canvasHeight-weed[2].getHeight();
        int weed3Y = canvasHeight-weed[3].getHeight();
        int weed4Y = canvasHeight-weed[4].getHeight();

        weedX = weedX - weedSpeed;
        if(weedX < 0-weed[0].getWidth()){
            weedX = canvasWidth+100;
        }

        canvas.drawBitmap(weed[0], weedX, weed0Y, null);
        canvas.drawBitmap(weed[1], weedX+200, weed1Y, null);
        canvas.drawBitmap(weed[2], weedX+500, weed2Y, null);
        canvas.drawBitmap(weed[3], weedX+800, weed3Y, null);
        canvas.drawBitmap(weed[4], weedX+1100, weed4Y, null);

    }       //end drawWeeds()


    //draws bad fish to canvas
    public void drawBadFish(Canvas canvas){

        badFishX = badFishX - badFishSpeed;

        if(badFishX < 0-badGuy[0].getWidth()){
            badFishX = canvasWidth+100;
            badFishY = (int) Math.floor(Math.random()*750) + 550;
        }

        canvas.drawBitmap(badGuy[0], badFishX, badFishY, null);
/*        canvas.drawBitmap(badGuy[1], 780, 750, null);
        canvas.drawBitmap(badGuy[2], 780, 950, null);
        canvas.drawBitmap(badGuy[3], 780, 1150, null);*/
        //canvas.drawBitmap(badGuy[4], 180, 750, null);

    }       //end drawBadFish()



    public boolean fishCaught(int x, int y){
        if(fishX<x && x<(fishX+fish[0].getWidth()) && fishY<y && y<(fishY+fish[0].getHeight())){
            return true;
        }

        return false;
    }       //end fishCaught()



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            tapScreen=true;         //flag to help us detect user action

            fishSpeed = -20;        //fish keeps going downward
        }

        return true;
    }       //end onTouchEvent()


}       //end class
