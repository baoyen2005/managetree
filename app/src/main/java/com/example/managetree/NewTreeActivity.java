package com.example.managetree;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managetree.database.AppDatabase;
import com.example.managetree.model.Tree;
import com.google.gson.Gson;

public class NewTreeActivity extends AppCompatActivity {
    private Button btnSave, btnTakePhoto;
    private TextView tvTitle;
    private EditText edtName, edtHeight, edtQuantityFruit, edtDiameter, edtLeafColor, edtDes;

    static final String TREE = "TREE";
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    private AppDatabase database;

    private String path;
    private String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.CAMERA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tree);

        tvTitle = findViewById(R.id.tvTitle);
        btnSave = findViewById(R.id.btnSave);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        edtName = findViewById(R.id.edtName);
        edtHeight = findViewById(R.id.edtHeight);
        edtQuantityFruit = findViewById(R.id.edtQuantityFruit);
        edtDiameter = findViewById(R.id.edtDiameter);
        edtLeafColor = findViewById(R.id.edtLeafColor);
        edtDes = findViewById(R.id.edtDes);
        database = AppDatabase.getInMemoryDatabase(this);

        Gson gson = new Gson();
        Tree tree = gson.fromJson(getIntent().getStringExtra(TREE), Tree.class);
        if (tree == null) {
            tvTitle.setText("Thêm cây cảnh");
            edtName.setText("");
            edtHeight.setText("");
            edtQuantityFruit.setText("");
            edtDiameter.setText("");
            edtLeafColor.setText("");
            edtDes.setText("");
        } else {
            tvTitle.setText("Chỉnh sửa cây cảnh");
            edtName.setText(tree.name);
            edtHeight.setText(tree.height);
            edtQuantityFruit.setText(tree.quantityFruit);
            edtDiameter.setText(tree.diameter);
            edtLeafColor.setText(tree.leafColor);
            edtDes.setText(tree.designation);
        }

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(NewTreeActivity.this, CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewTreeActivity.this, new String[]{CAMERA},
                            CAMERA_REQUEST);
                } else {
                    openCamera();
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tree newTree = new Tree();
                newTree.name = edtName.getText().toString();
                newTree.height = edtHeight.getText().toString();
                newTree.quantityFruit = edtQuantityFruit.getText().toString();
                newTree.diameter = edtDiameter.getText().toString();
                newTree.leafColor = edtLeafColor.getText().toString();
                newTree.designation = edtDes.getText().toString();
                newTree.path = path;
                if (tree == null) {
                    database.treeDao().insertTree(newTree);
                    Toast.makeText(NewTreeActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    database.treeDao().updateTree(newTree);
                    Toast.makeText(NewTreeActivity.this, "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, MY_CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(NewTreeActivity.this, "Chưa cấp quyền", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == MY_CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
//                Uri selectedImage = data.getData();
//                path = selectedImage.getPath();
            }
        }
    }
}