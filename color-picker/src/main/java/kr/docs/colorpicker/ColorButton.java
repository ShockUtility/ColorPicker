package kr.docs.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class ColorButton extends Button implements View.OnClickListener {

    private int mCurrentColor = 0xFFFFFFFF;

    public ColorButton(Context context, AttributeSet attr) {
        super(context, attr);

        this.setShadowLayer(2, 1, 1, Color.BLACK);

        TypedArray a = context.getTheme().obtainStyledAttributes(attr, R.styleable.ColorButton, 0, 0);
        try {
            mCurrentColor = a.getColor(R.styleable.ColorButton_currentColor, 0xFFFFFFFF);
        } finally {
            a.recycle();
        }

        this.setBackgroundColor(mCurrentColor);
    }

    public int getCurrentColor() {
        return mCurrentColor;
    }

    public void setCurrentColor(int color) {
        mCurrentColor = color;
        this.setBackgroundColor(mCurrentColor);
    }

    @Override
    public void onClick(View v) {
        AppCompatActivity activity = (AppCompatActivity)getContext();
        FragmentManager fm = activity.getSupportFragmentManager();
        ColorPickerFragment dialog = new ColorPickerFragment();
        dialog.defaultColor = mCurrentColor;
        dialog.clickedButton = this;
        dialog.show(fm, null);
    }
}
