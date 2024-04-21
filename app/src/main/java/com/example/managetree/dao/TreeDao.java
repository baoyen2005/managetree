package com.example.managetree.dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.managetree.model.Tree;

import java.util.List;

@Dao
public interface TreeDao {
    @Insert
    void insertTree(Tree tree);
    @Update
    void updateTree(Tree tree);
    @Query("DELETE FROM Tree")
    void deleteAll();
    @Query("SELECT * FROM Tree")
    List<Tree> findAllTree();
}
