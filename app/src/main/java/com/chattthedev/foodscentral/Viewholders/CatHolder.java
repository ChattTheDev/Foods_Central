package com.chattthedev.foodscentral.Viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chattthedev.foodscentral.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class CatHolder extends RecyclerView.ViewHolder {
    public Chip chip;
    public ChipGroup chipGroup;
    public CatHolder(@NonNull View itemView) {
        super(itemView);

        chip = itemView.findViewById(R.id.chipfinal);
        chipGroup = itemView.findViewById(R.id.chiplayout);
    }
}
