package io.forus.me.android.presentation.view.screens.qr;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.journeyapps.barcodescanner.CaptureActivity;

public class CaptureActivityPortrait extends CaptureActivity {

    //static final int

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_BACK) {
            //Do something here
             Intent resultIntent = new Intent();
             resultIntent.putExtra("result", "back");
             setResult(Activity.RESULT_OK, resultIntent);
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }
}
