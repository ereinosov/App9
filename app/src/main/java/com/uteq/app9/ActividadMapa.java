package com.uteq.app9;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActividadMapa extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker markerActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actividad_mapa);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://software2026-2b742-default-rtdb.firebaseio.com");
        DatabaseReference refUbicacion = database.getReference("vehiculos/GPR250/ubicacion_actual");

        refUbicacion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Double lat = snapshot.child("latitud").getValue(Double.class);
                    Double lon = snapshot.child("longitud").getValue(Double.class);
                    if (lat != null && lon != null) {
                        actualizarMarcadorMapa(lat, lon);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void actualizarMarcadorMapa(double lat, double lon) {
        LatLng pos = new LatLng(lat, lon);
        if (markerActual != null) {
            markerActual.remove();
        }
        markerActual = mMap.addMarker(new MarkerOptions().position(pos).title("GPR250"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
    }

    public void grabarNuevaPosicionGPS(double lat, double lon) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://software2026-2b742-default-rtdb.firebaseio.com");
        DatabaseReference refUbicacion = database.getReference("vehiculos/GPR250/ubicacion_actual");
        refUbicacion.child("latitud").setValue(lat);
        refUbicacion.child("longitud").setValue(lon);
    }
}
