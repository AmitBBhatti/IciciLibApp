package com.fitpass.libfitpass.fontcomponent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class FitpassFontAwesome extends TextView {
    public FitpassFontAwesome(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public FitpassFontAwesome(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public FitpassFontAwesome(Context context) {
        super(context);
        init();
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fitpass_icicifont.ttf");
        setTypeface(tf);
    }
}