package com.choucheng.dengdao2.definewidget.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.choucheng.dengdao2.R;

/**
 * Created by Administrator on 2015/2/6.
 */
public class UpdateAPKDialogFragment extends DialogFragment {
    public static UpdateAPKDialogFragment newInstance(String url) {
        UpdateAPKDialogFragment frag = new UpdateAPKDialogFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String url = getArguments().getString("url");


        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_launcher)
                .setTitle(R.string.app_apk_update)
                .setPositiveButton(R.string.dialog_submit,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(url);
                                intent.setData(content_url);
                                startActivity(intent);
                            }
                        }
                )
                .setNegativeButton(R.string.dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                )
                .create();
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失

        return dialog;
    }
}
