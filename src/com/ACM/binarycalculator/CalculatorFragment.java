package com.ACM.binarycalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CalculatorFragment extends Fragment {

	TextView mComputeTextView;
	TextView mWorkingTextView;

	// we need to inflate our View so let's grab all the View IDs and inflate
	// them.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_calculator_decimal,
				container, false);

		mComputeTextView = (TextView) v
				.findViewById(R.id.fragment_calculator_decimal_computedTextView);
		mWorkingTextView = (TextView) v
				.findViewById(R.id.fragment_calculator_decimal_workingTextView);

		View.OnClickListener genericButtonListener = new View.OnClickListener() {
			// when someone clicks a button that isn't "special" we are going to
			// add it to the workingTextView
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				String workingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();
				// see if the workingTextView is empty aka equal to zero.
				if (workingText.equals("0")) {
					mWorkingTextView.setText(textFromButton);
				} else {
					// if the working TextView isn't zero we need to append the
					// textFromButton to what is already there.
					mWorkingTextView.setText(workingText + textFromButton);
				}
			}
		};

		View.OnClickListener backspaceButtonListener = new View.OnClickListener() {
			// remove the last thing to be inputed into the workingTextView,
			// also update the post fix stacks accordingly?
			@Override
			public void onClick(View v) {
				if (mWorkingTextView.getText().toString().length() != 0) {
					String removingLastChar = mWorkingTextView.getText()
							.toString();
					removingLastChar = removingLastChar.substring(0,
							removingLastChar.length() - 1);
					mWorkingTextView.setText(removingLastChar);
				}
			}
		};

		// get a reference to our TableLayout XML
		TableLayout tableLayout = (TableLayout) v
				.findViewById(R.id.fragment_calculator_decimal_tableLayout);

		// adds the values and listeners to the buttons and pretty much every
		// button except for a few
		int numberForTheButton = 1;
		for (int i = tableLayout.getChildCount() - 2; i >= 0; i--) {
			// get the button row
			TableRow row = (TableRow) tableLayout.getChildAt(i);
			for (int j = 0; j < row.getChildCount(); j++) {
				// get the button from the button row
				Button butt = (Button) row.getChildAt(j);
				if (i == 0 && j == 0) {
					butt.setText("(");
					butt.setOnClickListener(genericButtonListener);
				} else if (i == 0 && j == 1) {
					butt.setText(")");
					butt.setOnClickListener(genericButtonListener);
				} else if (j < row.getChildCount() - 1 && i > 0) {
					butt.setText("" + numberForTheButton++);
					butt.setOnClickListener(genericButtonListener);

				} else {
					if (i == tableLayout.getChildCount() - 2) {
						butt.setText("-");
						butt.setOnClickListener(genericButtonListener);
					} else if (i == tableLayout.getChildCount() - 3) {
						butt.setText("x");
						butt.setOnClickListener(genericButtonListener);
					} else if (i == tableLayout.getChildCount() - 4) {
						butt.setText("/");
						butt.setOnClickListener(genericButtonListener);
					} else if (i == tableLayout.getChildCount() - 5) {
						butt.setText("<-");
						butt.setOnClickListener(backspaceButtonListener);
					}
				}
			}
		}

		TableRow firstRow = (TableRow) tableLayout.getChildAt(0);
		Button clearAllButton = (Button) firstRow.getChildAt(2);
		clearAllButton.setText("Clear All");
		clearAllButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// clear all the text in the working textView, AND maybe the
				// computed textView as well?
				// Also, might want to clear out the post fix expression stack
				mWorkingTextView.setText("");
				mComputeTextView.setText("");
			}
		});

		// now we need to get the last row or buttons and and them to the
		// screen.
		TableRow lastRow = (TableRow) tableLayout.getChildAt(tableLayout
				.getChildCount() - 1);

		// set the zero button
		Button zeroButton = (Button) lastRow.getChildAt(2);
		zeroButton.setText(".");
		zeroButton.setOnClickListener(genericButtonListener);

		// set the decimal button
		Button decimalPointButton = (Button) lastRow.getChildAt(1);
		decimalPointButton.setText("0");
		decimalPointButton.setOnClickListener(genericButtonListener);

		// set the plus button
		Button plusButton = (Button) lastRow.getChildAt(3);
		plusButton.setText("+");
		plusButton.setOnClickListener(genericButtonListener);

		// set the equals button, it will have it's own separate listener to
		// computer the inputed value
		Button equalsButton = (Button) lastRow.getChildAt(0);
		equalsButton.setText("=");
		equalsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO The arithmetic for the inputed numbers. Post fix?

			}
		});

		return v;
	}

}