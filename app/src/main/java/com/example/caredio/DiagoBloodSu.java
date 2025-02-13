package com.example.caredio;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class DiagoBloodSu extends AppCompatActivity {

    ArrayList<BarEntry> barArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diago_blood_su);
        Log.d("BarChart", "onCreate() called");

        getData();  // Calling the method to get data

        // Log to confirm that getData() has been called
        Log.d("BarChart", "Data populated: " + barArrayList.toString());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BarChart barChart = findViewById(R.id.barChart);
        BarDataSet barDataSet = new BarDataSet(barArrayList, "Tuto");
        BarData barData = new BarData(barDataSet);

        // Set data to the chart
        barChart.setData(barData);
        barDataSet.setColor(ColorTemplate.COLOR_NONE);
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(true);

        // Ensure the chart is refreshed
        barChart.invalidate();
    }

    private void getData() {
        barArrayList = new ArrayList<>();
        barArrayList.add(new BarEntry(1f, 10));
        barArrayList.add(new BarEntry(2f, 15));
        barArrayList.add(new BarEntry(3f, 20));

        Log.d("BarChart", "Data: " + barArrayList.toString());

    }
}
