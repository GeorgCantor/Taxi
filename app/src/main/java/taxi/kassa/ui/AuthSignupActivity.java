package taxi.kassa.ui;

//public class AuthSignupActivity extends Fragment {
//    // vars
//    static Boolean login_ready;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        // vars
//        login_ready = true;
//        // view
//        View v = inflater.inflate(R.layout.auth_signup, null);
//        final Context context = getActivity();
//        final EditText input_login = v.findViewById(R.id.input_login);
//        final TextView tv_error = v.findViewById(R.id.tv_error);
//        TextView tv_agreement = v.findViewById(R.id.tv_agreement);
//        CheckBox login_checkbox = v.findViewById(R.id.login_checkbox);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
//        tv_agreement.setMovementMethod(LinkMovementMethod.getInstance());
//        //input_login.setSelection(3);
//        //input_login.clearFocus();
//        // btn (checkbox)
//        login_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                login_ready = isChecked;
//            }
//        });
//        // btn (signup)
//        v.findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//
//                if (!login_ready) {
//                    Tools.showError(context, tv_error, "необходимо принять условия работы", 5000, 0);
//                    return;
//                }
//
//                String phone = input_login.getText().toString().replaceAll("[^\\d]", "");
//
//                Log.i("My Logs", "phone:_" + phone + "_");
//                if (phone.equals("7")) {
//                    Tools.showError(context, tv_error, "заполните поле", 5000, 0);
//                    return;
//                }
//
//                if (phone.length() != 11) {
//                    Tools.showError(context, tv_error, "неправильный формат", 5000, 0);
//                    return;
//                }
//
//                //Log.i("My Log", phone);
//
//                SharedPreferences sp = context.getSharedPreferences("MainStorage", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putString("phone", phone);
//                editor.apply();
//
//                Call<ResponseAPI<ResponseCreateRequest>> call = api.createRequest("", phone, 11, "ZVc3THdVdzM5dDhXQVBoR1ZJNk80SmNxdjBGcEhGUzY4YkJIcmV");
//                call.enqueue(new Callback<ResponseAPI<ResponseCreateRequest>>() {
//                    @Override
//                    public void onResponse(@NonNull Call<ResponseAPI<ResponseCreateRequest>> call, @NonNull Response<ResponseAPI<ResponseCreateRequest>> response) {
//                        if (response.code() == 200 && response.body() != null) {
//                            if (response.body().getSuccess()) {
//                                Fragments.fragmentCreateRequestComplite(context);
//                                Tools.hideKeyboard(v);
//                            } else {
//                                Tools.showError(context, tv_error, response.body().getErrorMsg(), 5000, 0);
//                            }
//                        }
//                    }
//                    @Override
//                    public void onFailure(@NonNull Call<ResponseAPI<ResponseCreateRequest>> call, @NonNull Throwable t) {
//                    }
//                });
//            }
//        });
//        // input (login)
//        input_login.addTextChangedListener(new TextWatcher() {
//            int length_before = 0;
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                length_before = s.length();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (input_login.length() <= 2) {
//                    input_login.setText("+7 ");
//                    input_login.setSelection(3);
//                }
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (length_before < s.length()) {
//                    if (s.length() == 6)
//                        s.append(" ");
//                    if (s.length() == 10 || s.length() == 13)
//                        s.append("-");
//                    if (s.length() > 6) {
//                        if (Character.isDigit(s.charAt(6)))
//                            s.insert(6, "-");
//                    }
//                    if (s.length() > 10) {
//                        if (Character.isDigit(s.charAt(10)))
//                            s.insert(10, "-");
//                    }
//                }
//            }
//        });
//
//        // output
//        return v;
//    }
//
//}


