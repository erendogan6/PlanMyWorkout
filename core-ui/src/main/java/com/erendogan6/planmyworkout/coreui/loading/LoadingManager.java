package com.erendogan6.planmyworkout.coreui.loading;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.erendogan6.planmyworkout.coreui.R;

import java.lang.ref.WeakReference;

/**
 * A utility class to manage loading overlay across the app.
 * This class provides methods to show and hide a loading animation overlay.
 */
public class LoadingManager implements LifecycleEventObserver {

    private static final String TAG_LOADING_OVERLAY = "loading_overlay";
    private static final long MINIMUM_LOADING_DURATION_MS = 500;
    private static final long LOADING_DELAY_MS = 100;
    private static final long LOADING_TIMEOUT_MS = 20000; // 20 seconds timeout

    private static volatile LoadingManager instance;
    private View loadingView;
    private long loadingStartTime;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable hideRunnable;
    private Runnable showRunnable;
    private Runnable timeoutRunnable;
    private WeakReference<Activity> activityRef;
    private boolean isLoadingRequested = false;

    private LoadingManager() {
        // Private constructor to enforce singleton pattern
    }

    /**
     * Gets the singleton instance of LoadingManager.
     *
     * @return The LoadingManager instance
     */
    public static LoadingManager getInstance() {
        if (instance == null) {
            synchronized (LoadingManager.class) {
                if (instance == null) {
                    instance = new LoadingManager();
                }
            }
        }
        return instance;
    }

    /**
     * Shows the loading overlay after a short delay.
     * If the loading is hidden before the delay passes, the loading will not be shown.
     *
     * @param activity The activity where the loading overlay will be shown
     */
    public void showLoading(final Activity activity) {
        if (activity == null) {
            return;
        }

        // Cancel any pending hide operations
        if (hideRunnable != null) {
            handler.removeCallbacks(hideRunnable);
            hideRunnable = null;
        }

        // Cancel any pending show operations
        if (showRunnable != null) {
            handler.removeCallbacks(showRunnable);
        }

        // Cancel any existing timeout
        if (timeoutRunnable != null) {
            handler.removeCallbacks(timeoutRunnable);
            timeoutRunnable = null;
        }

        // Mark that loading has been requested
        isLoadingRequested = true;

        // Create a new show runnable that will execute after the delay
        showRunnable = () -> {
            // Only show loading if it's still requested after the delay
            if (isLoadingRequested) {
                showLoadingImmediately(activity);

                // Set up timeout to automatically hide loading after 20 seconds
                setupTimeout(activity);
            }
            showRunnable = null;
        };

        // Schedule the show runnable after the delay
        handler.postDelayed(showRunnable, LOADING_DELAY_MS);
    }

    /**
     * Sets up a timeout to automatically hide the loading overlay after 20 seconds.
     *
     * @param activity The activity where the loading overlay is shown
     */
    private void setupTimeout(final Activity activity) {
        // Create a new timeout runnable
        timeoutRunnable = () -> {
            // Hide loading due to timeout
            hideLoading(activity);
            timeoutRunnable = null;
        };

        // Schedule the timeout runnable
        handler.postDelayed(timeoutRunnable, LOADING_TIMEOUT_MS);
    }

    /**
     * Shows the loading overlay immediately without any delay.
     *
     * @param activity The activity where the loading overlay will be shown
     */
    private void showLoadingImmediately(Activity activity) {
        // Check if loading overlay is already showing
        if (isLoadingShowing(activity)) {
            return;
        }

        // Clean up previous activity reference
        cleanupPreviousActivity();

        // Store weak reference to the activity
        activityRef = new WeakReference<>(activity);

        // Register as lifecycle observer if possible
        if (activity instanceof LifecycleOwner) {
            ((LifecycleOwner) activity).getLifecycle().addObserver(this);
        }

        // Get the root view of the activity
        ViewGroup rootView = activity.findViewById(android.R.id.content);
        if (rootView == null) {
            return;
        }

        // Inflate the loading overlay layout
        LayoutInflater inflater = LayoutInflater.from(activity);
        loadingView = inflater.inflate(R.layout.layout_loading_overlay, null);
        loadingView.setTag(TAG_LOADING_OVERLAY);

        // Add the loading overlay to the root view
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        rootView.addView(loadingView, params);

        // Record the start time
        loadingStartTime = System.currentTimeMillis();
    }

