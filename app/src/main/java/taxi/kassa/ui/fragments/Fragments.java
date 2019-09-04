package taxi.kassa.ui.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import taxi.kassa.R;
import taxi.kassa.ui.AccountCreateActivity;
import taxi.kassa.ui.AccountsActivity;
import taxi.kassa.ui.AuthCodeActivity;
import taxi.kassa.ui.AuthPhoneActivity;
import taxi.kassa.ui.AuthSignupActivity;
import taxi.kassa.ui.IntroActivity;
import taxi.kassa.ui.RequestCreateSuccessActivity;
import taxi.kassa.ui.WithdrawCreateActivity;
import taxi.kassa.ui.WithdrawCreateEmptyActivity;
import taxi.kassa.ui.BalancesActivity;
import taxi.kassa.ui.WithdrawsActivity;

public class Fragments extends Fragment {

    public static void fragmentIntro(Context context) {
        FragmentTransaction ft = ((FragmentActivity) context).getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left);
        ft.replace(R.id.fragment_container, new IntroActivity());
        ft.commit();
    }

    public static void fragmentAuthPhone(Context context) {
        FragmentTransaction ft = ((FragmentActivity) context).getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left);
        ft.replace(R.id.fragment_container, new AuthPhoneActivity());
        ft.addToBackStack(null);
        ft.commit();
    }

    public static void fragmentAuthCode(Context context) {
        FragmentTransaction ft = ((FragmentActivity) context).getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left);
        ft.replace(R.id.fragment_container, new AuthCodeActivity());
        ft.commit();
    }

    public static void fragmentAuthSignup(Context context) {
        FragmentTransaction ft = ((FragmentActivity) context).getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left);
        ft.replace(R.id.fragment_container, new AuthSignupActivity());
        ft.addToBackStack(null);
        ft.commit();
    }
    public static void fragmentCreateRequestComplite(Context context) {
        FragmentTransaction ft = ((FragmentActivity) context).getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left);
        ft.replace(R.id.fragment_container, new RequestCreateSuccessActivity());
        ft.commit();
    }
    public static void fragmentCreateTransactionRequest(Context context, int source_id) {
        // params
        Bundle b = new Bundle();
        b.putInt("source_id", source_id);
        // fragment
        FragmentTransaction ft = ((FragmentActivity) context).getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left);
        Fragment frag = new WithdrawCreateActivity();
        frag.setArguments(b);
        ft.replace(R.id.fragment_container, frag);
        ft.addToBackStack(null);
        ft.commit();
    }

    public static void fragmentWithdraws(Context context) {
        FragmentTransaction ft = ((FragmentActivity) context).getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new WithdrawsActivity(), null);
        ft.commit();
    }

    public static void fragmentCreateTransactionRequestEmpty(Context context) {
        FragmentTransaction ft = ((FragmentActivity) context).getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new WithdrawCreateEmptyActivity(), null);
        ft.commit();
    }

    public static void fragmentBalance(Context context) {
        FragmentTransaction ft = ((FragmentActivity) context).getFragmentManager().beginTransaction();
        //ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_left, R.animator.slide_out_right);
        ft.replace(R.id.fragment_container, new BalancesActivity());
        //ft.addToBackStack(null)
        ft.commit();
        //TextView toolbar_title = ((AppCompatActivity) context).findViewById(R.id.toolbar_title);
        //toolbar_title.setText("Балансы");
        //((AppCompatActivity) context).getSupportActionBar().setTitle("Балансы");
    }

    public static void fragmentAccounts(Context context) {
        Fragment f = ((FragmentActivity) context).getFragmentManager().findFragmentByTag("ACCOUNTS_LIST");
        if (f != null && f.isVisible()) return;
        FragmentTransaction ft = ((FragmentActivity) context).getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new AccountsActivity(), "ACCOUNTS_LIST");
        ft.addToBackStack(null);
        ft.commit();
        //TextView toolbar_title = ((AppCompatActivity) context).findViewById(R.id.toolbar_title);
        //toolbar_title.setText("Мои счета");
        //((AppCompatActivity) context).getSupportActionBar().setTitle("Мои счета");
    }

    public static void fragmentCreateAccount(Context context) {
        FragmentTransaction ft = ((FragmentActivity) context).getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left);
        ft.replace(R.id.fragment_container, new AccountCreateActivity());
        ft.addToBackStack(null);
        ft.commit();
        //TextView toolbar_title = ((AppCompatActivity) context).findViewById(R.id.toolbar_title);
        //toolbar_title.setText("Мои счета");
        //((AppCompatActivity) context).getSupportActionBar().setTitle("Мои счета");
    }
}
