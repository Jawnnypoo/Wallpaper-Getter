package com.jawnnypoo.wallpapergetter.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.jawnnypoo.wallpapergetter.R;

public class PhotoDialog extends DialogFragment {

    private OnPhotoDialogListener mListener;

    public interface OnPhotoDialogListener {
        public void onAddToFavorites();
        public void onDownload();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.photo))
                .setPositiveButton(getString(R.string.add_to_favorites), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null) {
                            mListener.onAddToFavorites();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.download_image), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null) {
                            mListener.onDownload();
                        }
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setPhotoListener(OnPhotoDialogListener listener) {
        mListener = listener;
    }
}