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

public class AuthPhoneActivity extends Fragment {
    // vars
    static Boolean login_ready;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // vars
        login_ready = true;
        // view
        View v = inflater.inflate(R.layout.auth_phone, null);
        final Context context = getActivity();
        final EditText input_login = v.findViewById(R.id.input_login);
        final TextView tv_error = v.findViewById(R.id.tv_error);
        TextView tv_agreement = v.findViewById(R.id.tv_agreement);
        CheckBox login_checkbox = v.findViewById(R.id.login_checkbox);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        tv_agreement.setMovementMethod(LinkMovementMethod.getInstance());
        //input_login.setSelection(3);
        //input_login.clearFocus();
        // btn (checkbox)
        login_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                login_ready = isChecked;
            }
        });
        // btn (login)
        v.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (!login_ready) {
                    Tools.showError(context, tv_error, "необходимо принять условия работы", 5000, 0);
                    return;
                }

                String phone = input_login.getText().toString().replaceAll("[^\\d]", "");

                SharedPreferences sp = context.getSharedPreferences("MainStorage", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("phone", phone);
                editor.apply();

                Call<ResponseAPI<ResponseAuthSendPhone>> call = api.authSendPhone(phone);
                call.enqueue(new Callback<ResponseAPI<ResponseAuthSendPhone>>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseAPI<ResponseAuthSendPhone>> call, @NonNull Response<ResponseAPI<ResponseAuthSendPhone>> response) {
                        if (response.code() == 200 && response.body() != null) {
                            if (response.body().getSuccess()) {
                                Fragments.fragmentAuthCode(context);
                                Tools.hideKeyboard(v);
                            } else {
                                Tools.showError(context, tv_error, response.body().getErrorMsg(), 5000, 0);
                            }
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseAPI<ResponseAuthSendPhone>> call, @NonNull Throwable t) {
                    }
                });
            }
        });
        // input (login)
        input_login.addTextChangedListener(new TextWatcher() {
            int length_before = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                length_before = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input_login.length() <= 2) {
                    input_login.setText("+7 ");
                    input_login.setSelection(3);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (length_before < s.length()) {
                    if (s.length() == 6)
                        s.append(" ");
                    if (s.length() == 10 || s.length() == 13)
                        s.append("-");
                    if (s.length() > 6) {
                        if (Character.isDigit(s.charAt(6)))
                            s.insert(6, "-");
                    }
                    if (s.length() > 10) {
                        if (Character.isDigit(s.charAt(10)))
                            s.insert(10, "-");
                    }
                }
            }
        });

        // output
        return v;
    }

}


