package com.erendogan6.planmyworkout;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.erendogan6.planmyworkout.coreui.base.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Main activity for the PlanMyWorkout app.
 * This activity hosts the navigation components and bottom navigation.
 */
@AndroidEntryPoint
public class MainActivity extends BaseActivity {
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private int currentTabId = R.id.home_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, com.erendogan6.planmyworkout.coreui.R.color.background_dark));

            // Dark content for status bar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(0);
            }
        }

        // Set up the NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        // Set up bottom navigation
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set up the bottom navigation with the NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Handle bottom navigation item clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Get current destination
            NavDestination currentDestination = navController.getCurrentDestination();
            if (currentDestination == null) {
                return false;
            }

            // If the same tab is selected, do nothing
            if (itemId == currentDestination.getId()) {
                return true; // Consume the event but don't navigate
            }

            // Check if we're in a child of the selected graph
            NavGraph parentGraph = currentDestination.getParent();
            while (parentGraph != null) {
                if (itemId == parentGraph.getId()) {
                    return true; // Already in this tab, do nothing
                }
                parentGraph = parentGraph.getParent();
            }

            // Switch to the selected tab
            return NavigationUI.onNavDestinationSelected(item, navController);
        });

        // Handle destination changes to update the bottom navigation
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Show/hide bottom navigation based on destination
            updateBottomNavigationVisibility(destination);
        });
    }

    private void updateBottomNavigationVisibility(NavDestination destination) {
        // By default, don't show the bottom navigation
        boolean showBottomNav = false;

        // Check if the destination is within the bottom_nav_graph
        NavDestination currentDest = destination;
        while (currentDest != null) {
            // If we're in the bottom_nav_graph, we might show the bottom nav
            if (currentDest.getId() == R.id.bottom_nav_graph) {
                showBottomNav = true;
                break;
            }

            // Move up to the parent
            NavGraph parent = currentDest.getParent();
            if (parent == currentDest) {
                break; // Avoid infinite loop
            }
            currentDest = parent;
        }

        // Even if we're in the bottom_nav_graph, we need to check specific fragments
        // where we want to hide the bottom navigation
        if (showBottomNav) {
            int destinationId = destination.getId();

            // Hide bottom navigation in the ExerciseListFragment flow
            // Check the label of the destination to identify the fragments
            String label = destination.getLabel() != null ? destination.getLabel().toString() : "";
            if (label.equals("Exercise List") ||
                label.equals("Exercise History") ||
                label.equals("Exercise Detail")) {
                showBottomNav = false;
            }
        }

        bottomNavigationView.setVisibility(showBottomNav ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        // If we're on a main tab and can't go back further in that tab's stack
        if (!navController.popBackStack()) {
            // If we're not on the home tab, switch to home
            if (currentTabId != R.id.home_navigation) {
                bottomNavigationView.setSelectedItemId(R.id.home_navigation);
            } else {
                // Otherwise, let the system handle the back press (exit app)
                super.onBackPressed();
            }
        }
    }
}
