# ColorPicker
Color Picker for Android.

Easy to use. 

Project consists three main classes which can be easily extended and suited to user requirements.


# Screen Shot
<p align="center" >
<img src="https://github.com/ShockUtility/ColorPicker/blob/master/Screenshot.png">
</p>

# Installation (build.gradle)
```sh
dependencies {
    compile 'kr.docs:color-picker:0.0.9'
}
```

# Layout
```sh
    <kr.docs.colorpicker.ColorButton
        android:layout_width="wrap_content"
        android:layout_height="37dp"
        android:text="Color" 
        custom:currentColor="#30384a" />
```

# Code
```sh
public class MainActivity extends AppCompatActivity implements ColorPickerFragment.ColorPickerListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onColorPickerDone (ColorButton button) {
        Log.d("MainActivity", "Selected Color : " + ColorEx.hexColor(button.getCurrentColor()));
    }
}
```

# License
MIT
