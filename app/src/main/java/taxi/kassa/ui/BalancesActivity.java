package taxi.kassa.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taxi.kassa.R;
import taxi.kassa.ui.fragments.Fragments;
import taxi.kassa.responses.ResponseAPI;
import taxi.kassa.responses.ResponseOwner;

import static taxi.kassa.ui.MainActivity.api;

public class BalancesActivity extends Fragment {

    View v;
    TextView balance_city_value, balance_gett_value, balance_yandex_value, balance_total_value;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        // view
        v = inflater.inflate(R.layout.balances, container, false);
        final Context context = getActivity();
        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Балансы");
        // city
        v.findViewById(R.id.balance_city_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragments.fragmentCreateTransactionRequest(context, 2);
            }
        });
        // gett
        v.findViewById(R.id.balance_gett_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragments.fragmentCreateTransactionRequest(context, 3);
            }
        });
        // yandex
        v.findViewById(R.id.balance_yandex_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragments.fragmentCreateTransactionRequest(context, 1);
            }
        });
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        balance_city_value = v.findViewById(R.id.balance_city_value);
        balance_gett_value = v.findViewById(R.id.balance_gett_value);
        balance_yandex_value = v.findViewById(R.id.balance_yandex_value);
        balance_total_value = v.findViewById(R.id.balance_total_value);

        // storage
        //SharedPreferences sp = getActivity().getSharedPreferences("MainStorage", Context.MODE_PRIVATE);
        //String old_balance_total = sp.getString("old_balance_total", "");
        //Log.i("My Log", "curr value: " + old_balance_total + " \u20BD");
        //if (old_balance_total != "") balance_total_value.setText(old_balance_total + " \u20BD");

        // output
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        // vars
        final Context context = getActivity();
        final SwipeRefreshLayout upload_swipe_refresh_layout = v.findViewById(R.id.swipe_container);
        // api
        getBalances(context);
        // swipe
        upload_swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBalances(context);
                upload_swipe_refresh_layout.setRefreshing(false);
            }
        });
    }

    public void getBalances(final Context context) {
        // call
        Call<ResponseAPI<ResponseOwner>> call = api.getOwner();
        call.enqueue(new Callback<ResponseAPI<ResponseOwner>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseAPI<ResponseOwner>> call, @NonNull Response<ResponseAPI<ResponseOwner>> response) {
                if (response.code() == 200 && response.body() != null && response.body().getResponse() != null) {
                    // info
                    ResponseOwner body = response.body().getResponse();
                    String city = body.getBalanceCity();
                    String gett = body.getBalanceGett();
                    String yandex = body.getBalanceYandex();
                    String total = body.getBalanceTotal();
                    String driver_name = body.getFullName();
                    String driver_phone = body.getPhone();
                    // update
                    if (balance_city_value != null) balance_city_value.setText(city + " \u20BD");
                    if (balance_gett_value != null) balance_gett_value.setText(gett + " \u20BD");
                    if (balance_yandex_value != null) balance_yandex_value.setText(yandex + " \u20BD");
                    if (balance_total_value != null) balance_total_value.setText(total + " \u20BD");
                    // storage
                    //SharedPreferences sp = getActivity().getSharedPreferences("MainStorage", Context.MODE_PRIVATE);
                    //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    //SharedPreferences.Editor editor = sp.edit();
                    //editor.putString("old_balance_total", total);
                    //editor.apply();
                    // sidebar
                    if (context != null) {
                        TextView tv_driver_name = ((Activity) context).findViewById(R.id.tv_driver_name);
                        TextView tv_driver_phone = ((Activity) context).findViewById(R.id.tv_driver_phone);
                        if (tv_driver_name != null) tv_driver_name.setText(driver_name);
                        if (tv_driver_phone != null) tv_driver_phone.setText("+" + driver_phone);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseAPI<ResponseOwner>> call, @NonNull Throwable t) {
            }
        });
    }
}

