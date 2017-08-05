package pt.castro.mornings2.models;

import java.util.HashMap;
import java.util.Map;

import pt.castro.mornings2.utils.TimeUtils;

/**
 * Created by lourenco on 30/05/2017.
 */

public class ShowModel {

    private String mShowName;
    private String mRadioName;
    private String mShowDescription;
    private String mShowImage;

    private int mShowTime;
    private int mShowTimeMinutesFromMidnight;
    private String mShowTimeAsText;
    private Map<String, Integer> mTimetable;

    private RadioModel mRadioModel;

    public ShowModel() {

    }

    public ShowModel(String showName, String radioName, String showDescription, String showImage,
            int showTime, Map<String, Integer> timetable) {
        this.mShowName = showName;
        this.mRadioName = radioName;
        this.mShowDescription = showDescription;
        this.mShowImage = showImage;
        this.mShowTime = showTime;
        this.mTimetable = timetable;
    }

    public String getShowName() {
        return mShowName;
    }

    public void setShowName(String mShowName) {
        this.mShowName = mShowName;
    }

    public String getRadioName() {
        return mRadioName;
    }

    public void setRadioName(String mRadioName) {
        this.mRadioName = mRadioName;
    }

    @Override
    public String toString() {
        return mShowName + "\t" + mRadioName;
    }

    public int getShowTime() {
        return mShowTime;
    }

    public void setShowTime(int showTime) {
        this.mShowTime = showTime;
        String time = String.valueOf(mShowTime);
        char[] chars = time.toCharArray();

        String minutes = "";
        String hours = "";
        for (int x = chars.length - 1; x >= 0; x--) {
            if (chars.length - x < 3) {
                minutes = chars[x] + minutes;
            } else {
                hours = chars[x] + hours;
            }
        }

        hours = TimeUtils.fillTimeEntry(hours);
        minutes = TimeUtils.fillTimeEntry(minutes);

        mShowTimeAsText = hours + ":" + minutes;
        mShowTimeMinutesFromMidnight = Integer.parseInt(hours) * 60;
        mShowTimeMinutesFromMidnight += Integer.parseInt(minutes);
    }

    public void setTimetable(HashMap<String, Integer> timetable) {
        this.mTimetable = timetable;
    }

    public Map<String, Integer> getTimetable() {
        return mTimetable;
    }

    public RadioModel getRadioModel() {
        return mRadioModel;
    }

    public void setRadioModel(RadioModel radioModel) {
        this.mRadioModel = radioModel;
    }

    public int getMinutesFromMidnight() {
        return mShowTimeMinutesFromMidnight;
    }

    public String getShowTimeAsText() {
        return mShowTimeAsText;
    }

    public String getShowDescription() {
        return mShowDescription;
    }

    public void setShowDescription(String showDescription) {
        this.mShowDescription = showDescription;
    }

    public String getShowImage() {
        return mShowImage;
    }

    public void setShowImage(String showImage) {
        this.mShowImage = showImage;
    }
}
