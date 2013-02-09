package com.quizz.core.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import com.quizz.core.R;

public class ConfirmQuitDialog extends AlertDialog {

    private Closeable mClosable;

    public ConfirmQuitDialog(Context context) {
	super(context);

	setTitle(R.string.quit_label);
	setMessage(getContext().getText(R.string.sure_to_quit));
	setButton(BUTTON_POSITIVE, getContext().getText(R.string.yes),
		mPositiveButtonClick);
	setButton(BUTTON_NEGATIVE, getContext().getText(R.string.not_yet),
		mNegativeButtonClick);
    }

    public ConfirmQuitDialog(Context context, View customView) {
	super(context, android.R.style.Theme_Translucent_NoTitleBar);

	setView(customView);
	Button positiveButton = (Button) customView
		.findViewById(R.id.dialog_positive_button);
	if (positiveButton != null) {
	    positiveButton.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
		    closeActivity();
		}
	    });
	}

	Button negativeButton = (Button) customView
		.findViewById(R.id.dialog_negative_button);
	if (negativeButton != null) {
	    negativeButton.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
		    closeDialog();
		}
	    });
	}
    }

    public void setClosable(Closeable closable) {
	mClosable = closable;
    }

    private void closeDialog() {
	cancel();
    }

    private void closeActivity() {
	if (mClosable != null) {
	    mClosable.close();
	}
    }

    // ===========================================================
    // Listeners
    // ===========================================================

    OnClickListener mPositiveButtonClick = new OnClickListener() {

	@Override
	public void onClick(DialogInterface dialog, int which) {
	    closeActivity();
	}
    };

    OnClickListener mNegativeButtonClick = new OnClickListener() {

	@Override
	public void onClick(DialogInterface dialog, int which) {
	    closeDialog();
	}
    };

    // ===========================================================
    // Inner classes
    // ===========================================================

    public interface Closeable {
	public void close();
    }
}
