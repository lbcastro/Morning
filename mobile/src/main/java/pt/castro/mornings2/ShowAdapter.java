package pt.castro.mornings2;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pt.castro.mornings2.models.ShowModel;
import pt.castro.mornings2.utils.ImageUtils;
import pt.castro.mornings2.utils.TimeUtils;

/**
 * Created by lourenco on 30/05/2017.
 */

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {

    private static Toast sToast;

    private List<ShowModel> mShows;
    private Context mContext;

    private RecyclerViewPresenter.LoadShowsObserver mObserver;

    private int mFirstOfInterest = -1;
    private boolean previousWasOver;

    private int currentMinute = 20;
    private int currentHour = 7;

    public ShowAdapter(List<ShowModel> mShows, Context mContext,
            RecyclerViewPresenter.LoadShowsObserver observer) {
        this.mShows = mShows;
        this.mContext = mContext;
        this.mObserver = observer;
    }

    public Context getContext() {
        return mContext;
    }

    public void update() {
        mFirstOfInterest = -1;
        if (ApplicationExtension.DEBUG) {
            currentMinute++;
            if (currentMinute >= 60) {
                currentMinute = 0;
                currentHour++;
            }
//            if (sToast != null) {
//                sToast.cancel();
//            }
//            sToast = Toast.makeText(mContext, "" + currentHour + ":" + currentMinute, Toast
//                    .LENGTH_SHORT);
//            sToast.show();
        }

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View showView = layoutInflater.inflate(R.layout.row, parent, false);
        return new ViewHolder(showView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ShowModel show = mShows.get(position);

        Calendar calendar = Calendar.getInstance();
        if (ApplicationExtension.DEBUG) {
            calendar.set(Calendar.HOUR_OF_DAY, currentHour);
            calendar.set(Calendar.MINUTE, currentMinute);
        }

        int visibility;
        if (position > 0) {
            int previousTime = mShows.get(position - 1).getShowTime();
            if (show.getShowTime() == previousTime) {
                visibility = View.INVISIBLE;
            } else {
                visibility = View.VISIBLE;
            }
            holder.mSeparator.setVisibility(visibility);
            holder.mShowTime.setVisibility(visibility);
        } else {
            visibility = View.VISIBLE;
            holder.mSeparator.setVisibility(visibility);
            holder.mShowTime.setVisibility(visibility);
        }

        holder.mSeparator.setBackgroundColor(ContextCompat.getColor(mContext, R.color
                .separator_color));
        holder.mStatusIndicator.setVisibility(View.INVISIBLE);
        holder.setDefaultValues(show);
        int backgroundColor;
        View background = holder.mBackground;

        int showState = TimeUtils.getShowState(calendar, show);
        switch (showState) {
            case TimeUtils.SHOW_ONGOING:
                if (mFirstOfInterest < 0) {
                    mFirstOfInterest = holder.getAdapterPosition();
                }
                backgroundColor = ContextCompat.getColor(mContext, R.color.ongoing);
                holder.mStatusIndicator.setVisibility(visibility);
                holder.setDefaultColors();
                previousWasOver = false;
                break;
            case TimeUtils.SHOW_ENDED:
                backgroundColor = ContextCompat.getColor(mContext, R.color.white);
                holder.setDisabledColors();
                previousWasOver = true;
                break;
            case TimeUtils.SHOW_UPCOMING:
                if (mFirstOfInterest < 0) {
                    mFirstOfInterest = holder.getAdapterPosition();
                }
                holder.setDefaultColors();
                backgroundColor = ContextCompat.getColor(mContext, R.color.upcoming);
                previousWasOver = false;
                break;
            case TimeUtils.SHOW_LATER:
            default:
                if (mFirstOfInterest < 0) {
                    mFirstOfInterest = holder.getAdapterPosition();
                }
                holder.setDefaultColors();
                backgroundColor = ContextCompat.getColor(mContext, R.color.white);
                if (previousWasOver) {
                    holder.mSeparator.setBackgroundColor(ContextCompat.getColor(mContext, R.color
                            .colorPrimary));
                }
                previousWasOver = false;
                break;
        }

        changeColor(background, backgroundColor);

        CircleImageView imageView = holder.mImage;
        if (show.getRadioModel() != null && show.getRadioModel().getImage() != null) {
            imageView.setImageBitmap(ImageUtils.decodeImage(show.getRadioModel().getImage()));
        }

        if (holder.getAdapterPosition() == 0) {
            holder.mSeparator.setVisibility(View.INVISIBLE);
        }

        holder.mImage.setTransitionName(show.getShowName() + show.getShowTime() + "_image");
        holder.mShowName.setTransitionName(show.getShowName() + show.getShowTime() + "_show");
        holder.mRadioName.setTransitionName(show.getShowName() + show.getShowTime() + "_radio");
        holder.mRow.setTransitionName(show.getShowName() + show.getShowTime() + "_background");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObserver.onItemClick(show, v);
            }
        });
    }

    public int getFirstofInterest() {
        return mFirstOfInterest;
    }

    private void changeColor(final View view, int colorTo) {
        view.setBackgroundColor(colorTo);
    }

    @Override
    public int getItemCount() {
        return mShows.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mShowTime;
        public TextView mShowName;
        public TextView mRadioName;
        public View mBackground;
        public LinearLayout mRow;
        public CircleImageView mImage;
        public ImageView mStatusIndicator;
        public View mSeparator;

        public ViewHolder(View itemView) {
            super(itemView);
            mShowTime = (TextView) itemView.findViewById(R.id.show_time);
            mShowName = (TextView) itemView.findViewById(R.id.show_name);
            mRadioName = (TextView) itemView.findViewById(R.id.radio_name);
            mBackground = itemView.findViewById(R.id.background);
            mImage = (CircleImageView) itemView.findViewById(R.id.profile_image);
            mStatusIndicator = (ImageView) itemView.findViewById(R.id.status_indicator);
            mSeparator = itemView.findViewById(R.id.separator);
            mRow = (LinearLayout) itemView.findViewById(R.id.parentFrame);
        }

        public void setDefaultValues(ShowModel show) {
            mShowTime.setText(show.getShowTimeAsText());
            mShowName.setText(show.getShowName());
            mRadioName.setText(show.getRadioModel().getName());
        }

        public void setDefaultColors() {
            mShowName.setTextColor(Color.DKGRAY);
            mShowTime.setTextColor(Color.DKGRAY);
            mRadioName.setTextColor(Color.DKGRAY);
        }

        public void setDisabledColors() {
            mShowName.setTextColor(Color.LTGRAY);
            mShowTime.setTextColor(Color.GRAY);
            mRadioName.setTextColor(Color.LTGRAY);
        }
    }
}
