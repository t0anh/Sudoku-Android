package android.hfad.com.sudoku;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.style.ImageSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tuana on 01-04-2018.
 */

public class SpannableWithImage {

    private static final Spannable.Factory spannableFactory = Spannable.Factory.getInstance();

    private static boolean addImages(Context context, Spannable spannable, float height) {
        Pattern refImg = Pattern.compile("\\Q[img src=\\E([a-zA-Z0-9_]+?)\\Q/]\\E");
        boolean hasChanges = false;

        Matcher matcher = refImg.matcher(spannable);
        while (matcher.find()) {
            boolean set = true;
            for (ImageSpan span : spannable.getSpans(matcher.start(), matcher.end(), ImageSpan.class)) {
                if (spannable.getSpanStart(span) >= matcher.start()
                        && spannable.getSpanEnd(span) <= matcher.end()
                        ) {
                    spannable.removeSpan(span);
                } else {
                    set = false;
                    break;
                }
            }
            String resName = spannable.subSequence(matcher.start(1), matcher.end(1)).toString().trim();
            int id = context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
            Drawable mDrawable = context.getResources().getDrawable(id);
            mDrawable.setBounds(0, 0, (int)height, (int)height);
            if (set) {
                hasChanges = true;
                spannable.setSpan(  new ImageSpan(mDrawable),
                        matcher.start(),
                        matcher.end(),
                        0
                );
            }
        }

        return hasChanges;
    }

    static Spannable getTextWithImages(Context context, CharSequence text, float height) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addImages(context, spannable, height);
        return spannable;
    }
}
