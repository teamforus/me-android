package io.forus.me.android.presentation.view.screens.qr.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.forus.me.android.presentation.R;


public class ConfirmLoginDeviceDialog extends DialogFragment {

    View rootView;



    public SubmitClickListener submitClickListener;

    public static ConfirmLoginDeviceDialog display(FragmentManager fragmentManager,SubmitClickListener submitClickListener) {
        ConfirmLoginDeviceDialog dialog = new ConfirmLoginDeviceDialog();

        dialog.submitClickListener = submitClickListener;

        dialog.show(fragmentManager, "");
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

     boolean  isSmallScreen = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth = outMetrics.widthPixels / density;

        if (dpWidth <= 320 || dpHeight < 522){
            isSmallScreen = true;
        }

        rootView = inflater.inflate(R.layout.dialog_confirm_login_device, container, false);


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

        io.forus.me.android.presentation.view.component.buttons.Button submitButton = rootView.findViewById(R.id.submitButton);

        LinearLayout imageL = rootView.findViewById(R.id.imageL);
        if(isSmallScreen){
            imageL.setVisibility(View.INVISIBLE);
        }

        rootView.findViewById(R.id.closeBt).setOnClickListener(view12 -> {
            if (submitClickListener != null) {
                submitClickListener.dismiss(ConfirmLoginDeviceDialog.this);
            }
        });


        rootView.findViewById(R.id.cancelButton).setOnClickListener(view1 -> {
            if (submitClickListener != null) {
                submitClickListener.dismiss(ConfirmLoginDeviceDialog.this);
            }
        });




        if (submitClickListener != null) {
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (submitClickListener != null) {
                        submitClickListener.confirm(ConfirmLoginDeviceDialog.this);
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
        void confirm(ConfirmLoginDeviceDialog dialog);
        void dismiss(ConfirmLoginDeviceDialog dialog);
    }


}