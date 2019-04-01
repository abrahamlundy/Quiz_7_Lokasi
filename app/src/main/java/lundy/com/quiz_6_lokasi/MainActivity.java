package lundy.com.quiz_6_lokasi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    //Variabel
    private static final int MY_PERMISSIONS_REQUEST = 99;//int bebas, maks 1 byte
    GoogleApiClient mGoogleApiClient ;
    Location mLastLocation;
    TextView txtLocation;
    LocationRequest mLocationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback mLocationCallback;
    ArrayList<Lokasi> daftarlokasi;
    RecyclerView rv;
    RecyclerView.LayoutManager lm;
    ListHandler adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Quiz 6");

        txtLocation = findViewById(R.id.tvLong);
        daftarlokasi= new ArrayList<>();
        rv= findViewById(R.id.rvLokasi);
        rv.setHasFixedSize(true);

        //menjalankan service lokasi
        buildGoogleApiClient();
        createLocationRequest();
        aturPermission();
        ambilLokasiSekali();
        ambilLokasi();

        //setting recyvleview
        adapter = new ListHandler(daftarlokasi);
        lm= new LinearLayoutManager(this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(lm);

    }


    //Untuk menjalan LocationRequestPeriodik
    @SuppressLint("RestrictedApi")
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        //10 detik sekali minta lokasi (10000ms = 10 detik)
        mLocationRequest.setInterval(10000);
        //tapi tidak boleh lebih cepat dari 5 detik
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    ambilLokasiSekali();
    ambilLokasi();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //Untuk meminta permission
    public void aturPermission(){
        if (ActivityCompat.checkSelfPermission (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission (this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            //belum ada ijin, tampilkan dialog
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST);
           // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST);

            return;
        }

    }

    public void ambilLokasiSekali() {
        //cek status izin
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            //ambil lokasi terakhir
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        mLastLocation = location;
                        //Toast.makeText(getApplicationContext(),String.valueOf(mLastLocation.getLatitude()),Toast.LENGTH_SHORT).show();
                        txtLocation.setText("Latitude : "+String.valueOf(mLastLocation.getLatitude()+"\n"+"Longitude : "+String.valueOf(mLastLocation.getLongitude())));

                    }else{
                        Toast.makeText(getApplicationContext(),"Kosong",Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }else{
            //bagian ini tidak berguna
            ambilLokasiSekali();
        }
    }

    private void ambilLokasi() {

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    //txtLatitude.setText(String.valueOf(location.getLatitude()));
                    //txtLongitude.setText(String.valueOf(location.getLongitude()));
                    Toast.makeText(getApplicationContext(),String.valueOf(location.getLatitude()),Toast.LENGTH_SHORT).show();

                    Lokasi lokasi =null ;
                    lokasi= new Lokasi();
                    lokasi.setLatitude(String.valueOf(location.getLatitude()));
                    lokasi.setLongitude(String.valueOf(location.getLongitude()));
                    daftarlokasi.add(lokasi);
                    adapter.notifyDataSetChanged();
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.requestLocationUpdates( mLocationRequest, mLocationCallback,null);
        }else{
        ambilLokasi();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        //data diupdate diluar interval periodik saat lokasi berubah
        ambilLokasi();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
