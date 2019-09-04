package taxi.kassa.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import taxi.kassa.R;
import taxi.kassa.responses.Account;

public class AccountsSpinnerAdapter extends ArrayAdapter<Account> {
    private Context context;
    private Account[] values;

    public AccountsSpinnerAdapter(Context context, int textViewResourceId, Account[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.length;
    }

    @Override
    public Account getItem(int position){
        return values[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater v = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = v.inflate(R.layout.spinner_item, null);
        }

        TextView tv_bank = convertView.findViewById(R.id.spinner_bank);
        tv_bank.setText(values[position].getBankName());
        TextView tv_number = convertView.findViewById(R.id.spinner_number);
        tv_number.setText(values[position].getAccountNumber());
        TextView tv_driver = convertView.findViewById(R.id.spinner_driver);
        tv_driver.setText(values[position].getDriverName());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater v = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = v.inflate(R.layout.spinner_item_dropdown, null);
        }
        parent.setPadding(0, 0, 0, 0);
        LinearLayout l = convertView.findViewById(R.id.spinner_group);
        if (position != values.length - 1) l.setBackgroundResource(R.drawable.border_bottom_2);
        TextView tv_bank = convertView.findViewById(R.id.spinner_bank);
        tv_bank.setText(values[position].getBankName());
        TextView tv_number = convertView.findViewById(R.id.spinner_number);
        tv_number.setText(values[position].getAccountNumber());
        TextView tv_driver = convertView.findViewById(R.id.spinner_driver);
        tv_driver.setText(values[position].getDriverName());
        return convertView;
    }
}
