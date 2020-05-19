package io.forus.me.android.presentation.view.screens.vouchers.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import io.forus.me.android.presentation.R;


public class FullscreenDialog extends DialogFragment {

    public static final String TAG = "example_dialog";

    View rootView;

    private String title = "";

    private String description = "";

    SubmitClickListener submitClickListener;

    public static FullscreenDialog display(FragmentManager fragmentManager, String title, String details, SubmitClickListener submitClickListener) {
        FullscreenDialog fullscreenDialog = new FullscreenDialog();
        fullscreenDialog.title = title;
        fullscreenDialog.description = details;
        fullscreenDialog.submitClickListener = submitClickListener;

        fullscreenDialog.show(fragmentManager, "");
        return fullscreenDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.dialog_fullscreen, container, false);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView titleTv = rootView.findViewById(R.id.title);
        TextView descriptionTv = rootView.findViewById(R.id.description);
        io.forus.me.android.presentation.view.component.buttons.Button submitButton = rootView.findViewById(R.id.submitButton);

        titleTv.setText(title);
        descriptionTv.setText(description);

        rootView.findViewById(R.id.closeBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenDialog.this.dismiss();
            }
        });

        if (submitClickListener != null) {
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(submitClickListener != null){
                        submitClickListener.click(FullscreenDialog.this);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public interface SubmitClickListener {
        void click(FullscreenDialog dialog);
    }


}