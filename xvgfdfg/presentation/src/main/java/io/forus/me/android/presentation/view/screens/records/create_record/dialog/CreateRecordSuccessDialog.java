package io.forus.me.android.presentation.view.screens.records.create_record.dialog;

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


public class CreateRecordSuccessDialog extends DialogFragment {

    View rootView;

    private String recordType = "";
    private String recordName = "";
    String positiveButtonText= "OK";

    SubmitClickListener submitClickListener;



    public static CreateRecordSuccessDialog display(FragmentManager fragmentManager, String recordType, String recordNameTV, SubmitClickListener submitClickListener) {
        CreateRecordSuccessDialog fullscreenDialog = new CreateRecordSuccessDialog();
        fullscreenDialog.recordType = recordType;
        fullscreenDialog.recordName = recordNameTV;
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

        rootView = inflater.inflate(R.layout.dialog_create_record_success, container, false);


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

        TextView recordTypeTV = rootView.findViewById(R.id.recordTypeTV);
        TextView recordNameTV = rootView.findViewById(R.id.recordNameTV);
        io.forus.me.android.presentation.view.component.buttons.Button submitButton = rootView.findViewById(R.id.submitButton);

        recordTypeTV.setText(recordType);
        recordNameTV.setText(recordName);

        rootView.findViewById(R.id.closeBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateRecordSuccessDialog.this.dismiss();
            }
        });

        submitButton.setText(positiveButtonText);

        if (submitClickListener != null) {
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (submitClickListener != null) {
                        CreateRecordSuccessDialog.this.dismiss();
                        submitClickListener.click(CreateRecordSuccessDialog.this);
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
        void click(CreateRecordSuccessDialog dialog);
    }


}