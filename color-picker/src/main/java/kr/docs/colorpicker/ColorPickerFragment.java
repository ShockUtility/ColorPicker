package kr.docs.colorpicker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class ColorPickerFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    final  String       colorTable = "#00000000,#ffffff,#e2e2e2,#bbbbbb,#8e8e8e,#707070,#545454,#383838,#1c1c1c,#000000,#d8fff8,#91ffec,#49ffe0,#00ffd4,#00d8b4,#00b799,#00967d,#007561,#005446,#00332a,#d8ffeb,#a3ffd1,#6dffb6,#38ff9b,#00ff7f,#00d369,#00a854,#007c3e,#005446,#002613,#d8ffd8,#a3ffa3,#6dff6d,#38ff38,#00ff00,#00d600,#00ad00,#008400,#005b00,#003300,#ebffd8,#d1ffa3,#b6ff6d,#9bff38,#7fff00,#6cd800,#59b200,#468c00,#336600,#1f3f00,#ffffd8,#ffff91,#ffff49,#ffff00,#d8d800,#b7b700,#969600,#757500,#545400,#333300,#fff5d8,#ffeaad,#ffdf82,#ffd456,#ffca2b,#ffbf00,#cc9900,#997200,#664c00,#332600,#ffebd8,#ffd6ad,#ffc082,#ffaa56,#ff952b,#ff7f00,#d16800,#a05000,#703800,#3f1f00,#ffe2d8,#ffc1ad,#ffa182,#ff8056,#ff602b,#ff3f00,#d63500,#a82a00,#7a1e00,#4c1300,#ffd8d8,#ffadad,#ff8282,#ff5656,#ff2b2b,#ff0000,#d10000,#a00000,#700000,#3f0000,#ffd8eb,#ffadd6,#ff82c0,#ff56aa,#ff2b95,#ff007f,#cc0066,#99004c,#660033,#330019,#ffd8ff,#ffa3ff,#ff6dff,#ff38ff,#ff00ff,#d600d6,#ad00ad,#840084,#5b005b,#330033,#ebd8ff,#d6adff,#c082ff,#aa56ff,#952bff,#7f00ff,#6600cc,#4c0099,#330066,#190033,#d8d8ff,#adadff,#8282ff,#5656ff,#2b2bff,#0000ff,#0000d1,#0000a0,#000070,#00003f,#d8e5ff,#adc8ff,#82abff,#568eff,#2b71ff,#0055ff,#0044cc,#003399,#002266,#001133,#d8f2ff,#a3e0ff,#6dceff,#38bcff,#00a9ff,#008ed6,#0073ad,#005884,#003d5b,#002133";

    public int          defaultColor;
    public ColorButton  clickedButton;

    Button          btnDone;
    ColorButton     btnColor;
    TextView        txtColor;
    SeekBar         sbAlpha;
    GridView        gridView;

    int             mRowHeight;
    Context         mContext;

    public interface ColorPickerListener {
        public void onColorPickerDone(ColorButton button);
    }

    ColorPickerListener colorPickerListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_color_picker, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        btnDone  = (Button)rootView.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);
        btnColor = (ColorButton)rootView.findViewById(R.id.btnColor);
        txtColor = (TextView)rootView.findViewById(R.id.txtColor);
        sbAlpha  = (SeekBar)rootView.findViewById(R.id.sbAlpha);
        gridView = (GridView)rootView.findViewById(R.id.gridView);

        btnColor.setCurrentColor(defaultColor);
        btnColor.setOnClickListener(null);
        txtColor.setText(String.format("%06X", 0xFFFFFF & defaultColor));

        txtColor.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId==EditorInfo.IME_ACTION_DONE || event.getKeyCode()==KeyEvent.KEYCODE_ENTER) {
                    String s = v.getText().toString();
                    if (s.length()==6) {
                        int color;
                        try {
                            color = ColorEx.parseColor("#"+s);
                            btnColor.setCurrentColor(Color.argb(sbAlpha.getProgress()+1, Color.red(color), Color.green(color), Color.blue(color)));
                        } catch (Exception e) {
                            txtColor.setText("");
                            Toast.makeText(getActivity(), getString(R.string.picker_invalid_color), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.picker_invalid_digit), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });

        int alpha = Color.alpha(defaultColor);
        sbAlpha.setProgress(alpha==0 ? 254 : alpha);
        sbAlpha.setOnSeekBarChangeListener(this);

        // 그리드 리스트의 어답터를 셋팅한다
        gridView.setColumnWidth(mRowHeight);
        ArrayList colors = new ArrayList(new ArrayList<String>(Arrays.asList(colorTable.split(","))));
        ColorAdapter adapter = new ColorAdapter(mContext, R.layout.cell_color_picker, colors);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l_position) {
                ColorAdapter adapter = (ColorAdapter)parent.getAdapter();
                int color = Color.parseColor(adapter.getItem(position));
                btnColor.setCurrentColor(color);
                sbAlpha.setProgress(254);
                txtColor.setText(String.format("%06X", 0xFFFFFF & color));
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mContext = activity;
            mRowHeight = (int)(320 * activity.getResources().getDisplayMetrics().density / 10);
            colorPickerListener = (ColorPickerListener) activity;
        } catch (ClassCastException e) {}
    }

    @Override
    public void onClick(View v) {
        try {
            int color = ColorEx.parseColor("#"+txtColor.getText());
            btnColor.setCurrentColor(Color.argb(sbAlpha.getProgress()+1, Color.red(color), Color.green(color), Color.blue(color)));
        } catch (Exception e) {}

        clickedButton.setCurrentColor(btnColor.getCurrentColor());
        colorPickerListener.onColorPickerDone(clickedButton);
        dismiss();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int color = btnColor.getCurrentColor();
        btnColor.setCurrentColor(Color.argb(progress+1, Color.red(color), Color.green(color), Color.blue(color)));
        txtColor.setText(String.format("%06X", 0xFFFFFF & btnColor.getCurrentColor()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int color = btnColor.getCurrentColor();
        btnColor.setCurrentColor(Color.argb(seekBar.getProgress()+1, Color.red(color), Color.green(color), Color.blue(color)));
        txtColor.setText(String.format("%06X", 0xFFFFFF & btnColor.getCurrentColor()));
    }

    class ColorAdapter extends BaseAdapter {
        public ArrayList data;
        private LayoutInflater inflater;
        private int layout;

        public ColorAdapter(Context context, int layout, ArrayList data){
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.data = data;
            this.layout = layout;
        }

        @Override
        public int getCount() { return data.size(); }

        @Override
        public String getItem(int position) { return (String) data.get(position); }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView==null) convertView = inflater.inflate(layout,null);

            String color = (String) data.get(position);
            convertView.setBackgroundColor(Color.parseColor(color));
            convertView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, mRowHeight));

            return convertView;
        }
    }
}