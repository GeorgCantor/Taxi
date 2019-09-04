package taxi.kassa.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taxi.kassa.R;
import taxi.kassa.ui.adapters.WithdrawsAdapter;
import taxi.kassa.responses.ResponseAPI;
import taxi.kassa.responses.Withdraw;
import taxi.kassa.responses.Withdraws;

import static taxi.kassa.ui.MainActivity.api;

public class WithdrawsActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // view
        final View v = inflater.inflate(R.layout.withdrawals, null);
        final Context context = getActivity();
        final SwipeRefreshLayout upload_swipe_refresh_layout = v.findViewById(R.id.swipe_container);
        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Заявки на вывод");
        // api
        getWithdraws(context, v);

        upload_swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWithdraws(context, v);
                upload_swipe_refresh_layout.setRefreshing(false);
            }
        });

        // output
        return v;
    }

    public void getWithdraws(final Context context, final View v) {
        final RecyclerView rv = v.findViewById(R.id.ts_requests);
        Call<ResponseAPI<Withdraws>> call = api.getWithdraws();
        call.enqueue(new Callback<ResponseAPI<Withdraws>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseAPI<Withdraws>> call, @NonNull Response<ResponseAPI<Withdraws>> response) {
                if (response.code() == 200 && response.body() != null && response.body().getResponse() != null) {
                    if (response.body().getResponse().getCount() > 0) {
                        List<Withdraw> ts_requests = response.body().getResponse().getInfo();
                        rv.setAdapter(new WithdrawsAdapter(context, ts_requests));
                    } else {
                        ConstraintLayout wrap = v.findViewById(R.id.withdrawals_root);
                        View inflatedLayout= LayoutInflater.from(context).inflate(R.layout.withdrawals_empty, (ViewGroup) v, false);
                        wrap.addView(inflatedLayout);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseAPI<Withdraws>> call, @NonNull Throwable t) {
            }
        });
    }
}
