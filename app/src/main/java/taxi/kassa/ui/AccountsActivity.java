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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taxi.kassa.R;
import taxi.kassa.ui.adapters.AccountsAdapter;
import taxi.kassa.ui.fragments.Fragments;
import taxi.kassa.responses.Account;
import taxi.kassa.responses.ResponseAPI;
import taxi.kassa.responses.AccountsList;

import static taxi.kassa.ui.MainActivity.api;

public class AccountsActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // view
        final View v = inflater.inflate(R.layout.accounts, null);
        final Context context = getActivity();
        final SwipeRefreshLayout upload_swipe_refresh_layout = v.findViewById(R.id.swipe_container);
        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Мои счета");
        // api
        getAccounts(context, v, true);
        // add account (button)
        getActivity().findViewById(R.id.account_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragments.fragmentCreateAccount(context);
            }
        });

        upload_swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAccounts(context, v, false);
                upload_swipe_refresh_layout.setRefreshing(false);
            }
        });

        // output
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        ImageButton account_add = getActivity().findViewById(R.id.account_add);
        if (account_add.getVisibility() == View.GONE) account_add.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        ImageButton account_add = getActivity().findViewById(R.id.account_add);
        if (account_add.getVisibility() == View.VISIBLE) account_add.setVisibility(View.GONE);
    }

    public void getAccounts(final Context context, final View v, final boolean progress) {
        // vars
        final RecyclerView rv = v.findViewById(R.id.accounts_list);
        // progress
        final ProgressBar progressBar = v.findViewById(R.id.progressBar);
        if (progress) progressBar.setVisibility(ProgressBar.VISIBLE);
        // call
        Call<ResponseAPI<AccountsList>> call = api.getAccounts();
        call.enqueue(new Callback<ResponseAPI<AccountsList>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseAPI<AccountsList>> call, @NonNull Response<ResponseAPI<AccountsList>> response) {
                // progress
                if (progress) progressBar.setVisibility(ProgressBar.GONE);
                if (response.code() == 200 && response.body() != null && response.body().getResponse() != null) {
                    if (response.body().getResponse().getCount() > 0) {
                        List<Account> ts_requests = response.body().getResponse().getInfo();
                        rv.setAdapter(new AccountsAdapter(context, ts_requests));
                    } else {
                        ConstraintLayout wrap = v.findViewById(R.id.accounts_root);
                        View inflatedLayout= LayoutInflater.from(context).inflate(R.layout.accounts_empty, (ViewGroup) v, false);
                        wrap.addView(inflatedLayout);
                        v.findViewById(R.id.btn_create_account).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                Fragments.fragmentCreateAccount(context);
                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseAPI<AccountsList>> call, Throwable t) {
            }
        });
    }

}
