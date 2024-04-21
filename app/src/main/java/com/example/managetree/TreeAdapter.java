package com.example.managetree;


import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managetree.model.Tree;

import java.util.ArrayList;
import java.util.List;

public class TreeAdapter extends RecyclerView.Adapter<TreeAdapter.ViewHolder> {
    List<Tree> listTree = new ArrayList<>();
    private final OnItemClickListener listener;

    public TreeAdapter(OnItemClickListener listener, List<Tree> trees) {
        this.listener = listener;
        this.listTree = trees;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.tree_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tree tree = listTree.get(position);

        holder.tvName.setText(tree.name);
        holder.tvHeight.setText(tree.height.toString());
        holder.tvQuantityFruit.setText(tree.quantityFruit.toString());
        holder.tvDiameter.setText(tree.diameter.toString());
        if (position == 0){
            holder.ivMoreVer.setVisibility(View.INVISIBLE);
        } else {
            holder.ivMoreVer.setVisibility(View.VISIBLE);
        }
        holder.ivMoreVer.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(tree);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listTree.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvHeight, tvQuantityFruit, tvDiameter;
        public ImageView ivMoreVer;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvHeight = itemView.findViewById(R.id.tvHeight);
            tvQuantityFruit = itemView.findViewById(R.id.tvQuantityFruit);
            tvDiameter = itemView.findViewById(R.id.tvDiameter);
            ivMoreVer = itemView.findViewById(R.id.ivMoreVer);

            ivMoreVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}