    /**
     * Hides the loading overlay.
     *
     * @param activity The activity where the loading overlay will be hidden
     */
    public void hideLoading(final Activity activity) {
        if (activity == null) {
            return;
        }

        // Set loading as not requested anymore
        isLoadingRequested = false;

        // Cancel any pending show operations
        if (showRunnable != null) {
            handler.removeCallbacks(showRunnable);
            showRunnable = null;

            // Loading wasn't shown yet, so we can return early
            return;
        }

        // Cancel any pending timeout
        if (timeoutRunnable != null) {
            handler.removeCallbacks(timeoutRunnable);
            timeoutRunnable = null;
        }

        // If loading is not showing, nothing more to do
        if (!isLoadingShowing(activity)) {
            return;
        }

        // Calculate how long the loading has been shown
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - loadingStartTime;
        long remainingTime = Math.max(0, MINIMUM_LOADING_DURATION_MS - elapsedTime);

        // Cancel any existing hide operations
        if (hideRunnable != null) {
            handler.removeCallbacks(hideRunnable);
            hideRunnable = null;
        }

        // If we've already shown the loading for the minimum duration, hide it immediately
        // Otherwise, delay hiding until the minimum duration has passed
        hideRunnable = () -> {
            hideLoadingImmediately(activity);
            hideRunnable = null;
        };

        if (remainingTime <= 0) {
            handler.post(hideRunnable);
        } else {
            handler.postDelayed(hideRunnable, remainingTime);
        }
    }

    /**
     * Immediately hides the loading overlay without enforcing the minimum duration.
     *
     * @param activity The activity where the loading overlay will be hidden
     */
    private void hideLoadingImmediately(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        try {
            // Get the root view of the activity
            ViewGroup rootView = activity.findViewById(android.R.id.content);
            if (rootView == null) {
                return;
            }

            // Find and remove the loading overlay
            View loadingOverlay = rootView.findViewWithTag(TAG_LOADING_OVERLAY);
            if (loadingOverlay != null) {
                rootView.removeView(loadingOverlay);
            }

            // Unregister lifecycle observer if needed
            if (activity instanceof LifecycleOwner &&
                    activityRef != null && activityRef.get() == activity) {
                ((LifecycleOwner) activity).getLifecycle().removeObserver(this);
            }

        } catch (Exception e) {
            // Log error or handle exception
        }
    }

    /**
     * Checks if the loading overlay is currently showing.
     *
     * @param context The context to check
     * @return True if the loading overlay is showing, false otherwise
     */
    public boolean isLoadingShowing(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing()) {
                return false;
            }

            ViewGroup rootView = activity.findViewById(android.R.id.content);
            if (rootView != null) {
                return rootView.findViewWithTag(TAG_LOADING_OVERLAY) != null;
            }
        }
        return false;
    }

    /**
     * Cleanup resources when a new activity is used
     */
    private void cleanupPreviousActivity() {
        if (activityRef != null) {
            Activity previousActivity = activityRef.get();
            if (previousActivity != null && !previousActivity.isFinishing()) {
                if (previousActivity instanceof LifecycleOwner) {
                    ((LifecycleOwner) previousActivity).getLifecycle().removeObserver(this);
                }
                hideLoadingImmediately(previousActivity);
            }
            activityRef = null;
        }
    }

    /**
     * Handle lifecycle events to prevent memory leaks
     */
    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.getLifecycle().removeObserver(this);

            if (activityRef != null && activityRef.get() instanceof LifecycleOwner) {
                LifecycleOwner activityOwner = (LifecycleOwner) activityRef.get();
                if (activityOwner == source) {
                    if (hideRunnable != null) {
                        handler.removeCallbacks(hideRunnable);
                        hideRunnable = null;
                    }
                    if (showRunnable != null) {
                        handler.removeCallbacks(showRunnable);
                        showRunnable = null;
                    }
                    if (timeoutRunnable != null) {
                        handler.removeCallbacks(timeoutRunnable);
                        timeoutRunnable = null;
                    }
                    isLoadingRequested = false;
                    activityRef = null;
                    loadingView = null;
                }
            }
        }
    }
}