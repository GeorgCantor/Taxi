package taxi.kassa.ui.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taxi.kassa.R;
import taxi.kassa.responses.Account;
import taxi.kassa.responses.ResponseAPI;
import taxi.kassa.responses.ResponseSimple;

import static taxi.kassa.ui.MainActivity.api;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Account> accounts_list;
    private Context context;

    public AccountsAdapter(Context context, List<Account> accounts_list) {
        this.accounts_list = accounts_list;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView bank_name, account, driver_name;
        final ImageButton btn_detele;
        ViewHolder(View view){
            super(view);
            bank_name = view.findViewById(R.id.bank_name);
            account = view.findViewById(R.id.account);
            driver_name = view.findViewById(R.id.driver_name);
            btn_detele = view.findViewById(R.id.btn_delete);
        }
    }
    @NonNull
    @Override
    public AccountsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.account_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AccountsAdapter.ViewHolder holder, final int position) {
        final Account info = accounts_list.get(position);
        final Boolean readonly = info.getReadonly();
        // info
        holder.bank_name.setText(info.getBankName());
        holder.account.setText("Р/с: " + info.getAccountNumber());
        holder.driver_name.setText(info.getDriverName());
        // btn delete
        if (!readonly) {
            holder.btn_detele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            //.setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Удаление счета")
                            .setMessage("Вы действительно хотите удалить этот счет?")
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int account_id = info.getId();
                                    // api
                                    Call<ResponseAPI<ResponseSimple>> call = api.deleteAccount(account_id);
                                    call.enqueue(new Callback<ResponseAPI<ResponseSimple>>() {
                                        @Override
                                        public void onResponse(@NonNull Call<ResponseAPI<ResponseSimple>> call, @NonNull Response<ResponseAPI<ResponseSimple>> response) {
                                            if (response.code() == 200 && response.body() != null) {
                                                accounts_list.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position, accounts_list.size());
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<ResponseAPI<ResponseSimple>> call, @NonNull Throwable t) {
                                        }
                                    });
                                }

                            })
                            .setNegativeButton("Нет", null)
                            .show();
                }
            });
        } else {
            holder.btn_detele.setVisibility(ProgressBar.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return accounts_list.size();
    }

}
