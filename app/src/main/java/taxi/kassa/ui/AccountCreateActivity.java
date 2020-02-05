package taxi.kassa.ui;

//public class AccountCreateActivity extends Fragment {
//    View v;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        // view
//        v = inflater.inflate(R.layout.account_create, null);
//        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
//        toolbar_title.setText("Новый счет");
//        final Context context = v.getContext();
//        final TextView tv_error_bank_code = v.findViewById(R.id.tv_error_bank_code);
//        final TextView tv_error_account_number = v.findViewById(R.id.tv_error_account_number);
//        final TextView tv_error_last_name = v.findViewById(R.id.tv_error_last_name);
//        final TextView tv_error_first_name = v.findViewById(R.id.tv_error_first_name);
//
//        // complite
//        Button btn_complite = v.findViewById(R.id.btn_complite);
//        btn_complite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // vars (name)
//                EditText input_first_name = v.findViewById(R.id.first_name);
//                String first_name = input_first_name.getText().toString();
//                EditText input_last_name = v.findViewById(R.id.last_name);
//                String last_name = input_last_name.getText().toString();
//                EditText input_middle_name = v.findViewById(R.id.middle_name);
//                String middle_name = input_middle_name.getText().toString();
//                // vars (bank)
//                EditText input_account_number = v.findViewById(R.id.account_number);
//                String account_number = input_account_number.getText().toString();
//                EditText input_bank_code = v.findViewById(R.id.bank_code);
//                String bank_code = input_bank_code.getText().toString();
//                // api
//                Call<ResponseAPI<ResponseSimple>> call = api.createAccount(first_name, last_name, middle_name, account_number, bank_code);
//                call.enqueue(new Callback<ResponseAPI<ResponseSimple>>() {
//                    @Override
//                    public void onResponse(@NonNull Call<ResponseAPI<ResponseSimple>> call, @NonNull Response<ResponseAPI<ResponseSimple>> response) {
//                        if (response.code() == 200 && response.body() != null) {
//                            if (response.body().getSuccess()) {
//                                Fragments.fragmentAccounts(context);
//                                Tools.hideKeyboard(v);
//                            } else {
//                                HashMap<String, String> error_data = response.body().getErrorData();
//                                if (error_data != null) {
//                                    if (error_data.containsKey("first_name")) Tools.showError(context, tv_error_first_name, error_data.get("first_name"), 5000, 0);
//                                    if (error_data.containsKey("last_name")) Tools.showError(context, tv_error_last_name, error_data.get("last_name"), 5000, 0);
//                                    if (error_data.containsKey("account_number")) Tools.showError(context, tv_error_account_number, error_data.get("account_number"), 5000, 0);
//                                    if (error_data.containsKey("bank_code")) Tools.showError(context, tv_error_bank_code, error_data.get("bank_code"), 5000, 0);
//                                }
//                            }
//                        }
//                    }
//                    @Override
//                    public void onFailure(@NonNull Call<ResponseAPI<ResponseSimple>> call, @NonNull Throwable t) {
//                    }
//                });
//            }
//        });
//
//        return v;
//    }
//}
