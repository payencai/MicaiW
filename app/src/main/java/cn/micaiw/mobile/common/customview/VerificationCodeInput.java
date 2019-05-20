package cn.micaiw.mobile.common.customview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.micaiw.mobile.R;

/**
 * 作者：凌涛 on 2018/5/10 10:44
 * 邮箱：771548229@qq..com
 * 輸入验证码的自定义view
 */

public class VerificationCodeInput extends LinearLayout implements TextWatcher, View.OnKeyListener {


    private final static String TYPE_NUMBER = "number";
    private final static String TYPE_TEXT = "text";
    private final static String TYPE_PASSWORD = "password";
    private final static String TYPE_PHONE = "phone";

    private static final String TAG = "VerificationCodeInput";
    private int box = 4;
    private int boxWidth = 80;
    private int boxHeight = 80;
    private int childHPadding = 14;
    private int childVPadding = 14;
    private String inputType = TYPE_PASSWORD;
    private Drawable boxBgFocus = null;
    private Drawable boxBgNormal = null;
    private Listener listener;
    private boolean focus = false;
    private List<EditText> mEditTextList = new ArrayList<>();
    private int currentPosition = 0;
    private StringBuffer mContent = new StringBuffer();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public VerificationCodeInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeInput);
        box = a.getInt(R.styleable.VerificationCodeInput_box, 4);

        childHPadding = (int) a.getDimension(R.styleable.VerificationCodeInput_child_h_padding, 0);
        childVPadding = (int) a.getDimension(R.styleable.VerificationCodeInput_child_v_padding, 0);
        boxBgFocus = a.getDrawable(R.styleable.VerificationCodeInput_box_bg_focus);
        boxBgNormal = a.getDrawable(R.styleable.VerificationCodeInput_box_bg_normal);
        inputType = a.getString(R.styleable.VerificationCodeInput_inputType);
        boxWidth = (int) a.getDimension(R.styleable.VerificationCodeInput_child_width, boxWidth);
        boxHeight = (int) a.getDimension(R.styleable.VerificationCodeInput_child_height, boxHeight);
        a.recycle();
        initViews(context);

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initViews(Context context) {
        for (int i = 0; i < box; i++) {
            EditText editText = new EditText(getContext());
            WindowManager m = ((Activity) context).getWindowManager();
            Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
            int width = d.getWidth();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width / 10, width / 10);
//            layoutParams.bottomMargin = childVPadding;
//            layoutParams.topMargin = childVPadding;
//            layoutParams.leftMargin = childHPadding;
//            layoutParams.rightMargin = childHPadding;
            layoutParams.gravity = Gravity.CENTER;


            editText.setOnKeyListener(this);
            if (i == 0) {
                setBg(editText, true);
            } else setBg(editText, false);
            editText.setTextColor(Color.BLACK);
            editText.setLayoutParams(layoutParams);
            editText.setGravity(Gravity.CENTER);
            editText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

//            if (TYPE_NUMBER.equals(inputType)) {
//                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
//            } else if (TYPE_PASSWORD.equals(inputType)){
//                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
//            } else if (TYPE_TEXT.equals(inputType)){
//                editText.setInputType(InputType.TYPE_CLASS_TEXT);
//            } else if (TYPE_PHONE.equals(inputType)){
//                editText.setInputType(InputType.TYPE_CLASS_PHONE);
//
//            }
            editText.setId(i);
            editText.setEms(1);
            editText.addTextChangedListener(this);
            addView(editText, i);
            mEditTextList.add(editText);

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void backFocus() {
        int count = getChildCount();
        EditText editText;
        for (int i = count - 1; i >= 0; i--) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() == 1) {
                editText.requestFocus();
                setBg(mEditTextList.get(i), true);
                //setBg(mEditTextList.get(i-1),true);
                editText.setSelection(1);
                return;
            }
        }
    }

    private void focus() {
        int count = getChildCount();
        EditText editText;
        for (int i = 0; i < count; i++) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() < 1) {
                editText.requestFocus();
                return;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setBg(EditText editText, boolean focus) {
        if (boxBgNormal != null && !focus) {
            editText.setBackground(boxBgNormal);
        } else if (boxBgFocus != null && focus) {
            editText.setBackground(boxBgFocus);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setBg() {
        int count = getChildCount();
        EditText editText;
        for (int i = 0; i < count; i++) {
            editText = (EditText) getChildAt(i);
            if (boxBgNormal != null && !focus) {
                editText.setBackground(boxBgNormal);
            } else if (boxBgFocus != null && focus) {
                editText.setBackground(boxBgFocus);
            }
        }

    }

    private void checkAndCommit() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean full = true;
        for (int i = 0; i < box; i++) {
            EditText editText = (EditText) getChildAt(i);
            String content = editText.getEditableText().toString();
            if (content.length() == 0) {
                full = false;
                break;
            } else {
                stringBuilder.append(content);
            }

        }
//        if (full) {
//
//            if (listener != null) {
//                listener.onComplete(stringBuilder.toString());
//                setEnabled(true);
//            }
//
//        }

//        if (listener != null) {
//            listener.onComplete(stringBuilder.toString());
//            setEnabled(true);
//        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    public void setOnCompleteListener(Listener listener) {
        this.listener = listener;
    }

    @Override

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LinearLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        if (count > 0) {
            View child = getChildAt(0);
            int cHeight = child.getMeasuredHeight();
            int cWidth = child.getMeasuredWidth();
            int maxH = cHeight + 2 * childVPadding;
//            int maxH = (cHeight + 2 * childVPadding) + 8;
            int maxW = (cWidth + childHPadding) * box + childHPadding;
            setMeasuredDimension(resolveSize(maxW, widthMeasureSpec),
                    resolveSize(maxH, heightMeasureSpec));
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            child.setVisibility(View.VISIBLE);
            int cWidth = child.getMeasuredWidth();
            int cHeight = child.getMeasuredHeight();
            int cl = (i) * (cWidth + childHPadding);
            int cr = cl + cWidth;
            int ct = childVPadding;
            int cb = ct + cHeight;
            child.layout(cl, ct, cr, cb);
        }


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (start == 0 && count >= 1 && currentPosition != mEditTextList.size() - 1) {
            currentPosition++;
            mEditTextList.get(currentPosition).requestFocus();
            setBg(mEditTextList.get(currentPosition), true);
            setBg(mEditTextList.get(currentPosition - 1), false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        if (TextUtils.isEmpty(text)) {
            mContent.deleteCharAt(mContent.length() - 1);
        } else {
            mContent.append(text);
        }
        String result = mContent.toString();
        listener.onComplete(result);
        if (s.length() == 0) {
        } else {
            focus();
            checkAndCommit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        EditText editText = (EditText) view;
        if (keyCode == KeyEvent.KEYCODE_DEL && editText.getText().length() == 0) {
            int action = event.getAction();
            if (currentPosition != 0 && action == KeyEvent.ACTION_DOWN) {
                currentPosition--;
                mEditTextList.get(currentPosition).requestFocus();
                setBg(mEditTextList.get(currentPosition), true);
                setBg(mEditTextList.get(currentPosition + 1), false);
                mEditTextList.get(currentPosition).setText("");
            }
        }
        return false;
    }

    public interface Listener {
        void onComplete(String content);
    }


}
