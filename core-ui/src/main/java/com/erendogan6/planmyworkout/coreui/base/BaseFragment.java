package com.erendogan6.planmyworkout.coreui.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.erendogan6.planmyworkout.coreui.loading.LoadingManager;

/**
 * Base Fragment class that provides common functionality for all fragments.
 * This includes loading state management using the LoadingManager.
 */
public abstract class BaseFragment extends Fragment {

    private LoadingManager loadingManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingManager = LoadingManager.getInstance();
    }

    /**
     * Shows the loading overlay.
     */
    protected void showLoading() {
        if (getActivity() != null && loadingManager != null) {
            loadingManager.showLoading(getActivity());
        }
    }

    /**
     * Hides the loading overlay.
     */
    protected void hideLoading() {
        if (getActivity() != null && loadingManager != null) {
            loadingManager.hideLoading(getActivity());
        }
    }

    /**
     * Checks if the loading overlay is currently showing.
     *
     * @return True if the loading overlay is showing, false otherwise
     */
    protected boolean isLoadingShowing() {
        return loadingManager != null && getActivity() != null && 
               loadingManager.isLoadingShowing(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Ensure loading is hidden when the fragment is destroyed
        hideLoading();
    }
}
