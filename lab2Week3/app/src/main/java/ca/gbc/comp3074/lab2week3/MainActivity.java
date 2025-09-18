package ca.gbc.comp3074.lab2week3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    int cnt = 0;
    int step = 1;
    final int def_step = 1;

    TextView label = null;

    void update(int val){
        cnt = val;
        ((TextView)findViewById(R.id.label)).setText(getResources().getString(R.string.counter, cnt));
        }
    void updateStep(int val){
        step = val;
        ((Button)findViewById(R.id.btn_step)).setText(getResources().getString(R.string.step, step));
    }
    void reset(){
        update(0);
        updateStep(def_step);
    }

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

        reset();

        ((Button)findViewById(R.id.btn_plus)).setOnClickListener(v -> update(cnt + step));

        ((Button)findViewById(R.id.btn_minus)).setOnClickListener(v -> update(cnt - step));

        ((Button)findViewById(R.id.btn_step)).setOnClickListener(v -> updateStep(2));

        ((Button)findViewById(R.id.btn_reset)).setOnClickListener(v -> reset());

    }
}