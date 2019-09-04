package taxi.kassa.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taxi.kassa.R;
import taxi.kassa.responses.ResponseAPI;
import taxi.kassa.responses.ResponseAuthSendPhone;
import taxi.kassa.responses.ResponseCreateRequest;
import taxi.kassa.tools.Tools;
import taxi.kassa.ui.fragments.Fragments;

import static taxi.kassa.ui.MainActivity.api;

public class IntroActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // view
        View v = inflater.inflate(R.layout.intro, null);
        final Context context = getActivity();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        // btn (login)
        v.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Fragments.fragmentAuthPhone(context);
            }
        });
        // btn (signup)
        v.findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Fragments.fragmentAuthSignup(context);
            }
        });

        // output
        return v;
    }

}


