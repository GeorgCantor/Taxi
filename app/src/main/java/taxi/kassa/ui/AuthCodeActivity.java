package taxi.kassa.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taxi.kassa.R;
import taxi.kassa.api.ApiManager;
import taxi.kassa.responses.ResponseAPI;
import taxi.kassa.responses.ResponseAuthSendCode;
import taxi.kassa.tools.Tools;
import taxi.kassa.ui.fragments.Fragments;

import static taxi.kassa.ui.MainActivity.api;

public class AuthCodeActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // view
        final View v = inflater.inflate(R.layout.auth_code, null);
        Button btn_code = v.findViewById(R.id.btn_complite);
        final EditText input_code = v.findViewById(R.id.input_code);
        final TextView tv_error = v.findViewById(R.id.tv_error);
        final Context context = v.getContext();
        // input
        input_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    sendCode(context, v);
                }
            }
        });
        // send code
        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                sendCode(context, v);
            }
        });
        // output
        return v;
    }

    public void sendCode(final Context context, final View v) {
        // vars
        final TextView tv_error = v.findViewById(R.id.tv_error);
        final EditText input_code = v.findViewById(R.id.input_code);
        Log.i("My Logs", "input " + input_code.toString());
        String code = input_code.getText().toString();
        // storage
        final SharedPreferences sp = context.getSharedPreferences("MainStorage", Context.MODE_PRIVATE);
        final String phone = sp.getString("phone", "");
        // validate
        if (code.equals("")) {
            Tools.showError(context, tv_error, "введите код", 5000, 0);
            return;
        }
        // api
        Call<ResponseAPI<ResponseAuthSendCode>> call = api.getCode(phone, code);
        call.enqueue(new Callback<ResponseAPI<ResponseAuthSendCode>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseAPI<ResponseAuthSendCode>> call, @NonNull Response<ResponseAPI<ResponseAuthSendCode>> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getSuccess()) {
                        String access_token = response.body().getResponse().getToken();
                        // storage
                        SharedPreferences sp = context.getSharedPreferences("MainStorage", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("access_token", access_token);
                        editor.apply();
                        ApiManager.access_token = access_token;
                        // view
                        clearBackStack();
                        Fragments.fragmentBalance(context);
                        Tools.hideKeyboard(v);
                    } else {
                        Log.i("My Logs", "error_msg: " + response.body().getErrorMsg());
                        Tools.showError(context, tv_error, response.body().getErrorMsg(), 5000, 0);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseAPI<ResponseAuthSendCode>> call, @NonNull Throwable t) {
            }
        });
    }

    private void clearBackStack() {
        FragmentManager manager = getFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}
