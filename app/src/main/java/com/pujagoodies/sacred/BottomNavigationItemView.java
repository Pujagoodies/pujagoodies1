package com.pujagoodies.sacred;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.text.TextPaint;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;

//import com.example.mandir.BottomNavigationBar;
import com.pujagoodies.sacred.BottomNavigationBar;
//import com.example.mandir.RippleUtils;
import com.pujagoodies.sacred.RippleUtils;

final class BottomNavigationItemView extends View {
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    private MenuItem menuItem;
    private boolean checked = false;

    private int position;
    private Drawable icon;
    private CharSequence label;
    private int iconSize;
    private ColorStateList iconTint;
    private ColorStateList labelTextColor;
    private int contentSpacing;
    private boolean rippleEnabled;
    private ColorStateList rippleColor;
    private boolean unboundedRipple;
    @com.pujagoodies.sacred.BottomNavigationBar.LabelVisibilityMode private int labelVisibilityMode;

    private final Rect labelBounds = new Rect();
    private int labelBaselineX;
    private int labelBaselineY;

    private TextPaint textPaint;
    private TextPaint activeTextPaint;
    private TextPaint inactiveTextPaint;
    private ColorStateList activeTextColor;
    private ColorStateList inactiveTextColor;

    public BottomNavigationItemView(Context context) {
        super(context);

        activeTextPaint = new TextPaint();
        inactiveTextPaint = new TextPaint();
        textPaint = inactiveTextPaint;

        setFocusable(true);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (menuItem != null && menuItem.isCheckable() && menuItem.isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = right - left;
        int height = bottom - top;
        int iconLeft;
        int iconTop;
        int iconRight;
        int iconBottom;
        int contentHeight;

        switch (labelVisibilityMode) {
            case com.pujagoodies.sacred.BottomNavigationBar.LABEL_VISIBILITY_ALWAYS:
                textPaint.getTextBounds(label.toString(), 0, label.length(), labelBounds);
                contentHeight = icon.getIntrinsicHeight() + labelBounds.height() + contentSpacing;
                iconLeft = (width - icon.getIntrinsicWidth()) / 2;
                iconTop = (height - contentHeight) / 2;
                iconRight = iconLeft + icon.getIntrinsicWidth();
                iconBottom = iconTop + icon.getIntrinsicHeight();

                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                labelBaselineX = getWidth() / 2;
                labelBaselineY = iconBottom + contentSpacing - labelBounds.top;
                break;
            case com.pujagoodies.sacred.BottomNavigationBar.LABEL_VISIBILITY_NEVER:
                iconLeft = (width - icon.getIntrinsicWidth()) / 2;
                iconTop = (height - icon.getIntrinsicHeight()) / 2;
                iconRight = iconLeft + icon.getIntrinsicWidth();
                iconBottom = iconTop + icon.getIntrinsicHeight();

                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (labelVisibilityMode) {
            case com.pujagoodies.sacred.BottomNavigationBar.LABEL_VISIBILITY_ALWAYS:
                icon.draw(canvas);
                canvas.drawText(label.toString(), labelBaselineX, labelBaselineY, textPaint);
                break;
            case com.pujagoodies.sacred.BottomNavigationBar.LABEL_VISIBILITY_NEVER:
                icon.draw(canvas);
                break;
        }
    }

    void updateView(MenuItem menuItem) {
        this.menuItem = menuItem;

        checked = menuItem.isChecked();
        if (labelTextColor == null) {
            labelTextColor = checked ? activeTextColor : inactiveTextColor;
        }

        refreshDrawableState();
        setSelected(menuItem.isChecked());

        setEnabled(menuItem.isEnabled());
        setIcon(menuItem.getIcon());
        setLabel(menuItem.getTitle());
        setId(menuItem.getItemId());

        setVisibility(menuItem.isVisible() ? View.VISIBLE : View.GONE);

        updateTextPaint();
        updateTextColor();
        icon.setState(getDrawableState());
        requestLayout();
        invalidate();
    }

    private void updateTextPaint() {
        textPaint = checked ? activeTextPaint : inactiveTextPaint;
    }

    private void updateTextColor() {
        int newColor = labelTextColor.getColorForState(getDrawableState(), 0);
        if (textPaint != null && newColor != textPaint.getColor()) {
            textPaint.setColor(newColor);
            invalidate();
        }
    }

    public int getPosition() {
        return position;
    }

    void setPosition(int position) {
        this.position = position;
    }

    void setIcon(Drawable newIcon) {
        if (icon == newIcon) {
            return;
        }

        icon = newIcon;
        if (newIcon != null) {
            Drawable.ConstantState state = newIcon.getConstantState();
            icon = DrawableCompat.wrap(state == null ? newIcon : state.newDrawable().mutate());
            if (iconTint != null) {
                DrawableCompat.setTintList(icon, iconTint);
            }
        }

        requestLayout();
    }

    void setLabel(CharSequence label) {
        if (this.label != null && this.label.equals(label)) {
            return;
        }
        this.label = label;
        invalidate();
    }

    void setIconSize(@Dimension int iconSize) {
        if (this.iconSize == iconSize) {
            return;
        }
        this.iconSize = iconSize;
        requestLayout();
    }

    void setIconTint(ColorStateList iconTint) {
        this.iconTint = iconTint;
        if (icon != null) {
            DrawableCompat.setTintList(icon, iconTint);
        }
        invalidate();
    }

    void setLabelTextAppearanceInactive(@StyleRes int textAppearance) {
        TypedArray appearance = getContext().getTheme().obtainStyledAttributes(textAppearance,
                androidx.appcompat.R.styleable.TextAppearance);
        inactiveTextColor = appearance.getColorStateList(
                androidx.appcompat.R.styleable.TextAppearance_android_textColor);
        appearance.recycle();

        AppCompatTextView utilTextView = new AppCompatTextView(getContext());
        utilTextView.setTextAppearance(getContext(), textAppearance);
        inactiveTextPaint = utilTextView.getPaint();
        inactiveTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    void setLabelTextAppearanceActive(@StyleRes int textAppearance) {
        TypedArray appearance = getContext().getTheme().obtainStyledAttributes(textAppearance,
                androidx.appcompat.R.styleable.TextAppearance);
        activeTextColor = appearance.getColorStateList(
                androidx.appcompat.R.styleable.TextAppearance_android_textColor);
        appearance.recycle();

        AppCompatTextView utilTextView = new AppCompatTextView(getContext());
        utilTextView.setTextAppearance(getContext(), textAppearance);
        activeTextPaint = utilTextView.getPaint();
        activeTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    void setLabelTextColor(ColorStateList color) {
        if (color != null) {
            this.labelTextColor = color;
            updateTextColor();
        }
    }

    void setContentSpacing(@Dimension int spacing) {
        if (contentSpacing == spacing) {
            return;
        }
        this.contentSpacing = spacing;
        if (labelVisibilityMode == com.pujagoodies.sacred.BottomNavigationBar.LABEL_VISIBILITY_ALWAYS) {
            requestLayout();
        }
    }

    void setViewBackground(@DrawableRes int backgroundRes) {
        Drawable backgroundDrawable =
                backgroundRes == 0 ? null : ContextCompat.getDrawable(getContext(), backgroundRes);
        setViewBackground(backgroundDrawable);
    }

    void setViewBackground(Drawable background) {
        if (background != null && background.getConstantState() != null) {
            background = background.getConstantState().newDrawable().mutate();
        }
        ViewCompat.setBackground(this, background);
    }

    void setRippleBackground(ColorStateList rippleColor) {
        if (this.rippleColor == rippleColor) {
            if (rippleColor == null && getBackground() != null) {
                setViewBackground(null);
            }
            return;
        }
        this.rippleColor = rippleColor;
        if (rippleEnabled) {
            ColorStateList rippleDrawableColor =
                    RippleUtils.convertToRippleDrawableColor(rippleColor);

            GradientDrawable contentDrawable = new GradientDrawable();
            contentDrawable.setColor(Color.TRANSPARENT);

            GradientDrawable maskDrawable = new GradientDrawable();
            maskDrawable.setCornerRadius(0.00001F);
            maskDrawable.setColor(Color.WHITE);

            Drawable background = new RippleDrawable(rippleDrawableColor,
                    unboundedRipple ? null : contentDrawable, unboundedRipple ? null : maskDrawable);
            setViewBackground(background);
        } else {
            setViewBackground(null);
        }
    }

    void setRippleEnabled(boolean enabled) {
        this.rippleEnabled = enabled;
        setRippleColor(rippleColor);
    }

    void setRippleColor(ColorStateList rippleColor) {
        if (this.rippleColor == rippleColor) {
            if (rippleColor == null && getBackground() != null) {
                setViewBackground(null);
            }
            return;
        }
        this.rippleColor = rippleColor;
        if (rippleEnabled) {
            ColorStateList rippleDrawableColor =
                    RippleUtils.convertToRippleDrawableColor(rippleColor);

            GradientDrawable contentDrawable = new GradientDrawable();
            contentDrawable.setColor(Color.TRANSPARENT);

            GradientDrawable maskDrawable = new GradientDrawable();
            maskDrawable.setCornerRadius(0.00001F);
            maskDrawable.setColor(Color.WHITE);

            Drawable background = new RippleDrawable(rippleDrawableColor,
                    unboundedRipple ? null : contentDrawable, unboundedRipple ? null : maskDrawable);
            setViewBackground(background);
        } else {
            setViewBackground(null);
        }
    }

    void setUnboundedRipple(boolean unbounded) {
        unboundedRipple = unbounded;
        setRippleColor(rippleColor);
    }

    void setLabelVisibilityMode(@BottomNavigationBar.LabelVisibilityMode int mode) {
        if (this.labelVisibilityMode == mode) {
            return;
        }
        this.labelVisibilityMode = mode;
        requestLayout();
    }
}
