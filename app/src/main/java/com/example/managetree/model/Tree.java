package com.example.managetree.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Tree {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "name")
    public String name = "";
    @ColumnInfo(name = "height")
    public String height = "0";
    @ColumnInfo(name = "quantity_fruit")
    public String quantityFruit = "0";
    @ColumnInfo(name = "leaf_color")
    public String leafColor = "";
    @ColumnInfo(name = "diameter") // duong kinh
    public String diameter = "0";
    @ColumnInfo(name = "designation")
    public String designation = "";
    @ColumnInfo(name = "path")
    public String path = "";
}
