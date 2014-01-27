package com.ACM.binarycalculator.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ACM.binarycalculator.R;
import com.ACM.binarycalculator.Activities.CalculatorFloatingPointActivity;
import com.ACM.binarycalculator.Activities.CalculatorPagerActivity;
import com.ACM.binarycalculator.Interfaces.FragmentDataPasser;
import com.actionbarsherlock.app.SherlockFragment;

/**
 * 
 * @author James Van Gaasbeck, ACM at UCF <jjvg@knights.ucf.edu>
 * 
 * 
 */
public class CalculatorFloatingPointFragment extends SherlockFragment {
	// this is a tag used for debugging purposes
	// private static final String TAG = "CalculatorFloatingPointFragment";
	// string constant for saving our workingTextViewText
	private static final String KEY_WORKINGTEXTVIEW_STRING = "workingTextString";
	// the views number in the view pagers, pager adapter
	private static final int VIEW_NUMBER = 1;
	// the radix number (base-number) to be used when parsing the string.
	private static final int VIEWS_RADIX = 10;

	// these are our member variables
	TextView mComputeTextView;
	TextView mWorkingTextView;
	FragmentDataPasser mCallback;
	String mCurrentWorkingText;
	String savedComputeTextView = "";

	// we need to inflate our View so let's grab all the View IDs and inflate
	// them.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// we need to make a view instance from our layout.
		View v = inflater.inflate(R.layout.fragment_calculator_floatingpoint,
				container, false);

		// get the textViews by id, notice we have to reference them via the
		// view instance we just created.
		mComputeTextView = (TextView) v
				.findViewById(R.id.fragment_calculator_floatingpoint_computedTextView);
		mWorkingTextView = (TextView) v
				.findViewById(R.id.fragment_calculator_floatingpoint_workingTextView);
		
		mWorkingTextView.setText("0 00000000 00000000000000000000000");
		mComputeTextView.setTextSize(20);

		// if the we saved something away to handle the activity life cycle,
		// grab it!
		if (savedInstanceState != null) {
			mCurrentWorkingText = "0 00000000 000000000000000000000000";
			// set the text to be what we saved away and just now retrieved.
			mWorkingTextView.setText(mCurrentWorkingText);
		}

		final TableLayout tableLayout = (TableLayout) v
				.findViewById(R.id.fragment_calculator_floatingpoint_tableLayout2);
		
		View.OnClickListener genericOperatorListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mWorkingTextView.setText("0 00000000 00000000000000000000000");
				int i, j;
				for(i=0; i<tableLayout.getChildCount(); i++){
					TableRow row = (TableRow) tableLayout.getChildAt(i);
					for(j=0; j<row.getChildCount(); j++){
						LinearLayout button = (LinearLayout) row.getChildAt(j);
						TextView topTextView = (TextView) button
								.findViewById(R.id.firstTextView);
						if(topTextView.getText().toString() == "1")
							topTextView.setText("0");
					}
				}
				
				Button operator = (Button) v;
				String op = operator.getText().toString();
				String temp = mComputeTextView.getText().toString();
				
				if(op == "x")
					mComputeTextView.setText(temp + " x ");
				else if(op == "/")
					mComputeTextView.setText(temp + " / ");
				else if(op == "+")
					mComputeTextView.setText(temp + " + ");
				else if(op == "-")
					mComputeTextView.setText(temp + " - ");
				
