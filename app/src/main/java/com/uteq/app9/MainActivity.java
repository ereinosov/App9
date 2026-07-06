package com.uteq.app9;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference HumedadRef, presionRef, VelocidadRef, TemperauraRef;
    EditText txt_temperatura_edit, txt_humedad_edit, txt_velocidad_edit, txt_presion_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        database = FirebaseDatabase.getInstance();
        HumedadRef = database.getReference("sensores/humedad");
        presionRef = database.getReference("sensores/presion");
        VelocidadRef = database.getReference("sensores/velocidad");
        TemperauraRef = database.getReference("sensores/temperatura");
        TextView txtTemp = findViewById(R.id.valor_Temperatura);
        TextView txtHumedad = findViewById(R.id.valor_Humedad);
        TextView txtVelocidad = findViewById(R.id.valor_Velocidad);
        TextView txtPresion = findViewById(R.id.valor_Presion);
        txt_temperatura_edit = findViewById(R.id.setvalor_Temperatura);
        txt_humedad_edit = findViewById(R.id.setvalor_Humedad);
        txt_velocidad_edit = findViewById(R.id.setvalor_Velocidad);
        txt_presion_edit = findViewById(R.id.setvalor_Presion);

        TemperauraRef.addValueEventListener(setListener(txtTemp, "°C"));
        HumedadRef.addValueEventListener(setListener(txtHumedad, "%"));
        VelocidadRef.addValueEventListener(setListener(txtVelocidad, "km/h"));
        presionRef.addValueEventListener(setListener(txtPresion, "hPa"));
    }

    public void clickBotonTemperatura(View v) {
        TemperauraRef.setValue(Float.parseFloat(txt_temperatura_edit.getText().toString()));
    }

    public void clickBotonHumedad(View v) {
        HumedadRef.setValue(Float.parseFloat(txt_humedad_edit.getText().toString()));
    }

    public void clickBotonVelocidad(View v) {
        VelocidadRef.setValue(Float.parseFloat(txt_velocidad_edit.getText().toString()));
    }

    public void clickBotonPresion(View v) {
        presionRef.setValue(Float.parseFloat(txt_presion_edit.getText().toString()));
    }


    public ValueEventListener setListener(TextView txt, String UnidadMedida) {

        return (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txt.setText(snapshot.getValue().toString() + " " + UnidadMedida);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                txt.setText("");
            }
        });

    }
}