package taxi.kassa.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import taxi.kassa.R;
import taxi.kassa.ui.fragments.Fragments;

public class RequestCreateSuccessActivity extends Fragment {
    View v;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // view
        final Context context = getActivity();
        v = inflater.inflate(R.layout.signup_create_success, null);
        v.findViewById(R.id.btn_back_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Fragments.fragmentIntro(context);
            }
        });
        // output
        return v;
    }
}