				savedComputeTextView = mComputeTextView.getText().toString();
			
			}
		};
		
		View.OnClickListener bitListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				LinearLayout button = (LinearLayout) v;
				TextView textViewFromButton1 = (TextView) button.findViewById(R.id.firstTextView);
				TextView textViewFromButton2 = (TextView) button.findViewById(R.id.secondTextView);
				String bitNumber = textViewFromButton2.getText().toString();
				int number = Integer.parseInt(bitNumber);
				
				String textFromButton = textViewFromButton1.getText().toString();
				if(textFromButton.compareTo("0") == 0){
					textViewFromButton1.setText("1");
					mCurrentWorkingText = mWorkingTextView.getText().toString();
					
					if(number == 0){
						mWorkingTextView.setText(mCurrentWorkingText.substring(0, 33) + "1");
					}
					else if(number > 0 && number <= 22){
						String newNumber = mCurrentWorkingText.substring(0, 33-number) + "1" + mCurrentWorkingText.substring((33-number)+1,33) 
								+ mCurrentWorkingText.charAt(33);
						mWorkingTextView.setText(newNumber);
					}
					else if(number >= 23 && number <= 30){
						String newNumber = mCurrentWorkingText.substring(0, (33-number)-1) + "1" + mCurrentWorkingText.substring((33-number), 33)
								+ mCurrentWorkingText.charAt(33);
						mWorkingTextView.setText(newNumber);
					}
					else{
						String newNumber = "1" + mCurrentWorkingText.substring(1, 33) + mCurrentWorkingText.charAt(33);
						mWorkingTextView.setText(newNumber);
					}
					
					mCurrentWorkingText = mWorkingTextView.getText().toString();
					String temp = mWorkingTextView.getText().toString();
					//mComputeTextView.setText("");
					mComputeTextView.setText(savedComputeTextView + Double.toString(floatingPointToDecimal(temp)));
				}
				else{
					textViewFromButton1.setText("0"); 
					
					if(number == 0){
						mWorkingTextView.setText(mCurrentWorkingText.substring(0, 33) + "0");
					}
					
					else if(number >= 0 && number <= 22){
						String newNumber = mCurrentWorkingText.substring(0, 33-number) + "0" + mCurrentWorkingText.substring((33-number)+1,33)
								+ mCurrentWorkingText.charAt(33);
						mWorkingTextView.setText(newNumber);
					}
					else if(number >= 23 && number <= 30){
						String newNumber = mCurrentWorkingText.substring(0, (33-number)-1) + "0" + mCurrentWorkingText.substring((33-number), 33)
								+ mCurrentWorkingText.charAt(33);
						mWorkingTextView.setText(newNumber);
					}
					else{
						String newNumber = "0" + mCurrentWorkingText.substring(1, 33) + mCurrentWorkingText.charAt(33);
						mWorkingTextView.setText(newNumber);
					}
					
					mCurrentWorkingText = mWorkingTextView.getText().toString();
					String temp = mWorkingTextView.getText().toString();
					mComputeTextView.setText(savedComputeTextView + Double.toString(floatingPointToDecimal(temp)));
					
				}
				
				
					
			}
		};

		View.OnClickListener backspaceButtonListener = new View.OnClickListener() {
			// remove the last thing to be inputed into the workingTextView,
			// also update the post fix stacks accordingly?
			@Override
			public void onClick(View v) {
				// need to check if the view has anything in it, because if it
				// doesn't the app will crash when trying to change a null
				// string.
				if (mCurrentWorkingText.length() != 0) {
					mCurrentWorkingText = mCurrentWorkingText.substring(0,
							mCurrentWorkingText.length() - 1);
					mWorkingTextView.setText(mCurrentWorkingText);
				}
	//			onPassData(mCurrentWorkingText, true);
			}
		};

		// get a reference to our TableLayout XML
	//	TableLayout tableLayout = (TableLayout) v
	//			.findViewById(R.id.fragment_calculator_floatingpoint_tableLayout2);

		// adds the values and listeners to the buttons and pretty much every
		// button except for a few
		//
		// this for loop could probably be cleaned up, because the views had
		// changed from the original and the for loop had to change as well,
		// making the for loop look like a logical mess.
		int i, j;
		for (i = 0; i < tableLayout.getChildCount(); i++) {
			TableRow row = (TableRow) tableLayout.getChildAt(i);
			for (j = 0; j < row.getChildCount(); j++) {
				LinearLayout button = (LinearLayout) row.getChildAt(j);
				TextView bottomTextView = (TextView) button
						.findViewById(R.id.secondTextView);
				if (i == 0) {
					int num = 31 - j;
					bottomTextView.setText("" + num);
					button.setOnClickListener(bitListener);
				} else {
					int num = 15 - j;
					bottomTextView.setText("" + num);
					button.setOnClickListener(bitListener);
				}

			}
		}

		TableLayout tableLayout2 = (TableLayout) v
				.findViewById(R.id.fragment_calculator_floatingpoint_tableLayout1);
		TableRow row = (TableRow) tableLayout2.getChildAt(0);
		for (i = 0; i < row.getChildCount(); i++) {

			Button butt = (Button) row.getChildAt(i);

			if (i == 0) {
				butt.setText("");
				butt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent myIntent = new Intent(v.getContext(), CalculatorPagerActivity.class);
						startActivity(myIntent);
						return;
					}
				});
			} else if (i == 1) {
				butt.setText("AC");
				butt.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mWorkingTextView.setText("0 00000000 00000000000000000000000");
						int i, j;
						for(i=0; i<tableLayout.getChildCount(); i++){
							TableRow row = (TableRow) tableLayout.getChildAt(i);
							for(j=0; j<row.getChildCount(); j++){
								LinearLayout button = (LinearLayout) row.getChildAt(j);
								TextView topTextView = (TextView) button
										.findViewById(R.id.firstTextView);
								if(topTextView.getText().toString() == "1")
									topTextView.setText("0");
							}
						}
						
						savedComputeTextView = "";
						mComputeTextView.setText("");
						
					}
				});
			} else if (i == 2) {
				butt.setText("2's");
				butt.setOnClickListener(genericOperatorListener);
			} else if (i == 3) {
				butt.setText("1's");
				butt.setOnClickListener(genericOperatorListener);
			} else if (i == 4) {
				butt.setText("+");
				butt.setOnClickListener(genericOperatorListener);
			} else if (i == 5) {
				butt.setText("-");
				butt.setOnClickListener(genericOperatorListener);
			} else if (i == 6) {
				butt.setText("x");
				butt.setOnClickListener(genericOperatorListener);
			} else if (i == 7) {
				butt.setText("/");
				butt.setOnClickListener(genericOperatorListener);
			} else {
				butt.setText("=");
			}

		}

		return v;
	}

	public static Fragment newInstance() {
		CalculatorFloatingPointFragment fpFrag = new CalculatorFloatingPointFragment();
		return fpFrag;
	}

	// method to save the state of the application during the activity life
	// cycle. This is so we can preserve the values in the textViews upon screen
	// rotation.
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Log.i(TAG, "onSaveInstanceState");
		outState.putString(KEY_WORKINGTEXTVIEW_STRING, mCurrentWorkingText);
	}

	// fragment life-cycle method
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// set our dataPasser interface up when the fragment is on the activity
		try {
			// hook the call back up to the activity it is attached to, should
			// do this in a try/catch because the parent activity must implement
			// the interface.
			mCallback = (FragmentDataPasser) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					activity.toString()
							+ " must implement the FragmentDataPasser interface so we can pass data between the fragments.");
		}
	}

	// callback method to send data to the activity so we can then update all
	// the fragments
