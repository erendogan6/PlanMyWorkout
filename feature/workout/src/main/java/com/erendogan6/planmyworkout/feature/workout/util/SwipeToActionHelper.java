package com.erendogan6.planmyworkout.feature.workout.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.erendogan6.planmyworkout.feature.workout.adapter.ExerciseLogAdapter;

public class SwipeToActionHelper extends ItemTouchHelper.SimpleCallback {

    private ExerciseLogAdapter adapter;
    private Paint clearPaint;
    private ColorDrawable deleteBackground;
    private ColorDrawable duplicateBackground;
    private Drawable deleteIcon;
    private Drawable duplicateIcon;
    private int intrinsicWidth;
    private int intrinsicHeight;

    public SwipeToActionHelper(Context context, ExerciseLogAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;

        // Initialize backgrounds
        deleteBackground = new ColorDrawable(Color.parseColor("#FF4444"));
        duplicateBackground = new ColorDrawable(Color.parseColor("#44AA44"));

        // Initialize icons
        deleteIcon = ContextCompat.getDrawable(context, com.erendogan6.planmyworkout.coreui.R.drawable.ic_delete);
        duplicateIcon = ContextCompat.getDrawable(context, com.erendogan6.planmyworkout.coreui.R.drawable.ic_content_copy);

        if (deleteIcon != null) {
            intrinsicWidth = deleteIcon.getIntrinsicWidth();
            intrinsicHeight = deleteIcon.getIntrinsicHeight();
        }

        clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        adapter.onItemSwiped(position, direction);
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.65f;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return defaultValue * 1.2f;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(c, itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        if (dX > 0) {
            // Swiping to the right - Duplicate action
            duplicateBackground.setBounds(itemView.getLeft(), itemView.getTop(), (int) (itemView.getLeft() + dX), itemView.getBottom());
            duplicateBackground.draw(c);

            if (duplicateIcon != null) {
                int iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int iconMargin = (itemHeight - intrinsicHeight) / 2;
                int iconLeft = itemView.getLeft() + iconMargin;
                int iconRight = iconLeft + intrinsicWidth;
                int iconBottom = iconTop + intrinsicHeight;

                duplicateIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                duplicateIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                duplicateIcon.draw(c);
            }
        } else {
            // Swiping to the left - Delete action
            deleteBackground.setBounds((int) (itemView.getRight() + dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
            deleteBackground.draw(c);

            if (deleteIcon != null) {
                int iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int iconMargin = (itemHeight - intrinsicHeight) / 2;
                int iconLeft = itemView.getRight() - iconMargin - intrinsicWidth;
                int iconRight = itemView.getRight() - iconMargin;
                int iconBottom = iconTop + intrinsicHeight;

                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                deleteIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                deleteIcon.draw(c);
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void clearCanvas(Canvas c, float left, float top, float right, float bottom) {
        c.drawRect(left, top, right, bottom, clearPaint);
    }
}