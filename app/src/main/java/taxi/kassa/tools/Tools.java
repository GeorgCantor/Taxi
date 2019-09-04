package taxi.kassa.tools;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import taxi.kassa.R;

public class Tools {

    public static void showError(final Context context, final TextView tv, String message, int hide, final int gone) {
        if (gone == 1) tv.setVisibility(ProgressBar.VISIBLE);
        tv.setText(message);
        Animation a = AnimationUtils.loadAnimation(context, R.animator.fade_in);
        a.reset();
        tv.clearAnimation();
        tv.startAnimation(a);
        if (hide > 0) {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            Animation a = AnimationUtils.loadAnimation(context, R.animator.fade_out);
                            a.reset();
                            tv.clearAnimation();
                            tv.startAnimation(a);
                        }
                    },
                    3000);
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            tv.setText("");
                            if (gone == 1) tv.setVisibility(ProgressBar.GONE);
                        }
                    },
                    3350);
        }
    }

    public static void hideKeyboard(View v) {
        InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        IBinder binder = v.getWindowToken();
        inputManager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
