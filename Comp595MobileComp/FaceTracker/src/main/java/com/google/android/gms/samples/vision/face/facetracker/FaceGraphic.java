/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.samples.vision.face.facetracker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.google.android.gms.samples.vision.face.facetracker.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.face.Face;

/**
 * Graphic instance for rendering face position, orientation, and landmarks within an associated
 * graphic overlay view.
 */
class FaceGraphic extends GraphicOverlay.Graphic {
    private static final float FACE_POSITION_RADIUS = 10.0f;
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private static final int COLOR_CHOICES[] = {
        Color.BLUE,
        Color.CYAN,
        Color.GREEN,
        Color.MAGENTA,
        Color.RED,
        Color.WHITE,
        Color.YELLOW
    };
    private static int mCurrentColorIndex = 0;

    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    private Paint mBoxPaint;

    private volatile Face mFace;
    private int mFaceId;

    //testing to see if head shakes
    private boolean prev_left = false;
    private boolean prev_right = false;
    private boolean prev_up = false;
    private boolean prev_down = false;
    private float xStart, yStart;
    private boolean hasXYStart;
    private int messageCounterShake = 0;//keep the drawing on the screen for count of 50 updates
    private int messageCounterNod = 0;//keep the drawing on the screen for count of 50 updates


    FaceGraphic(GraphicOverlay overlay) {
        super(overlay);

//        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = COLOR_CHOICES[2];//set to green for now

        mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(selectedColor);

        mIdPaint = new Paint();
        mIdPaint.setColor(selectedColor);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(selectedColor);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);

        hasXYStart = false;
    }

    void setId(int id) {
        mFaceId = id;
    }


    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    void updateFace(Face face) {
        mFace = face;
        postInvalidate();
    }

    /**
     * Draws the face annotations for position on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) {
            return;
        }


        // Draws a circle at the position of the detected face, with the face's track id below.
        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);

        //get initial position of x and y to have a base point for head moving
        //only initializes at the first run
        if(!hasXYStart){
            xStart = x;
            yStart = y;
            hasXYStart = true;
            Log.w("ShakeStart", "xStart: " + xStart + " yStart: " + yStart);
        }

        // Draws a bounding box around the face.
        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f);
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;
        canvas.drawRect(left, top, right, bottom, mBoxPaint);

        /************Shaking head No********************/
        if (messageCounterShake > 0) {
            canvas.drawText("Shook your head no!", left, bottom + 200, mIdPaint);
            messageCounterShake--;//decrement counter
            Log.w("ShakingHead", "Message Counter: " + messageCounterShake);
        }
        /***************Nodding head Yes********************/
        else if (messageCounterNod > 0){
            canvas.drawText("Nodded your head yes!", left, bottom + 200, mIdPaint);
            messageCounterNod--;
        }
        else {
            if(messageCounterNod == 0)
                shookHead(x);
            if(messageCounterShake == 0)
                noddedHead(y);

            Log.w("ShakingHead", "Booleans for right: " + prev_right);
            Log.w("ShakingHead",  "Boolean for left:" + prev_left);
        }



    }

    /*
    * Checks the X axis to see if the head is turned right and then left
    */
    private void shookHead(float x){
        /*
        * checks the current x with the start.  If the current x is larger than the start and then
        * lower than the start both will be true and start the time at 50
        * once the time is decremented to 0, it will be reset
        * */
        if(x > xStart + 50)
            prev_right = true;
        if(x < xStart + 50)
            prev_left = true;

        if(prev_left && prev_right) {//head was shook left and right
            messageCounterShake = 50;
            prev_left = false;
            prev_right = false;
        }
    }

    private void noddedHead(float y){

        if(y > yStart + 50 )
            prev_up = true;
        if(y < yStart + 50 )
            prev_down = true;

        if(prev_up && prev_down){
            messageCounterNod = 50;
            prev_down = false;
            prev_up = false;
        }
    }
}
