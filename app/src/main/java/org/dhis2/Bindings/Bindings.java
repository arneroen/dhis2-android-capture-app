package org.dhis2.Bindings;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.dhis2.R;
import org.dhis2.usescases.programEventDetail.ProgramEventViewModel;
import org.dhis2.utils.CatComboAdapter;
import org.dhis2.utils.DateUtils;
import org.dhis2.utils.Period;
import org.hisp.dhis.android.core.category.CategoryOptionComboModel;
import org.hisp.dhis.android.core.common.ObjectStyleModel;
import org.hisp.dhis.android.core.common.State;
import org.hisp.dhis.android.core.enrollment.EnrollmentModel;
import org.hisp.dhis.android.core.enrollment.EnrollmentStatus;
import org.hisp.dhis.android.core.event.EventModel;
import org.hisp.dhis.android.core.event.EventStatus;
import org.hisp.dhis.android.core.imports.ImportStatus;
import org.hisp.dhis.android.core.period.PeriodType;
import org.hisp.dhis.android.core.program.ProgramModel;
import org.hisp.dhis.android.core.program.ProgramStageModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

/**
 * QUADRAM. Created by ppajuelo on 28/09/2017.
 */

public class Bindings {

    @BindingAdapter("elevation")
    public static void setElevation(View view, float elevation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setElevation(elevation);
        } else {
            Drawable drawable = view.getResources().getDrawable(android.R.drawable.dialog_holo_light_frame);
            drawable.setColorFilter(new PorterDuffColorFilter(view.getResources().getColor(R.color.colorGreyDefault), PorterDuff.Mode.MULTIPLY));
            view.setBackground(drawable);
        }
    }

    @BindingAdapter("scrollingTextView")
    public static void setScrollingTextView(TextView textView, boolean canScroll) {
        if (canScroll) {
            textView.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    @BindingAdapter("date")
    public static void setDate(TextView textView, String date) {
        SimpleDateFormat formatIn = DateUtils.databaseDateFormat();
        SimpleDateFormat formatOut = DateUtils.uiDateFormat();
        try {
            Date dateIn = formatIn.parse(date);
            String dateOut = formatOut.format(dateIn);
            textView.setText(dateOut);
        } catch (ParseException e) {
            Timber.e(e);
        }

    }

    @BindingAdapter("date")
    public static void parseDate(TextView textView, Date date) {
        if (date != null) {
            SimpleDateFormat formatOut = DateUtils.uiDateFormat();
            String dateOut = formatOut.format(date);
            textView.setText(dateOut);
        }

    }

    @BindingAdapter("drawableEnd")
    public static void setDrawableEnd(TextView textView, Drawable drawable) {
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (drawable instanceof AnimatedVectorDrawable)
                ((AnimatedVectorDrawable) drawable).start();
        }
    }

    @BindingAdapter(value = {"initGrid", "spanCount"}, requireAll = false)
    public static void setLayoutManager(RecyclerView recyclerView, boolean horizontal, int spanCount) {
        RecyclerView.LayoutManager recyclerLayout;
        if (spanCount == -1)
            spanCount = 1;

        recyclerLayout = new GridLayoutManager(recyclerView.getContext(), spanCount, RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(recyclerLayout);

    }

    @BindingAdapter("spanSize")
    public static void setSpanSize(RecyclerView recyclerView, boolean setSpanSize) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemViewType = recyclerView.getAdapter().getItemViewType(position);
                    return (itemViewType == 4 || itemViewType == 8) ? 2 : 1;
                }
            });
        }
    }

    @BindingAdapter("progressColor")
    public static void setProgressColor(ProgressBar progressBar, int color) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = progressBar.getContext().obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimary});
        int color2 = a.getColor(0, 0);
        a.recycle();
        progressBar.getIndeterminateDrawable().setColorFilter(color2, PorterDuff.Mode.SRC_IN);
    }

    @BindingAdapter("enrolmentIcon")
    public static void setEnrolmentIcon(ImageView view, EnrollmentStatus status) {
        Drawable lock;
        if (status == null)
            status = EnrollmentStatus.ACTIVE;
        switch (status) {
            case ACTIVE:
                lock = AppCompatResources.getDrawable(view.getContext(), R.drawable.ic_lock_open_green);
                break;
            case COMPLETED:
                lock = AppCompatResources.getDrawable(view.getContext(), R.drawable.ic_lock_completed);
                break;
            case CANCELLED:
                lock = AppCompatResources.getDrawable(view.getContext(), R.drawable.ic_lock_inactive);
                break;
            default:
                lock = AppCompatResources.getDrawable(view.getContext(), R.drawable.ic_lock_read_only);
                break;
        }

        view.setImageDrawable(lock);

    }

    @BindingAdapter("enrolmentText")
    public static void setEnrolmentText(TextView view, EnrollmentStatus status) {
        String text;
        if (status == null)
            status = EnrollmentStatus.ACTIVE;
        switch (status) {
            case ACTIVE:
                text = view.getContext().getString(R.string.event_open);
                break;
            case COMPLETED:
                text = view.getContext().getString(R.string.completed);
                break;
            case CANCELLED:
                text = view.getContext().getString(R.string.cancelled);
                break;
            default:
                text = view.getContext().getString(R.string.read_only);
                break;
        }

        view.setText(text);
    }

    @BindingAdapter(value = {"eventStatusIcon", "enrollmentStatusIcon", "eventProgramStage", "eventProgram"}, requireAll = false)
    public static void setEventIcon(ImageView view, EventModel event, EnrollmentModel enrollmentModel, ProgramStageModel eventProgramStage, ProgramModel program) {
        if (event != null) {
            EventStatus status = event.status();
            EnrollmentStatus enrollmentStatus = enrollmentModel.enrollmentStatus();
            if (status == null)
                status = EventStatus.ACTIVE;
            if (enrollmentStatus == null)
                enrollmentStatus = EnrollmentStatus.ACTIVE;

            if (enrollmentStatus == EnrollmentStatus.ACTIVE) {
                switch (status) {
                    case ACTIVE:
                        if (DateUtils.getInstance().hasExpired(event, program.expiryDays(), program.completeEventsExpiryDays(), eventProgramStage.periodType() != null ? eventProgramStage.periodType() : program.expiryPeriodType())) {
                            view.setImageDrawable(AppCompatResources.getDrawable(view.getContext(), R.drawable.ic_eye_red));
                        } else {
                            view.setImageDrawable(AppCompatResources.getDrawable(view.getContext(), R.drawable.ic_edit));
                        }
                        break;
                    case OVERDUE:
                    case COMPLETED:
                    case SKIPPED:
                        view.setImageDrawable(AppCompatResources.getDrawable(view.getContext(), R.drawable.ic_visibility));
                        break;
                    case SCHEDULE:
                        view.setImageDrawable(AppCompatResources.getDrawable(view.getContext(), R.drawable.ic_edit));
                        break;
                    case VISITED:
                        view.setImageDrawable(AppCompatResources.getDrawable(view.getContext(), R.drawable.ic_edit));
                        break;
                    default:
                        view.setImageDrawable(AppCompatResources.getDrawable(view.getContext(), R.drawable.ic_edit));
                        break;
                }
            } else if (enrollmentStatus == EnrollmentStatus.COMPLETED) {
                view.setImageDrawable(AppCompatResources.getDrawable(view.getContext(), R.drawable.ic_visibility));
            } else { //EnrollmentStatus = CANCELLED
                view.setImageDrawable(AppCompatResources.getDrawable(view.getContext(), R.drawable.ic_visibility));
            }
        }
    }

    @BindingAdapter(value = {"eventStatusText", "enrollmentStatus", "eventProgramStage", "eventProgram"})
    public static void setEventText(TextView view, EventModel event, EnrollmentModel enrollmentModel, ProgramStageModel eventProgramStage, ProgramModel program) {
        if (event != null) {
            EventStatus status = event.status();
            EnrollmentStatus enrollmentStatus = enrollmentModel.enrollmentStatus();
            if (status == null)
                status = EventStatus.ACTIVE;
            if (enrollmentStatus == null)
                enrollmentStatus = EnrollmentStatus.ACTIVE;


            if (enrollmentStatus == EnrollmentStatus.ACTIVE) {
                switch (status) {
                    case ACTIVE:
                        Date eventDate = event.eventDate();
                        if(eventProgramStage.periodType().name().contains(PeriodType.Weekly.name()))
                            eventDate = DateUtils.getInstance().getNextPeriod(eventProgramStage.periodType(),eventDate,0,true);
                        if (DateUtils.getInstance().isEventExpired(eventDate, null, event.status(), program.completeEventsExpiryDays(), eventProgramStage.periodType() != null ? eventProgramStage.periodType() : program.expiryPeriodType(), program.expiryDays())) {
                            view.setText(view.getContext().getString(R.string.event_expired));
                        } else {
                            view.setText(view.getContext().getString(R.string.event_open));
                        }
                        break;
                    case COMPLETED:
                        if (DateUtils.getInstance().isEventExpired(null, event.completedDate(), program.completeEventsExpiryDays())) {
                            view.setText(view.getContext().getString(R.string.event_expired));
                        } else {
                            view.setText(view.getContext().getString(R.string.event_completed));
                        }
                        break;
                    case SCHEDULE:
                        if (DateUtils.getInstance().hasExpired(event, program.expiryDays(), program.completeEventsExpiryDays(), eventProgramStage.periodType() != null ? eventProgramStage.periodType() : program.expiryPeriodType())) {
                            view.setText(view.getContext().getString(R.string.event_expired));
                        } else {
                            view.setText(view.getContext().getString(R.string.event_schedule));
                        }
                        break;
                    case SKIPPED:
                        view.setText(view.getContext().getString(R.string.event_skipped));
                        break;
                    case OVERDUE:
                        view.setText(R.string.event_overdue);
                        break;
                    default:
                        view.setText(view.getContext().getString(R.string.read_only));
                        break;
                }
            } else if (enrollmentStatus == EnrollmentStatus.COMPLETED) {
                view.setText(view.getContext().getString(R.string.program_completed));
            } else { //EnrollmentStatus = CANCELLED
                view.setText(view.getContext().getString(R.string.program_inactive));
            }
        }
    }

    @BindingAdapter(value = {"eventColor", "eventProgramStage", "eventProgram"})
    public static void setEventColor(View view, EventModel event, ProgramStageModel programStage, ProgramModel program) {
        if (event != null) {
            int bgColor;
            if (DateUtils.getInstance().isEventExpired(null, event.completedDate(), program.completeEventsExpiryDays())) {
                bgColor = R.drawable.item_event_dark_gray_ripple;
            } else if (event.status() != null) {
                switch (event.status()) {
                    case ACTIVE:
                        Date eventDate = event.eventDate();
                        if(programStage.periodType().name().contains(PeriodType.Weekly.name()))
                            eventDate = DateUtils.getInstance().getNextPeriod(programStage.periodType(),eventDate,0,true);
                        if (DateUtils.getInstance().isEventExpired(eventDate, null, event.status(), program.completeEventsExpiryDays(), programStage.periodType() != null ? programStage.periodType() : program.expiryPeriodType(), program.expiryDays())) {                            bgColor = R.drawable.item_event_dark_gray_ripple;
                        } else
                            bgColor = R.drawable.item_event_yellow_ripple;
                        break;
                    case COMPLETED:
                        if (DateUtils.getInstance().isEventExpired(null, event.completedDate(), program.completeEventsExpiryDays())) {
                            bgColor = R.drawable.item_event_dark_gray_ripple;
                        } else
                            bgColor = R.drawable.item_event_gray_ripple;
                        break;
                    case SCHEDULE:
                        if (DateUtils.getInstance().hasExpired(event, program.expiryDays(), program.completeEventsExpiryDays(), programStage.periodType() != null ? programStage.periodType() : program.expiryPeriodType())) {
                            bgColor = R.drawable.item_event_dark_gray_ripple;
                        } else
                            bgColor = R.drawable.item_event_green_ripple;
                        break;
                    case VISITED:
                    case SKIPPED:
                    default:
                        bgColor = R.drawable.item_event_red_ripple;
                        break;
                }
            } else {
                bgColor = R.drawable.item_event_red_ripple;
            }
            view.setBackground(AppCompatResources.getDrawable(view.getContext(), bgColor));
        }
    }

    @BindingAdapter("statusColor")
    public static void setStatusColor(ImageView view, ImportStatus status) {
        Drawable icon;
        switch (status) {
            case ERROR:
                icon = AppCompatResources.getDrawable(view.getContext(), R.drawable.red_circle);
                break;
            case SUCCESS:
                icon = AppCompatResources.getDrawable(view.getContext(), R.drawable.green_circle);
                break;
            case WARNING:
                icon = AppCompatResources.getDrawable(view.getContext(), R.drawable.yellow_circle);
                break;
            default:
                icon = null;
                break;
        }
        view.setImageDrawable(icon);
    }

    @BindingAdapter("eventWithoutRegistrationStatusText")
    public static void setEventWithoutRegistrationStatusText(TextView textView, ProgramEventViewModel event) {
        switch (event.eventStatus()) {
            case ACTIVE:
                if (event.isExpired()) {
                    textView.setText(textView.getContext().getString(R.string.event_editing_expired));
                } else {
                    textView.setText(textView.getContext().getString(R.string.event_open));
                }
                break;
            case COMPLETED:
                if (event.isExpired()) {
                    textView.setText(textView.getContext().getString(R.string.event_editing_expired));
                } else {
                    textView.setText(textView.getContext().getString(R.string.event_completed));
                }
                break;
            case SKIPPED:
                textView.setText(textView.getContext().getString(R.string.event_editing_expired));
                break;
            default:
                textView.setText(textView.getContext().getString(R.string.read_only));
                break;
        }
    }

    @BindingAdapter("eventWithoutRegistrationStatusIcon")
    public static void setEventWithoutRegistrationStatusIcon(ImageView imageView, ProgramEventViewModel event) {

        if (event.eventStatus() == EventStatus.ACTIVE && !event.isExpired())
            imageView.setImageResource(R.drawable.ic_edit);
        else
            imageView.setImageResource(R.drawable.ic_visibility);
/*
        switch (event.eventStatus()) {
            case ACTIVE:
                if (event.isExpired()) {
                    imageView.setImageResource(R.drawable.ic_visibility);
                } else
                    imageView.setImageResource(R.drawable.ic_edit);
                break;
            default:
                imageView.setImageResource(R.drawable.ic_visibility);
                break;

        }*/
    }

    @BindingAdapter("stateText")
    public static void setStateText(TextView textView, State state) {
        switch (state) {
            case TO_POST:
                textView.setText(textView.getContext().getString(R.string.state_to_post));
                break;
            case TO_UPDATE:
                textView.setText(textView.getContext().getString(R.string.state_to_update));
                break;
            case TO_DELETE:
                textView.setText(textView.getContext().getString(R.string.state_to_delete));
                break;
            case ERROR:
                textView.setText(textView.getContext().getString(R.string.state_error));
                break;
            case SYNCED:
                textView.setText(textView.getContext().getString(R.string.state_synced));
                break;
            default:
                break;
        }
    }

    @BindingAdapter("stateIcon")
    public static void setStateIcon(ImageView imageView, State state) {
        if (state != null) {
            switch (state) {
                case TO_POST:
                case TO_UPDATE:
                case TO_DELETE:
                    imageView.setImageResource(R.drawable.ic_sync_problem_grey);
                    break;
                case ERROR:
                    imageView.setImageResource(R.drawable.ic_sync_problem_red);
                    break;
                case SYNCED:
                    imageView.setImageResource(R.drawable.ic_sync);
                    break;
                case WARNING:
                    imageView.setImageResource(R.drawable.ic_sync_warning);
                    break;
                case SENT_VIA_SMS:
                case SYNCED_VIA_SMS:
                    imageView.setImageResource(R.drawable.ic_sync_sms);
                    break;
                default:
                    break;
            }
        }
    }

    @BindingAdapter("spinnerOptions")
    public static void setSpinnerOptions(Spinner spinner, List<CategoryOptionComboModel> options) {
        CatComboAdapter adapter = new CatComboAdapter(spinner.getContext(),
                R.layout.spinner_layout,
                R.id.spinner_text,
                options,
                "",
                R.color.white_faf);
        spinner.setAdapter(adapter);
    }

    @BindingAdapter("fromResBgColor")
    public static void setFromResBgColor(View view, int color) {
        String tintedColor;

        ArrayList<Double> rgb = new ArrayList<>();
        rgb.add(Color.red(color) / 255.0d);
        rgb.add(Color.green(color) / 255.0d);
        rgb.add(Color.blue(color) / 255.0d);

        Double r = null;
        Double g = null;
        Double b = null;
        for (Double c : rgb) {
            if (c <= 0.03928d)
                c = c / 12.92d;
            else
                c = Math.pow(((c + 0.055d) / 1.055d), 2.4d);

            if (r == null)
                r = c;
            else if (g == null)
                g = c;
            else
                b = c;
        }

        double L = 0.2126d * r + 0.7152d * g + 0.0722d * b;


        if (L > 0.179d)
            tintedColor = "#000000"; // bright colors - black font
        else
            tintedColor = "#FFFFFF"; // dark colors - white font

        if (view instanceof TextView) {
            ((TextView) view).setTextColor(Color.parseColor(tintedColor));
        }
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable != null)
                drawable.setColorFilter(Color.parseColor(tintedColor), PorterDuff.Mode.SRC_IN);
            ((ImageView) view).setImageDrawable(drawable);
        }
    }

    public static void setObjectStyle(View view, View itemView, ObjectStyleModel objectStyle) {
        if (objectStyle.icon() != null) {
            Resources resources = view.getContext().getResources();
            String iconName = objectStyle.icon().startsWith("ic_") ? objectStyle.icon() : "ic_" + objectStyle.icon();
            int icon = resources.getIdentifier(iconName, "drawable", view.getContext().getPackageName());
            if (view instanceof ImageView)
                ((ImageView) view).setImageResource(icon);
        }

        if (objectStyle.color() != null) {
            String color = objectStyle.color().startsWith("#") ? objectStyle.color() : "#" + objectStyle.color();
            int colorRes;
            if (color.length() == 4)
                colorRes = ContextCompat.getColor(view.getContext(), R.color.colorPrimary);
            else
                colorRes = Color.parseColor(color);

            itemView.setBackgroundColor(colorRes);
            setFromResBgColor(view, colorRes);
        }
    }

    @BindingAdapter("imageBackground")
    public static void setImageBackground(ImageView imageView, Drawable drawable) {

        TypedValue typedValue = new TypedValue();
        TypedArray a = imageView.getContext().obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimaryDark});
        TypedArray b = imageView.getContext().obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimaryLight});
        int colorPrimaryDark = a.getColor(0, 0);
        int colorPrimaryLight = b.getColor(0, 0);

        int px = (int) (1 * Resources.getSystem().getDisplayMetrics().density);
        ((GradientDrawable) drawable.mutate()).setStroke(px, colorPrimaryDark);
        //((GradientDrawable) drawable.mutate()).setColor(colorPrimaryLight);

        imageView.setBackground(drawable);

    }

    @BindingAdapter("searchOrAdd")
    public static void setFabIcoin(FloatingActionButton fab, boolean needSearch) {
        Drawable drawable;
        if (needSearch) {
            drawable = AppCompatResources.getDrawable(fab.getContext(), R.drawable.ic_search);
        } else {
            drawable = AppCompatResources.getDrawable(fab.getContext(), R.drawable.ic_add_accent);
        }
        fab.setColorFilter(Color.WHITE);
        fab.setImageDrawable(drawable);
    }

    @BindingAdapter("versionVisibility")
    public static void setVisibility(LinearLayout linearLayout, boolean check){
        if(check && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            linearLayout.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("settingIcon")
    public static void setSettingIcon(ImageView view, int drawableReference) {
        Drawable drawable = AppCompatResources.getDrawable(view.getContext(),drawableReference);
        view.setImageDrawable(drawable);
    }
}