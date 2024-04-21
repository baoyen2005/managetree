package com.example.managetree.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Tree {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "height")
    public String height;
    @ColumnInfo(name = "quantity_fruit")
    public String quantityFruit;
    @ColumnInfo(name = "leaf_color")
    public String leafColor;
    @ColumnInfo(name = "diameter") // duong kinh
    public String diameter;
    @ColumnInfo(name = "designation")
    public String designation;
    @ColumnInfo(name = "path")
    public String path;
}
