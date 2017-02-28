package prakhar.com.githubrepo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Lendingkart on 12/30/2016.
 */

public class CustomFontTextView extends TextView {

    public CustomFontTextView(Context context) {
        super(context);
        CustomFontUtils.applyCustomFont(this, context, null);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontUtils.applyCustomFont(this, context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CustomFontUtils.applyCustomFont(this, context, attrs);
    }
}
