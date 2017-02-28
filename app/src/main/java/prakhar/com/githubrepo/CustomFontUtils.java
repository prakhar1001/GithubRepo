package prakhar.com.githubrepo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Lendingkart on 12/30/2016.
 */

public class CustomFontUtils {

    private static final String INPUT_OPEN_SANS_SEMI_BOLD = "OpenSansSemiBold";
    private static final String INPUT_OPEN_SANS_SEMI_BOLD_ITALIC = "OpenSansSemiBoldItalic";

    public static void applyCustomFont(View customFontView, Context context, AttributeSet attrs) {
        Typeface customFont;
        if (customFontView instanceof CustomFontTextView) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
            CustomFontTextView fontTextView = (CustomFontTextView) customFontView;
            String fontName = typedArray.getString(R.styleable.CustomFontTextView_fontStyleTextView);
            customFont = selectTypeFace(context, fontName);
            if (customFont != null)
                fontTextView.setTypeface(customFont);
            typedArray.recycle();
            return;
        }
    }

    private static Typeface selectTypeFace(Context context, String fontName) {
        if (fontName != null) {
            switch (fontName) {
                case INPUT_OPEN_SANS_SEMI_BOLD:
                    return FontCache.getTypeface("OpenSans-Semibold.ttf", context);
                case INPUT_OPEN_SANS_SEMI_BOLD_ITALIC:
                    return FontCache.getTypeface("OpenSans-SemiboldItalic.ttf", context);
                default:
                    return FontCache.getTypeface("OpenSans-Regular.ttf", context);
            }
        } else {
            return null;
        }
    }
}
