package taxi.kassa.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taxi.kassa.R;
import taxi.kassa.responses.ResponseSimple;
import taxi.kassa.tools.Tools;
import taxi.kassa.ui.adapters.AccountsSpinnerAdapter;
import taxi.kassa.ui.fragments.Fragments;
import taxi.kassa.responses.Account;
import taxi.kassa.responses.AccountsList;
import taxi.kassa.responses.ResponseAPI;

import static taxi.kassa.ui.MainActivity.api;

public class WithdrawCreateActivity extends Fragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // view
        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Новая заявка");
        v = inflater.inflate(R.layout.withdrawal_create, null);
        // params
        Bundle params = this.getArguments();
        final int source_id = (params != null) ? params.getInt("source_id") : 1;
        // list view
        final Context context = getActivity();
        // api
        Call<ResponseAPI<AccountsList>> call = api.getAccounts();
        call.enqueue(new Callback<ResponseAPI<AccountsList>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseAPI<AccountsList>> call, @NonNull Response<ResponseAPI<AccountsList>> response) {
                if (response.code() == 200 && response.body() != null && response.body().getResponse() != null) {
                    List<Account> ts_requests = response.body().getResponse().getInfo();
                    if (ts_requests.size() == 0) {
                        //Fragments.fragmentCreateTransactionRequestEmpty(context);
                        LinearLayout wrap = v.findViewById(R.id.withdraw_create_root);
                        wrap.removeAllViews();
                        View inflatedLayout= LayoutInflater.from(context).inflate(R.layout.accounts_empty, (ViewGroup) v, false);
                        wrap.addView(inflatedLayout);
                        v.findViewById(R.id.btn_create_account).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                Fragments.fragmentCreateAccount(context);
                            }
                        });
                    } else {
                        final Spinner spinner = v.findViewById(R.id.accounts_list);
                        Account[] accounts = new Account[ts_requests.size()];
                        int i = 0;
                        for (Account o : ts_requests) {
                            accounts[i] = new Account(o.getId(), o.getAccountNumber(), o.getAccountCorr(), o.getBankName(), o.getDriverName(), o.getCardName(), o.getReadonly());
                            i++;
                        }
                        final AccountsSpinnerAdapter adapter = new AccountsSpinnerAdapter(context, R.layout.spinner_item, accounts);
                        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
                        spinner.setAdapter(adapter);
                        // complite
                        Button btn_complite = v.findViewById(R.id.btn_complite);
                        btn_complite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // vars
                                EditText input_amount = v.findViewById(R.id.amount);
                                final TextView tv_error = v.findViewById(R.id.tv_error);
                                String amount = input_amount.getText().toString();
                                Spinner spinner = v.findViewById(R.id.accounts_list);
                                Account tmp = (Account) spinner.getSelectedItem();
                                final int account_id = tmp.getId();
                                // validate
                                if (Objects.equals(amount, "")) {
                                    Tools.showError(context, tv_error, "введите сумму", 5000, 0);
                                    return;
                                }
                                // api
                                Call<ResponseAPI<ResponseSimple>> call = api.createWithdraw(source_id, amount, account_id);
                                call.enqueue(new Callback<ResponseAPI<ResponseSimple>>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ResponseAPI<ResponseSimple>> call, @NonNull Response<ResponseAPI<ResponseSimple>> response) {
                                        if (response.code() == 200 && response.body() != null) {
                                            // vars
                                            assert response.body() != null;
                                            if (response.body().getSuccess()) {
                                                Tools.hideKeyboard(v);
                                                Fragments.fragmentWithdraws(context);
                                            } else {
                                                Tools.showError(context, tv_error, response.body().getErrorMsg(), 8000, 0);
                                            }
                                        }
                                    }
                                    @Override
                                    public void onFailure(@NonNull Call<ResponseAPI<ResponseSimple>> call, @NonNull Throwable t) {
                                    }
                                });
                            }
                        });
                    }

                }

            }
            @Override
            public void onFailure(@NonNull Call<ResponseAPI<AccountsList>> call, @NonNull Throwable t) {
            }
        });
        return v;
    }
}

