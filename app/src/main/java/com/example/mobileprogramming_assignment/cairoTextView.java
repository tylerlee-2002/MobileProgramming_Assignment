package com.example.mobileprogramming_assignment;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import android.util.AttributeSet;

public class cairoTextView extends AppCompatTextView
{

    public cairoTextView(Context context)
    {
        super(context);
        init();
    }

    public cairoTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public cairoTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        if (!isInEditMode())
        {
            Typeface tf = ResourcesCompat.getFont(getContext(), R.font.cairo);
            setTypeface(tf);
        }
    }
}
