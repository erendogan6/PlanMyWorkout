package com.erendogan6.planmyworkout.coreui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.erendogan6.planmyworkout.coreui.R;

/**
 * A custom button that can show a loading state.
 */
public class LoadingButton extends FrameLayout {
    
    private TextView textView;
    private ProgressBar progressBar;
    private String buttonText;
    private boolean isLoading = false;
    
    public LoadingButton(@NonNull Context context) {
        super(context);
        init(context, null);
    }
    
    public LoadingButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    
    public LoadingButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_loading_button, this, true);
        
        textView = findViewById(R.id.button_text);
        progressBar = findViewById(R.id.button_progress);
        
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton);
            buttonText = a.getString(R.styleable.LoadingButton_buttonText);
            a.recycle();
        }
        
        if (buttonText != null) {
            textView.setText(buttonText);
        }
        
        updateLoadingState();
    }
    
    /**
     * Set the button text.
     *
     * @param text The text to display on the button
     */
    public void setText(String text) {
        buttonText = text;
        textView.setText(text);
    }
    
    /**
     * Set the loading state of the button.
     *
     * @param loading True to show loading state, false otherwise
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        updateLoadingState();
    }
    
    /**
     * Check if the button is in loading state.
     *
     * @return True if the button is in loading state, false otherwise
     */
    public boolean isLoading() {
        return isLoading;
    }
    
    private void updateLoadingState() {
        if (isLoading) {
            textView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            setEnabled(false);
        } else {
            textView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            setEnabled(true);
        }
    }
}
