package fr.xgouchet.scparchive.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerTitleStrip;
import android.util.AttributeSet;
import android.widget.TextView;

import fr.xgouchet.scparchive.BaseApplication;

/**
 * @author Xavier Gouchet
 */
public class TypefacePagerTitleStrip extends PagerTitleStrip {


    private final Typeface typeface;

    public TypefacePagerTitleStrip(Context context) {
        super(context);
        typeface = BaseApplication.from(context)
                .getAppComponent().getTypefaceForTitle();
    }

    public TypefacePagerTitleStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
        typeface = BaseApplication.from(context)
                .getAppComponent().getTypefaceForTitle();
    }


    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for (int i = 0; i < this.getChildCount(); i++) {
            if (this.getChildAt(i) instanceof TextView) {
                ((TextView) this.getChildAt(i)).setTypeface(typeface);
            }
        }
    }
}
