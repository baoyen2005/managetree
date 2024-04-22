package com.example.managetree;

import static android.Manifest.permission.CAMERA;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.managetree.database.AppDatabase;
import com.example.managetree.model.Tree;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

public class NewTreeActivity extends AppCompatActivity {
    private Button btnSave, btnTakePhoto;
    private TextView tvTitle;
    private EditText edtName, edtHeight, edtQuantityFruit, edtDiameter, edtLeafColor, edtDes;
    private ImageView ivTree;
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
        ivTree = findViewById(R.id.ivTree);

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
            ivTree.setImageURI(Uri.parse(tree.path));
        }

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(NewTreeActivity.this, CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewTreeActivity.this, new String[]{CAMERA},
                            CAMERA_REQUEST);
                } else {
                    takePhoto();
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tree == null) {
                    Tree newTree = new Tree();
                    newTree.name = edtName.getText().toString();
                    newTree.height = edtHeight.getText().toString();
                    newTree.quantityFruit = edtQuantityFruit.getText().toString();
                    newTree.diameter = edtDiameter.getText().toString();
                    newTree.leafColor = edtLeafColor.getText().toString();
                    newTree.designation = edtDes.getText().toString();
                    newTree.path = path;
                    database.treeDao().insertTree(newTree);
                    Toast.makeText(NewTreeActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    tree.name = edtName.getText().toString();
                    tree.height = edtHeight.getText().toString();
                    tree.quantityFruit = edtQuantityFruit.getText().toString();
                    tree.diameter = edtDiameter.getText().toString();
                    tree.leafColor = edtLeafColor.getText().toString();
                    tree.designation = edtDes.getText().toString();
                    tree.path = path;
                    database.treeDao().updateTree(tree);
                    Toast.makeText(NewTreeActivity.this, "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
    private File takePhotoFile = null;

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                takePhotoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (takePhotoFile != null) {
                Uri uri = FileProvider.getUriForFile(
                        this,
                        "com.example.managetree.fileprovider",
                        takePhotoFile
                );
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                takePhotoStartForResult.launch(takePictureIntent);
            }
        }
    }
    private ActivityResultLauncher<Intent> takePhotoStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                    Glide.with(NewTreeActivity.this)
                            .load(takePhotoFile.getPath())
                            .into(ivTree);

                    path = takePhotoFile.getPath();

                } else {
                    finish();
                }
            }
    );
    private File createImageFile() throws IOException {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null) {
            return File.createTempFile(
                    "JPEG_" + timeStamp + "_",
                    ".jpg",
                    storageDir
            );
        } else {
            throw new IOException("Failed to create image file. External storage directory is null.");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                Toast.makeText(NewTreeActivity.this, "Chưa cấp quyền", Toast.LENGTH_SHORT).show();
            }
        }
    }
}