package ca.gbc.comp3074.lab1week2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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
        Button btnNext = findViewById(R.id.button);
        btnNext.setOnClickListener(v -> {
            Toast.makeText(this, R.string.toast, Toast.LENGTH_LONG).show();
            Intent i = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(i);
        });

//        TextView studentId = findViewById(R.id.studentid);
//        studentId.setText("This is modified text");
    }
}
