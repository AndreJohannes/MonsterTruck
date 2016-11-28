package com.studiocinqo.monstertruck;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.studiocinqo.monstertruck.GameEngine.GameEngineFactory;
import com.studiocinqo.monstertruckandroidgl.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    // Our OpenGL Surfaceview
    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	super.onCreate(savedInstanceState);
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
	setContentView(R.layout.activity_main);
	glSurfaceView = (GLSurfaceView) findViewById(R.id.glsurf);
	ImageButton acceletatioButton = (ImageButton) findViewById(
		R.id.imageButton1);
	acceletatioButton.setOnTouchListener(new OnTouchListener() {
	    @SuppressLint("ClickableViewAccessibility")
	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
			|| event.getAction() == MotionEvent.ACTION_CANCEL) {
		    GameEngineFactory factory = GameEngineFactory.getInstance();
		    factory.getVehicle().setThrottle(12);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
		    GameEngineFactory factory = GameEngineFactory.getInstance();
		    factory.getVehicle().setThrottle(0);
		}
		return true;
	    }

	});
	ImageButton deacceletatioButton = (ImageButton) findViewById(
		R.id.imageButton2);
	deacceletatioButton.setOnTouchListener(new OnTouchListener() {
	    @SuppressLint("ClickableViewAccessibility")
	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
			|| event.getAction() == MotionEvent.ACTION_CANCEL) {
		    GameEngineFactory factory = GameEngineFactory.getInstance();
		    factory.getVehicle().setThrottle(-5);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
		    GameEngineFactory factory = GameEngineFactory.getInstance();
		    factory.getVehicle().setThrottle(0);
		}
		return true;
	    }

	});
    }

    @Override
    protected void onPause() {
	super.onPause();
	glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
	super.onResume();
	glSurfaceView.onResume();
    }

}
