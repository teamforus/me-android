package io.forus.me.android.presentation.view.screens.vouchers.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import io.forus.me.android.presentation.R;


public class FullscreenDialog extends DialogFragment {

    View rootView;

    private String title = "";
    private String description = "";
    String positiveButtonText= "OK";

    SubmitClickListener submitClickListener;

    public static FullscreenDialog display(FragmentManager fragmentManager, String title, String details, String positiveButtonText, SubmitClickListener submitClickListener) {
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
            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(width, height);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView titleTv = rootView.findViewById(R.id.titleTV);
        TextView descriptionTv = rootView.findViewById(R.id.description);
        io.forus.me.android.presentation.view.component.buttons.Button submitButton = rootView.findViewById(R.id.submitButton);

        titleTv.setText(title);
        descriptionTv.setText(description);

        rootView.findViewById(R.id.closeBt).setOnClickListener(view12 -> FullscreenDialog.this.dismiss());

        submitButton.setText(positiveButtonText);

        if (submitClickListener != null) {
            submitButton.setOnClickListener(view1 -> {
                if (submitClickListener != null) {
                    FullscreenDialog.this.dismiss();
                    submitClickListener.click(FullscreenDialog.this);
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