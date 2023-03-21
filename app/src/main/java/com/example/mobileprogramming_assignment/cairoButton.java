package com.example.mobileprogramming_assignment;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

public class cairoButton  extends AppCompatButton
{

    public cairoButton(Context context)
    {
        super(context);
        init();
    }

    public cairoButton(Context context, AttributeSet attrs)
    {
        this(context, attrs, androidx.appcompat.R.attr.borderlessButtonStyle);
        init();
    }

    public cairoButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        if (!isInEditMode())
        {
            setTextSize(18);
            Typeface tf = ResourcesCompat.getFont(getContext(), R.font.cairo);
            setTypeface(tf);
        }
    }
}