/*	public void onPassData(String dataToBePassed, boolean cameFromBackspace) {
		mCallback.onDataPassed(dataToBePassed, VIEW_NUMBER, VIEWS_RADIX,
				cameFromBackspace);
	}*/

	// method to receive the data from the activity/other-fragments and update
	// the textViews accordingly
/*	public void updateWorkingTextView(String dataToBePassed, int base) {
		if (dataToBePassed.length() != 0) {
			Long dataInt = Long.parseLong(dataToBePassed, base);
			mCurrentWorkingText = "" + dataInt;
			mWorkingTextView.setText(mCurrentWorkingText);
		} else {
			// if the data is blank set the textView to nothing
			mCurrentWorkingText = "";
			mWorkingTextView.setText(mCurrentWorkingText);
		}
	}*/
	
	public double floatingPointToDecimal(String floatingPointNumber){
		String mantissaString = floatingPointNumber.substring(11, 33) + floatingPointNumber.charAt(33);
		Double mantissa = calculateFractionalBits(mantissaString);
		mantissa += 1;
		String exponentString = floatingPointNumber.substring(2, 10);
		int sign = Integer.parseInt(floatingPointNumber.substring(0, 1));
		int exponent = Integer.parseInt(exponentString, 2);
					
		exponent -= 127;
		
		double answer = (double) Math.pow(2, exponent) * mantissa;
		if(sign == 1)
			answer = answer * -1; 
		
		return answer;
	}
	
	public double calculateFractionalBits(String mantissa){
		char mArray[] = mantissa.toCharArray();
		int length = mantissa.length();
		int i;
		double sum = 0;
		for(i=0; i<length; i++){
			if(mArray[i] == '1')
				sum = sum + (1 / (Math.pow(2, i+1)));
		}
		
		return sum;
	}
}
	

