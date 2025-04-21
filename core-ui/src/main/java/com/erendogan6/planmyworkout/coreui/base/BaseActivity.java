package com.erendogan6.planmyworkout.coreui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.erendogan6.planmyworkout.coreui.loading.LoadingManager;

/**
 * Base Activity class that provides common functionality for all activities.
 * This includes loading state management using the LoadingManager.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private LoadingManager loadingManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingManager = LoadingManager.getInstance();
    }

    /**
     * Shows the loading overlay.
     */
    protected void showLoading() {
        if (loadingManager != null) {
            loadingManager.showLoading(this);
        }
    }

    /**
     * Hides the loading overlay.
     */
    protected void hideLoading() {
        if (loadingManager != null) {
            loadingManager.hideLoading(this);
        }
    }

    /**
     * Checks if the loading overlay is currently showing.
     *
     * @return True if the loading overlay is showing, false otherwise
     */
    protected boolean isLoadingShowing() {
        return loadingManager != null && loadingManager.isLoadingShowing(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure loading is hidden when the activity is destroyed
        hideLoading();
    }
}
