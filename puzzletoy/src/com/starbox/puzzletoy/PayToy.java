package com.starbox.puzzletoy;

public interface PayToy {

	void payClick();	
	String getAId();
	void showToast(final CharSequence str);
	int getAccuracy();

}
