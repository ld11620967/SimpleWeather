package com.mob.cms.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.mob.cms.gui.CMSGUI;
import com.mob.cms.gui.themes.defaultt.DefaultTheme;
import com.mob.tools.utils.DeviceHelper;

public class MainActivity extends Activity implements OnClickListener {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0xffffffff);
		ll.setOrientation(LinearLayout.VERTICAL);
		setContentView(ll);
		
		int[] resIds = {
				R.string.use_umssdk,
				R.string.use_custom,
				R.string.use_anonymous
		};
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		FrameLayout.LayoutParams lpfl = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lpfl.gravity = Gravity.CENTER;
		for (int resId : resIds) {
			FrameLayout fl = new FrameLayout(this);
			ll.addView(fl, lp);
			
			Button btn = new Button(this);
			btn.setMinEms(10);
			btn.setId(resId);
			btn.setText(resId);
			btn.setOnClickListener(this);
			btn.setSingleLine();
			fl.addView(btn, lpfl);
		}
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
			case R.string.use_umssdk: {
				CMSGUI.setTheme(DefaultTheme.class);
				// to use this entrance, you have to integrate UMSSDK first
				CMSGUI.showNewsListPageWithUMSSDKUser(com.mob.ums.gui.themes.defaultt.DefaultTheme.class);
			} break;
			case R.string.use_custom: {
				CMSGUI.setTheme(DefaultTheme.class);
				// this entrance will allow you to input your own user id, nickname and avatar to CMSSDK
				String uid = DeviceHelper.getInstance(this).getSerialno();
				String nickname = DeviceHelper.getInstance(this).getAppName() + " Player";
				String avatarUrl = "http://www.mob.com/public/images/logo_black.png";
				CMSGUI.showNewsListPageWithCustomUser(uid, nickname, avatarUrl);
			} break;
			case R.string.use_anonymous: {
				CMSGUI.setTheme(DefaultTheme.class);
				// this entrance will create an anonymous user for CMSSDK
				CMSGUI.showNewsListPageWithAnonymousUser();
			} break;
		}
	}
	
}
