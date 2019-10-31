package com.pbasket.yellow.basketball;

//import com.airpush.android.Airpush;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;

import com.pbasket.yellow.R;

public class Start extends FragmentActivity
{
	int _keyCode = 0;
	GameRenderer mGR = null;
	private static Context CONTEXT;


	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);//OnlyOneChange
		CONTEXT	=	this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

		WindowManager mWinMgr = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		M.ScreenWidth =mWinMgr.getDefaultDisplay().getWidth();
		M.ScreenHieght=mWinMgr.getDefaultDisplay().getHeight();
		mGR=new GameRenderer(this);
	    VortexView glSurface= findViewById(R.id.vortexview); // use the xml to set the view
	    glSurface.setRenderer(mGR);
	    glSurface.showRenderer(mGR);
	}

	public static Context getContext() {
	        return CONTEXT;
	}

	@Override 
	public void onPause () {
		M.stop(CONTEXT);
		pause();
		super.onPause();
	}
	@Override 
	public void onResume() {
		resume();
		super.onResume();
		//view.onResume();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Log.d("----------------=>  "+keyCode,"   -----------    ");
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{

			switch (M.GameScreen) {
			case M.GAMEPLAY:
				M.GameScreen = M.GAMEPAUSE;
				mGR.gametime = System.currentTimeMillis() - mGR.gametime;
				M.stop(GameRenderer.mContext);
				break;
			case M.GAMEMENU:
				Exit();
				break;
			default:
				M.GameScreen = M.GAMEMENU;
				break;
			}
			return false;
		}
		_keyCode = keyCode;
		return super.onKeyDown(keyCode,event); 
	}  
	public boolean onKeyUp(int keyCode, KeyEvent event) 
	{
//		if(keyCode==KeyEvent.KEYCODE_BACK)
//			return false;
		_keyCode = 0;
		return super.onKeyUp( keyCode, event ); 
	}
	public void onDestroy()
	{
		super.onDestroy();

	}

	void pause() {
		mGR.resumeCounter = 0;
		M.stop(GameRenderer.mContext);
		SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt("screen", M.GameScreen);
		editor.putBoolean("setValue", M.setValue);

		{
			editor.putBoolean("Ballmove", mGR.mBall.move);
			editor.putInt("Ballrev", mGR.mBall.rev);
			editor.putInt("BallBounce", mGR.mBall.Bounce);
			editor.putInt("Ballcollide", mGR.mBall.collide);
			editor.putInt("Ballanim", mGR.mBall.anim);
			editor.putInt("BallmBalls", mGR.mBall.mBalls);
			editor.putInt("Balltype", mGR.mBall.type);
			editor.putInt("BallDrap", mGR.mBall.Drap);
			editor.putInt("Ballcombo", mGR.mBall.combo);
			editor.putInt("Ballxtime", mGR.mBall.xtime);

			editor.putFloat("Ballx", mGR.mBall.x);
			editor.putFloat("Bally", mGR.mBall.y);
			editor.putFloat("Ballz", mGR.mBall.z);
			editor.putFloat("Ballsx", mGR.mBall.sx);
			editor.putFloat("Ballsy", mGR.mBall.sy);
			editor.putFloat("Ballvx", mGR.mBall.vx);
			editor.putFloat("Ballvy", mGR.mBall.vy);
			editor.putFloat("Ballvz", mGR.mBall.vz);
			editor.putFloat("Ballang", mGR.mBall.ang);
			editor.putFloat("Balltx", mGR.mBall.tx);
			editor.putFloat("Ballty", mGR.mBall.ty);
		}
		{
			editor.putInt("TimeAnino", mGR.mTimeAni.no);
			editor.putFloat("TimeAnix", mGR.mTimeAni.x);
			editor.putFloat("TimeAniy", mGR.mTimeAni.y);
		}
		{
			editor.putInt("ComboAnino", mGR.mComboAni.no);
			editor.putFloat("ComboAnix", mGR.mComboAni.x);
			editor.putFloat("ComboAniy", mGR.mComboAni.y);
		}
		for (int i = 0; i < mGR.mAni.length; i++) {
			editor.putInt("Anino" + i, mGR.mAni[i].no);
			editor.putFloat("Anix"+i, mGR.mAni[i].x);
			editor.putFloat("Aniy"+i, mGR.mAni[i].y);
		}

		editor.putBoolean("addFree", mGR.addFree);
		editor.putBoolean("SingUpadate", mGR.SingUpadate);
		for (int i = 0; i < mGR.Achi.length; i++)
			editor.putBoolean("Achi" + i, mGR.Achi[i]);

		editor.putInt("mScore", mGR.mScore);
		editor.putInt("mTScore", mGR.mTScore);
		for (int i = 0; i < mGR.mBest.length; i++){
			editor.putInt("mBest"+i, mGR.mBest[i]);
		}
		editor.putInt("ads", mGR.ads);
		editor.putLong("gametime", mGR.gametime);

		editor.commit();
	}
	void resume()
	{
		SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
		M.GameScreen = prefs.getInt("screen", M.GAMELOGO);
		M.setValue = prefs.getBoolean("setValue", M.setValue);
		
		{
			mGR.mBall.move = prefs.getBoolean("Ballmove", mGR.mBall.move);

			mGR.mBall.rev = (byte)prefs.getInt("Ballrev", mGR.mBall.rev);
			mGR.mBall.Bounce = prefs.getInt("BallBounce", mGR.mBall.Bounce);
			mGR.mBall.collide = prefs.getInt("Ballcollide", mGR.mBall.collide);
			mGR.mBall.anim = prefs.getInt("Ballanim", mGR.mBall.anim);
			mGR.mBall.mBalls = prefs.getInt("BallmBalls", mGR.mBall.mBalls);
			mGR.mBall.type = prefs.getInt("Balltype", mGR.mBall.type);
			mGR.mBall.Drap = prefs.getInt("BallDrap", mGR.mBall.Drap);
			mGR.mBall.combo = prefs.getInt("Ballcombo", mGR.mBall.combo);
			mGR.mBall.xtime = prefs.getInt("Ballxtime", mGR.mBall.xtime);

			mGR.mBall.x = prefs.getFloat("Ballx", mGR.mBall.x);
			mGR.mBall.y = prefs.getFloat("Bally", mGR.mBall.y);
			mGR.mBall.z = prefs.getFloat("Ballz", mGR.mBall.z);
			mGR.mBall.sx = prefs.getFloat("Ballsx", mGR.mBall.sx);
			mGR.mBall.sy = prefs.getFloat("Ballsy", mGR.mBall.sy);
			mGR.mBall.vx = prefs.getFloat("Ballvx", mGR.mBall.vx);
			mGR.mBall.vy = prefs.getFloat("Ballvy", mGR.mBall.vy);
			mGR.mBall.vz = prefs.getFloat("Ballvz", mGR.mBall.vz);
			mGR.mBall.ang = prefs.getFloat("Ballang", mGR.mBall.ang);
			mGR.mBall.tx = prefs.getFloat("Balltx", mGR.mBall.tx);
			mGR.mBall.ty = prefs.getFloat("Ballty", mGR.mBall.ty);
		}
		{
			mGR.mTimeAni.no = prefs.getInt("TimeAnino", mGR.mTimeAni.no);
			mGR.mTimeAni.x = prefs.getFloat("TimeAnix", mGR.mTimeAni.x);
			mGR.mTimeAni.y = prefs.getFloat("TimeAniy", mGR.mTimeAni.y);
		}
		{
			mGR.mComboAni.no = prefs.getInt("ComboAnino", mGR.mComboAni.no);
			mGR.mComboAni.x = prefs.getFloat("ComboAnix", mGR.mComboAni.x);
			mGR.mComboAni.y = prefs.getFloat("ComboAniy", mGR.mComboAni.y);
		}
		for (int i = 0; i < mGR.mAni.length; i++) {
			mGR.mAni[i].no = prefs.getInt("Anino" + i, mGR.mAni[i].no);
			mGR.mAni[i].x = prefs.getFloat("Anix"+i, mGR.mAni[i].x);
			mGR.mAni[i].y = prefs.getFloat("Aniy"+i, mGR.mAni[i].y);
		}

		mGR.addFree = prefs.getBoolean("addFree", mGR.addFree);
		mGR.SingUpadate = prefs.getBoolean("SingUpadate", mGR.SingUpadate);
		for (int i = 0; i < mGR.Achi.length; i++)
			mGR.Achi[i] = prefs.getBoolean("Achi" + i, mGR.Achi[i]);

		mGR.mScore = prefs.getInt("mScore", mGR.mScore);
		mGR.mTScore = prefs.getInt("mTScore", mGR.mTScore);
		for (int i = 0; i < mGR.mBest.length; i++){
			mGR.mBest[i] = prefs.getInt("mBest"+i, mGR.mBest[i]);
		}
		mGR.ads = prefs.getInt("ads", mGR.ads);
		mGR.gametime = prefs.getLong("gametime", mGR.gametime);
		
		
	    mGR.resumeCounter = 0;
	}
	void Exit()
	{
	   new AlertDialog.Builder(this).setTitle("Do you want to Exit?")
	   .setNeutralButton("No",new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int which) {
      }}).setNegativeButton("Yes",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog, int which) {
		    		   finish();M.GameScreen=M.GAMELOGO;mGR.root.Counter =0;
      }}).show();
  }
	



}