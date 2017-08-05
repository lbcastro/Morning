package pt.castro.mornings2.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.transition.ChangeImageTransform;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.AttributeSet;

/**
 * Transition that performs almost exactly like {@link android.transition.AutoTransition}, but
 * has an
 * added {@link ChangeImageTransform} to support properly scaling up our gorgeous kittens.
 *
 * @author bherbst
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class SlideUpFadeTransition extends TransitionSet {
    public SlideUpFadeTransition() {
        init();
    }

    /**
     * This constructor allows us to use this transition in XML
     */
    public SlideUpFadeTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrdering(ORDERING_TOGETHER);
     addTransition(new Slide());
    }
}