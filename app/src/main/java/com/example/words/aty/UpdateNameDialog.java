package com.example.words.aty;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.words.R;

/**
 * Created by 6gold on 2017/9/10.
 */

public class UpdateNameDialog extends Dialog {

    public UpdateNameDialog(Context context) {
        super(context);
    }

    public UpdateNameDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String nickname;
        private String positiveButtonText;
        private String negativeButtonText;

        private View contentView;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setNickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public void setPositiveButton(String positiveButtonText,
                                      DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
        }

        public void setNegativeButton(String negativeButtonText,
                                      DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
        }

        public View getContentView() {
            return contentView;
        }

        public UpdateNameDialog create() {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final UpdateNameDialog dialog = new UpdateNameDialog(context, R.style.Dialog);

            View layout = inflater.inflate(R.layout.dialog_update_name, null);
            contentView = layout;

            dialog.addContentView(layout,new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            // set dialog title
            ((TextView)layout.findViewById(R.id.tv_dialog_title)).setText(title);

            // set nickname
            ((EditText)layout.findViewById(R.id.et_update_name)).setText(nickname);

            // set the confirm button
            if (positiveButtonText != null) {
                ((Button)layout.findViewById(R.id.btn_ok_dun)).setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button)layout.findViewById(R.id.btn_ok_dun)).setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            }
                    );
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.btn_ok_dun).setVisibility(View.GONE);
            }

            // set the cancel button
            if (negativeButtonText != null) {
                ((Button)layout.findViewById(R.id.btn_cancel_dun)).setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button)layout.findViewById(R.id.btn_cancel_dun)).setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            }
                    );
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.btn_cancel_dun).setVisibility(View.GONE);
            }

            return dialog;
        }
    }
}
