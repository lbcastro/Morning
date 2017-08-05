package pt.castro.mornings2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import pt.castro.mornings2.animation.ImageChangeTransition;
import pt.castro.mornings2.animation.SlideInItemAnimator;
import pt.castro.mornings2.models.ShowModel;

/**
 * Created by lourenco on 20/06/2017.
 */

public class ListFragment extends Fragment {

    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    private TextView mMessages;
    private RecyclerViewPresenter mRecyclerViewPresenter;
    private Handler mHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMessages = (TextView) view.findViewById(R.id.messages);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new SlideInItemAnimator());

        if (mRecyclerViewPresenter != null) {
            mRecyclerViewPresenter.updateView(recyclerView);
            mMessages.setVisibility(View.GONE);
            return;
        }

        mRecyclerViewPresenter = new RecyclerViewPresenter(recyclerView);
        mRecyclerViewPresenter.loadShows(new RecyclerViewPresenter.LoadShowsObserver() {
            @Override
            public void onShowsLoaded(List<ShowModel> shows) {
                if (shows.size() == 0) {
                    String[] weekend = getResources().getStringArray(R.array.weekend);
                    int random = new Random().nextInt(weekend.length);
                    mMessages.setText(weekend[random]);
                    mMessages.setVisibility(View.VISIBLE);
                } else {
                    mMessages.setVisibility(View.GONE);
                }
            }

            @Override
            public void onItemClick(ShowModel showModel, View showView) {
                DetailsFragment detailsFragment = new DetailsFragment();
                detailsFragment.setShowModel(showModel);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    Fade fadeIn = new Fade(Fade.IN);
                    TransitionSet transition = new TransitionSet();
                    transition.setOrdering(TransitionSet.ORDERING_TOGETHER);
                    transition.addTransition(fadeIn);
                    detailsFragment.setEnterTransition(transition);

                    detailsFragment.setSharedElementEnterTransition(new ImageChangeTransition());

                    setExitTransition(new Fade(Fade.OUT));
                    detailsFragment.setSharedElementReturnTransition(new ImageChangeTransition());
                }

                View image = showView.findViewById(R.id.profile_image);
                View showName = showView.findViewById(R.id.show_name);
                View radioName = showView.findViewById(R.id.radio_name);
                View row = showView.findViewById(R.id.parentFrame);

                detailsFragment.setTransitions(showName.getTransitionName(), radioName
                        .getTransitionName(), image.getTransitionName(), row.getTransitionName());

                getFragmentManager().beginTransaction().addSharedElement(image, image
                        .getTransitionName()).addSharedElement(showName, showName
                        .getTransitionName()).addSharedElement(radioName, radioName
                        .getTransitionName()).addSharedElement(row, row.getTransitionName())
                        .replace(R.id.container, detailsFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable
                    (BUNDLE_RECYCLER_LAYOUT);
            mRecyclerViewPresenter.getLayoutManager().onRestoreInstanceState
                    (savedRecyclerLayoutState);
            mRecyclerViewPresenter.onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        mRecyclerViewPresenter.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerViewPresenter.getLayoutManager()
                .onSaveInstanceState());
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecyclerViewPresenter.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerViewPresenter.onDestroy();
    }

    public static Bitmap loadView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getWidth(), v.getHeight());
        v.draw(c);
        return b;
    }

    public GenericPresenter getPresenter() {
        return mRecyclerViewPresenter;
    }

    public void setAutoScroll(boolean enabled) {
        mRecyclerViewPresenter.setAutoScroll(enabled);
    }

    public void setMessage(String text) {
        mMessages.setText(text);
    }

}
