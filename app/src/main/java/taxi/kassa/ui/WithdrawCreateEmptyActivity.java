package taxi.kassa.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import taxi.kassa.R;
import taxi.kassa.ui.fragments.Fragments;

public class WithdrawCreateEmptyActivity extends Fragment {
    View v;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // view
        final Context context = getActivity();
        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Новая заявка");
        v = inflater.inflate(R.layout.withdrawal_create_empty, null);

        v.findViewById(R.id.btn_create_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Fragments.fragmentCreateAccount(context);
            }
        });

        // output
        return v;
    }
}
