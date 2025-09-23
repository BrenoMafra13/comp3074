package ca.gbc.comp3074.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText hoursWorked;
    private EditText hourlyRate;
    private EditText taxRate;
    private TextView pay;
    private TextView overtimePay;
    private TextView tax;
    private TextView totalPay;

    void update(double paid, double overtime, double total, double taxed) {
        pay.setText(String.format("Pay: %.2f", paid));
        overtimePay.setText(String.format("Overtime Pay: %.2f", overtime));
        totalPay.setText(String.format("Total Pay: %.2f", total));
        tax.setText(String.format("Tax: %.2f", taxed));
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

        hoursWorked = findViewById(R.id.hoursWorked);
        hourlyRate = findViewById(R.id.hourlyRate);
        taxRate = findViewById(R.id.taxRate);
        pay = findViewById(R.id.pay);
        overtimePay = findViewById(R.id.overtimePay);
        tax = findViewById(R.id.tax);
        totalPay = findViewById(R.id.totalPay);

        ((Button)findViewById(R.id.calculate)).setOnClickListener(v -> {
            double hours = Double.parseDouble(hoursWorked.getText().toString());
            double hourly = Double.parseDouble(hourlyRate.getText().toString());
            double taxR = Double.parseDouble(taxRate.getText().toString());

            double paid;
            double overtime;
            double total;
            double taxed;

            if (hours <= 40) {
                paid = hours * hourly;
                overtime = 0;
            } else {
                paid = 40 * hourly;
                overtime = (hours - 40) * hourly * 1.5;
            }

            total = paid + overtime;
            taxed = total * (taxR / 100.0);

            update(paid, overtime, total, taxed);

        });

        ((Button)findViewById(R.id.aboutButton)).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

    }
}
