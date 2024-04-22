package com.example.managetree;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.managetree.database.AppDatabase;
import com.example.managetree.model.Tree;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends AppCompatActivity {
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        PieChart pieChart = findViewById(R.id.pieChart);

        database = AppDatabase.getInMemoryDatabase(this);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        List<Tree> list = database.treeDao().findAllTree();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                pieEntries.add(new PieEntry(Float.parseFloat(list.get(i).diameter), "Category = " + i));
            }
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Biểu đồ");
        dataSet.setColors(Color.rgb(255, 0, 0), Color.rgb(0, 255, 0), Color.rgb(0, 0, 255));
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();
    }
}