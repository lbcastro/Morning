package pt.castro.mornings2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pt.castro.mornings2.models.ShowModel;
import pt.castro.mornings2.utils.ImageUtils;
import pt.castro.mornings2.utils.TimeUtils;

/**
 * Created by lourenco on 18/06/2017.
 */

public class DetailsFragment extends DialogFragment {

    private ShowModel mShowModel;

    private String mShowNameTransition;
    private String mRadioNameTransition;
    private String mImageTransition;
    private String mRowTransition;

    public void setShowModel(ShowModel showModel) {
        mShowModel = showModel;
    }

    public void setTransitions(String show, String radio, String image, String row) {
        mShowNameTransition = show;
        mRadioNameTransition = radio;
        mImageTransition = image;
        mRowTransition = row;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
//        setSharedElementEnterTransition(new ImageChangeTransition());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        View parentLayout = view.findViewById(R.id.parentLayout);
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        TextView showName = (TextView) view.findViewById(R.id.show_name);
        showName.setText(mShowModel.getShowName());
        showName.setTransitionName(mShowNameTransition);

        TextView showRadio = (TextView) view.findViewById(R.id.radio_name);
        showRadio.setText(mShowModel.getRadioModel().getName());
        showRadio.setTransitionName(mRadioNameTransition);

        TextView showDescription = (TextView) view.findViewById(R.id.show_description);
        showDescription.setText(mShowModel.getShowDescription());

        ImageView showImage = (ImageView) view.findViewById(R.id.background);
        showImage.setImageBitmap(ImageUtils.decodeImage(mShowModel.getShowImage()));

        LinearLayout parent = (LinearLayout) view.findViewById(R.id.parent);
        parent.setTransitionName(mRowTransition);

        LinearLayout timeIndicators = (LinearLayout) view.findViewById(R.id.time_indicators);
        Map<String, Integer> timetable = mShowModel.getTimetable();
        for (int x = 0; x < timeIndicators.getChildCount(); x++) {
            String dayOfWeek = FirebaseKeys.calendarIntToKey(x + 2);
            if (timetable.get(dayOfWeek) != null) {
                TextView timeIndicator = (TextView) timeIndicators.getChildAt(x);
                timeIndicator.setText(TimeUtils.calendarIntToLabel(getActivity(), x + 2) + " " +
                        TimeUtils.showTimeToText(timetable.get(dayOfWeek)));
            }
        }

        CircleImageView imageView = (CircleImageView) view.findViewById(R.id.profile_image);
        if (mShowModel.getRadioModel() != null && mShowModel.getRadioModel().getImage() != null) {
            imageView.setImageBitmap(ImageUtils.decodeImage(mShowModel.getRadioModel().getImage()));
            imageView.setTransitionName(mImageTransition);
        }

        startPostponedEnterTransition();

        return view;
    }
}
