package pt.castro.mornings2.animation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.util.AttributeSet;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ImageChangeTransition extends TransitionSet {
    public ImageChangeTransition() {
        init();
    }

    /**
     * This constructor allows us to use this transition in XML
     */
    public ImageChangeTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds()).addTransition(new ChangeImageTransform()).addTransition
                (new ChangeTransform());
    }
}