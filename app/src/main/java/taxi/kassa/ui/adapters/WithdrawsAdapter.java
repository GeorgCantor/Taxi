package taxi.kassa.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import taxi.kassa.R;
import taxi.kassa.responses.Withdraw;

public class WithdrawsAdapter extends RecyclerView.Adapter<WithdrawsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Withdraw> ts_requests;
    private Context context;

    public WithdrawsAdapter(Context context, List<Withdraw> ts_requests) {
        this.ts_requests = ts_requests;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public WithdrawsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.withdrawal_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(WithdrawsAdapter.ViewHolder holder, int position) {
        final Withdraw info = ts_requests.get(position);
        // date
        String date = info.getDate();
        // source
        int source = Integer.parseInt(info.getSource_id());
        String source_title = "";
        if (source == 2) source_title = "City Такси";
        else if (source == 3) source_title = "Gett Такси";
        else if (source == 1) source_title = "Яндекс.Такси";
        // amount
        String amount = info.getAmount() + " \u20BD";
        // status
        String status = info.getStatus();
        int status_id = info.getStatusId();
        // info
        holder.ts_source.setText(source_title);
        holder.ts_amount.setText(amount);
        holder.ts_date.setText(date);
        holder.ts_status.setText(status);
        // color
        if (status_id == 1 || status_id == 2) holder.ts_status.setTextColor(Color.parseColor("#00a651"));
        else if (status_id == -1) holder.ts_status.setTextColor(Color.parseColor("#cf2832"));
        else holder.ts_status.setTextColor(Color.parseColor("#0f2f5d"));
    }

    @Override
    public int getItemCount() {
        return ts_requests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView ts_source, ts_amount, ts_date, ts_status;
        ViewHolder(View view){
            super(view);
            ts_source = view.findViewById(R.id.ts_source);
            ts_amount = view.findViewById(R.id.ts_amount);
            ts_date = view.findViewById(R.id.ts_date);
            ts_status = view.findViewById(R.id.ts_status);
        }
    }
}
