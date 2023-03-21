package com.example.mobileprogramming_assignment;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.res.ResourcesCompat;
import android.util.AttributeSet;

public class cairoEditText extends AppCompatEditText
{


    public cairoEditText(Context context)
    {
        super(context);
        init();
    }

    public cairoEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public cairoEditText(Context context, AttributeSet attrs, int defStyleAttr)
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
