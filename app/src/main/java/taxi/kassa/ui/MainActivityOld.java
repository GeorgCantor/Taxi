package taxi.kassa.ui;

//public class MainActivityOld extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//
//    public static API api = ApiManager.getHttpClient();
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        // storage
//        final SharedPreferences sp = this.getSharedPreferences("MainStorage", Context.MODE_PRIVATE);
//        final String access_token = sp.getString("access_token", "");
//        ApiManager.access_token = access_token;
//        Log.i("My Logs", "access_token: " + access_token);
//
//        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
//        // auth
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        assert access_token != null;
//        if (!access_token.equals("")) ft.replace(R.id.fragment_container, new BalancesActivity());
//        else ft.replace(R.id.fragment_container, new IntroActivity());
//        ft.commit();
//    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
//        else super.onBackPressed();
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.menu_balance) {
//            Fragments.fragmentBalance(this);
//        } else if (id == R.id.menu_requests) {
//            Fragments.fragmentWithdraws(this);
//        } else if (id == R.id.menu_accounts) {
//            Fragments.fragmentAccounts(this);
//        } else if (id == R.id.menu_logout) {
//            // storage
//            SharedPreferences sp = this.getSharedPreferences("MainStorage", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString("access_token", "");
//            editor.apply();
//            ApiManager.access_token = "";
//            // activity
//            Fragments.fragmentIntro(this);
//        }
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//}
