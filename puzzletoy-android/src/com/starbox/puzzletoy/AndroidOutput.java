package com.starbox.puzzletoy;


import android.content.Context;
import android.os.Handler;
import android.widget.Toast;
public class AndroidOutput implements PayToy {

	
	
	Handler uiThread;
    Context appContext;
    AndroidLauncher parent;

   public AndroidOutput(Context appContext,AndroidLauncher parent) {
            uiThread = new Handler();
            this.appContext = appContext;
            this.parent = parent;
    }

	
	@Override
	public void payClick() {
		parent.onUpgradeClicked();
	}	
	

	@Override
	public String getAId() {
		return parent.getAId();		
	}

	@Override
	public void showToast(final CharSequence str) {
		uiThread.post(new Runnable() {
            public void run() {
                    Toast.makeText(appContext, str, Toast.LENGTH_SHORT).show();
            }
    });

	}

	@Override
	public int getAccuracy() {
		return parent.getAccuracy();
	}

}
