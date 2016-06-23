package kr.docs.colorpicker;

import android.graphics.Color;

import java.util.StringTokenizer;

public class ColorEx {

    public static int parseColor(String s) {
        if(s==null || s.length() < 3) return 0;
        if(s.equals("transparent")) return Color.argb(0, 0, 0, 0);
        if(s.charAt(0) == '#') return Color.parseColor(s);

        StringTokenizer st = new StringTokenizer(s, " ,()");
        if(st.hasMoreTokens()) st.nextToken();
        int r = 0, g = 0, b = 0 , a = 255;

        try {
            if(st.hasMoreTokens()){
                s = st.nextToken();
                if(s.endsWith("%")){///**/""
                    s = s.substring(0, s.length() -1);
                    r = (int)(Integer.parseInt(s) * 255 / 100);
                }else{
                    r = Integer.parseInt(s);
                }

                if(st.hasMoreTokens()){
                    s = st.nextToken();
                    if(s.endsWith("%")){
                        s = s.substring(0, s.length() -1);
                        g = (int)(Integer.parseInt(s) * 255 / 100);
                    }else{
                        g = Integer.parseInt(s);
                    }
                    if(st.hasMoreTokens()){
                        s = st.nextToken();
                        if(s.endsWith("%")){
                            s = s.substring(0, s.length() -1);
                            b = (int)(Integer.parseInt(s) * 255 / 100);
                        }else{
                            b = Integer.parseInt(s);
                        }
                        if(st.hasMoreTokens()){
                            s = st.nextToken();
                            if(s.endsWith("%")){
                                s = s.substring(0, s.length() -1);
                                a = (int)(Integer.parseInt(s) * 255 / 100);
                            }else{
                                a = Integer.parseInt(s);
                            }
                        }
                    }
                }
            }
        } catch(Throwable e) {
            return 0;
        }

        return Color.argb(a, r, g, b);
    }

    public static String hexColor(int color) {
        return String.format("#%08X", 0xFFFFFFFF & color);
    }
}
