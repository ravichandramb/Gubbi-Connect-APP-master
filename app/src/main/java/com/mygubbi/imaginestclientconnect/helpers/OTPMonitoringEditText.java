package com.mygubbi.imaginestclientconnect.helpers;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class OTPMonitoringEditText extends AppCompatEditText {

    private final Context context;
    private OTPMonitorListener otpMonitorListener;

    /*
        Just the constructors to create a new EditText...
     */
    public OTPMonitoringEditText(Context context) {
        super(context);
        this.context = context;
    }

    public OTPMonitoringEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public OTPMonitoringEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    /**
     * <p>This is where the "magic" happens.</p>
     * <p>The menu used to cut/copy/paste is a normal ContextMenu, which allows us to
     * overwrite the consuming method and react on the different events.</p>
     *
     * @see <a href="http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3_r1/android/widget/TextView.java#TextView.onTextContextMenuItem%28int%29">Original Implementation</a>
     */
    @Override
    public boolean onTextContextMenuItem(int id) {
        // Do your thing:
        boolean consumed = super.onTextContextMenuItem(id);
        // React:
        switch (id) {
            case android.R.id.cut:
                // ignored
                break;
            case android.R.id.paste:
                if (otpMonitorListener != null) {
                    otpMonitorListener.onTextPaste();
                }
                break;
            case android.R.id.copy:
                // ignored
        }
        return consumed;
    }

    public OTPMonitorListener getOtpMonitorListener() {
        return otpMonitorListener;
    }

    public void setOtpMonitorListener(OTPMonitorListener otpMonitorListener) {
        this.otpMonitorListener = otpMonitorListener;
    }

    public interface OTPMonitorListener {

        void onTextPaste();
    }
}