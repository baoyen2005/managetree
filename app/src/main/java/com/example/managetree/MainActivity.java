package com.example.managetree;

import static com.example.managetree.NewTreeActivity.TREE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.managetree.database.AppDatabase;
import com.example.managetree.model.Tree;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final DialogLoading loadingDialog = new DialogLoading(MainActivity.this);
    private AppDatabase database;
    private RecyclerView rvListTree;
    private Button btnAddNewTree, btnAddNewTreeNow, btnPieChart;
    private LinearLayout llEmpty;
    private List<Tree> treeList;
    private TreeAdapter adapter;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = AppDatabase.getInMemoryDatabase(this);

        rvListTree = findViewById(R.id.rvListTree);
        btnAddNewTree = findViewById(R.id.btnAddNewTree);
        btnAddNewTreeNow = findViewById(R.id.btnAddNewTreeNow);
        btnPieChart = findViewById(R.id.btnPieChart);
        llEmpty = findViewById(R.id.llEmpty);

        treeList = database.treeDao().findAllTree();
        showData();
        gson = new Gson();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rvListTree.setLayoutManager(linearLayoutManager);
        btnAddNewTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewTreeActivity.class);
                intent.putExtra(TREE, gson.toJson(null));
                startActivity(intent);
            }
        });

        btnAddNewTreeNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewTreeActivity.class);
                intent.putExtra(TREE, gson.toJson(null));
                startActivity(intent);
            }
        });
        btnPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PieChartActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        treeList = database.treeDao().findAllTree();
        showData();
    }

    private void showData(){
        if (treeList == null || treeList.isEmpty()){
            llEmpty.setVisibility(View.VISIBLE);
            rvListTree.setVisibility(View.GONE);
            btnAddNewTree.setVisibility(View.GONE);
            btnPieChart.setVisibility(View.GONE);
        } else{
            llEmpty.setVisibility(View.GONE);
            rvListTree.setVisibility(View.VISIBLE);
            btnAddNewTree.setVisibility(View.VISIBLE);
            btnPieChart.setVisibility(View.VISIBLE);
            ArrayList<Tree> trees = new ArrayList<>();
            Tree newTree = new Tree();
            newTree.name = "Tên";
            newTree.height = "Chiều cao";
            newTree.quantityFruit = "Số quả";
            newTree.diameter = "Đường kính";
            trees.add(newTree);
            if (treeList != null && !treeList.isEmpty()){
                trees.addAll(treeList);
                adapter = new TreeAdapter(new OnItemClickListener() {
                    @Override
                    public void onItemClick(Tree item) {
                        final String[] fonts = {
                                "Chỉnh sửa"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setItems(fonts, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if ("Chỉnh sửa".equals(fonts[which])) {
                                    Intent intent = new Intent(MainActivity.this, NewTreeActivity.class);
                                    intent.putExtra(TREE, gson.toJson(item));
                                    startActivity(intent);
                                }

                            }
                        });
                        builder.show();
                    }
                }, trees);
                rvListTree.setAdapter(adapter);
            }
        }
    }
}