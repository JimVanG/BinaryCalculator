package com.ACM.binarycalculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * 
 * @author James Van Gaasbeck, ACM at UCF <jjvg@knights.ucf.edu>
 * 
 * 
 */
public class CalculatorBinaryFragment extends SherlockFragment {
	// this is a tag used for debugging purposes
	// private static final String TAG = "CalculatorBinaryFragment";

	// string constant for saving our workingTextViewText
	private static final String KEY_WORKINGTEXTVIEW_STRING = "workingTextString";
	private static final int VIEW_NUMBER = 0;
	// the radix number (base-number) to be used when parsing the string.
	private static final int VIEWS_RADIX = 2;

	// these are our member variables
	TextView mComputeTextView;
	TextView mWorkingTextView;
	private String mCurrentWorkingText;
	FragmentDataPasser mCallback;
	String mDataFromActivity;
	public static int numberOfOpenParenthesis;
	public static int numberOfClosedParenthesis;

	@Override
	// we need to inflate our View so let's grab all the View IDs and inflate
	// them.
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// we need to make a view instance from our layout.
		View v = inflater.inflate(R.layout.fragment_calculator_binary,
				container, false);

		// get the textViews by id, notice we have to reference them via the
		// view instance we just created.
		mComputeTextView = (TextView) v
				.findViewById(R.id.fragment_calculator_binary_computedTextView);
		mWorkingTextView = (TextView) v
				.findViewById(R.id.fragment_calculator_binary_workingTextView);

		// if the we saved something away, grab it!
		if (savedInstanceState != null) {
			mCurrentWorkingText = savedInstanceState
					.getString(KEY_WORKINGTEXTVIEW_STRING);
			// We need to check that we aren't accessing null data or else it
			// will crash upon turning the screen.
			if (mCurrentWorkingText == null) {
				mCurrentWorkingText = new String("");
			}
			// set the text to be what we saved away and just now retrieved.
			mWorkingTextView.setText(mCurrentWorkingText);
		}

		View.OnClickListener genericNumberButtonListener = new View.OnClickListener() {
			// when someone clicks a button that isn't "special" we are going to
			// add it to the workingTextView
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();

				if (mCurrentWorkingText.length() == 0) {
					mWorkingTextView.setText(textFromButton);
					mCurrentWorkingText = textFromButton;
				} else {
					StringTokenizer toke = new StringTokenizer(
							mCurrentWorkingText.concat(textFromButton),
							"-+/x)( ");
					String numberLengthTest = null;
					while (toke.hasMoreTokens()) {
						numberLengthTest = (String) toke.nextToken();
					}
					// we don't want to accept a number that's longer than 32
					if (numberLengthTest.length() > 32) {
						return;
					}
					// if the working TextView isn't zero we need to append
					// the
					// textFromButton to what is already there.
					mWorkingTextView.setText(mCurrentWorkingText
							+ textFromButton);
					mCurrentWorkingText = mWorkingTextView.getText().toString();
				}
				onPassData(mCurrentWorkingText);
			}
		};

		View.OnClickListener genericOperatorButtonListener = new View.OnClickListener() {
			// when someone clicks an operator "/x+" NOT "-", "-" is special so
			// it gets it's own listener. We can't have expressions with
			// adjacent operators "/+x" nor can we start with them.
			// We also cannot have a "." followed by an operator "+/x"
			// Nor can we have a "-" followed by an operator.
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();
				// see if the workingTextView is empty, if so DON'T add the
				// operator
				if (mCurrentWorkingText.length() == 0) {
					// do NOTHING because we can't start an expression with
					// "+/x" but we can with "-" which is why we are going to
					// give the minus/negative sign it's own listener.
				} else {
					// we can't have adjacent "+/x" nor can we have a "."
					// followed by "+/x"
					if (mCurrentWorkingText.endsWith("+ ")
							|| mCurrentWorkingText.endsWith("x ")
							|| mCurrentWorkingText.endsWith("/ ")
							|| mCurrentWorkingText.endsWith(".")
							|| mCurrentWorkingText.endsWith("- ")
							|| mCurrentWorkingText.endsWith("-")
							|| mCurrentWorkingText.endsWith("(")) {
						// do nothing because we can't have multiple adjacent
						// operators

					} else {
						// add it on up!
						mWorkingTextView.setText(mCurrentWorkingText + " "
								+ textFromButton + " ");
						mCurrentWorkingText = mWorkingTextView.getText()
								.toString();
					}
				}
				onPassData(mCurrentWorkingText);
			}
		};

		View.OnClickListener genericMinusButtonListener = new View.OnClickListener() {
			// we can't have more than 2 adjacent "-"
			// we also can't have something like this ".-3"
			// No cases like this "--3" BUT we can have "5--3"
			// No cases like this "(--3)
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();
				// see if the workingTextView is empty
				if (mCurrentWorkingText.length() == 0) {
					mWorkingTextView.setText(textFromButton);
					mCurrentWorkingText = textFromButton;
				} else if (mCurrentWorkingText.length() == 1
						&& mCurrentWorkingText.endsWith("-")) {
					// do nothing so we don't start out with something like this
					// "--2"
				} else {
					// we can't have more than 2 adjacent '-'. So get the last
					// two char's and check if it's "--"
					if (mCurrentWorkingText.endsWith(".")
							|| mCurrentWorkingText.endsWith("--")
							|| mCurrentWorkingText.endsWith("(-")) {
						// do nothing because we can't have more than 2
						// adjacent minus's
					} else {
						// otherwise, add it to the view
						if (mCurrentWorkingText.endsWith("1")
								|| mCurrentWorkingText.endsWith("0")) {
							mWorkingTextView.setText(mCurrentWorkingText + " "
									+ textFromButton + " ");
							mCurrentWorkingText = mWorkingTextView.getText()
									.toString();
						} else {
							mWorkingTextView.setText(mCurrentWorkingText
									+ textFromButton);
							mCurrentWorkingText = mWorkingTextView.getText()
									.toString();
						}
					}
				}
				// need to pass data to our call back so all fragments can be
				// updated with the new workingTextView
				onPassData(mCurrentWorkingText);
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
				if (mCurrentWorkingText != null) {
					if (mCurrentWorkingText.length() != 0) {

						if (mCurrentWorkingText.endsWith(")")) {
							CalculatorDecimalFragment.numberOfClosedParenthesis--;
							CalculatorBinaryFragment.numberOfClosedParenthesis--;
							CalculatorHexFragment.numberOfClosedParenthesis--;
							CalculatorOctalFragment.numberOfClosedParenthesis--;
						} else if (mCurrentWorkingText.endsWith("(")) {
							CalculatorDecimalFragment.numberOfOpenParenthesis--;
							CalculatorBinaryFragment.numberOfOpenParenthesis--;
							CalculatorHexFragment.numberOfOpenParenthesis--;
							CalculatorOctalFragment.numberOfOpenParenthesis--;
						}
						// we need to delete the spaces around the operators
						// also, not just the last char added to the
						// workingTextView
						if (mCurrentWorkingText.endsWith(" + ")
								|| mCurrentWorkingText.endsWith(" - ")
								|| mCurrentWorkingText.endsWith(" x ")
								|| mCurrentWorkingText.endsWith(" / ")) {

							mCurrentWorkingText = mCurrentWorkingText
									.substring(0,
											mCurrentWorkingText.length() - 3);
							mWorkingTextView.setText(mCurrentWorkingText);
						} else {

							// if it's not an operator with spaces around it,
							// just delete the last char
							mCurrentWorkingText = mCurrentWorkingText
									.substring(0,
											mCurrentWorkingText.length() - 1);
							mWorkingTextView.setText(mCurrentWorkingText);
						}
					}
					onPassData(mCurrentWorkingText);
				}
			}
		};

		View.OnClickListener floatingPointListener = new View.OnClickListener() {
			// We want to start a new activity with the floating point view
			// inside of it.
			@Override
			public void onClick(View v) {
				Intent startFloatingPoint = new Intent(getSherlockActivity(),
						CalculatorFloatingPointActivity.class);
				startActivity(startFloatingPoint);
				getSherlockActivity().getSupportFragmentManager()
						.beginTransaction().addToBackStack(null).commit();
			}
		};

		View.OnClickListener twosComplementButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Twos's complement
			}
		};

		// get a reference to our TableLayout XML
		TableLayout tableLayout = (TableLayout) v
				.findViewById(R.id.fragment_calculator_binary_tableLayout);

		// adds the values and listeners to the buttons and pretty much every
		// button except for a few
		//
		// this for loop could probably be cleaned up, because the views had
		// changed from the original and the for loop had to change as well,
		// making the for loop look like a logical mess.
		for (int i = tableLayout.getChildCount() - 2; i >= 0; i--) {
			// get the tableRow from the table layout
			TableRow row = (TableRow) tableLayout.getChildAt(i);
			for (int j = 0; j < row.getChildCount(); j++) {
				// get the button from the tableRow
				Button butt = (Button) row.getChildAt(j);
				// if we are in the first row (topmost), and on the first button
				// (leftmost), we want that button to be a '('
				if (i == 0 && j == 0) {
					butt.setText("Floating Point");
					butt.setOnClickListener(floatingPointListener);
				}
				// if we are on the topmost row and the second button, make the
				// button a ')'
				else if (i == 0 && j == 1) {
					butt.setText("2's");
					butt.setOnClickListener(twosComplementButtonListener);
				} else {
					// this sets the button of the last column of every row
					if (i == tableLayout.getChildCount() - 2) {
						butt.setText("-");
						butt.setOnClickListener(genericMinusButtonListener);
					} else if (i == tableLayout.getChildCount() - 3) {
						butt.setText("x");
						butt.setOnClickListener(genericOperatorButtonListener);
					} else if (i == tableLayout.getChildCount() - 4) {
						butt.setText("/");
						butt.setOnClickListener(genericOperatorButtonListener);
					} else if (i == tableLayout.getChildCount() - 5) {
						butt.setText("<-");
						butt.setOnClickListener(backspaceButtonListener);
					}
				}
			}
		} // closes for() loop

		// get a reference to the first (topmost) row so we can set the clear
		// all button manually, because it was annoying trying to work it in to
		// the for loop
		TableRow firstRow = (TableRow) tableLayout.getChildAt(0);
		// the clear all button was decided to be the third button in the
		// topmost row
		Button clearAllButton = (Button) firstRow.getChildAt(2);
		clearAllButton.setText("Clear All");
		clearAllButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// clear all the text in the working textView, AND maybe the
				// computed textView as well?
				// Also, might want to clear out the post fix expression stack
				mWorkingTextView.setText("");
				mCurrentWorkingText = "";
				// update the Static variable in our activity so we can use it
				// as a fragment argument
				mComputeTextView.setText("");

				CalculatorDecimalFragment.numberOfOpenParenthesis = 0;
				CalculatorBinaryFragment.numberOfOpenParenthesis = 0;
				CalculatorHexFragment.numberOfOpenParenthesis = 0;
				CalculatorOctalFragment.numberOfOpenParenthesis = 0;

				CalculatorDecimalFragment.numberOfClosedParenthesis = 0;
				CalculatorBinaryFragment.numberOfClosedParenthesis = 0;
				CalculatorHexFragment.numberOfClosedParenthesis = 0;
				CalculatorOctalFragment.numberOfClosedParenthesis = 0;

				onPassData(mCurrentWorkingText);
			}
		});

		// get a reference to the second row of the table (AND, OR, NAND)
		TableRow secondRow = (TableRow) tableLayout.getChildAt(1);

		Button andButton = (Button) secondRow.getChildAt(0);
		andButton.setText(" AND ");
		andButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();

				if (mCurrentWorkingText.length() == 0) {
					// Do nothing if it's blank
				} else {
					// if the working TextView isn't zero we need to append
					// the
					// textFromButton to what is already there.
					mWorkingTextView.setText(mCurrentWorkingText
							+ textFromButton);
					mCurrentWorkingText = mWorkingTextView.getText().toString();
				}
				onPassData(mCurrentWorkingText);
			}
		});

		Button orButton = (Button) secondRow.getChildAt(1);
		orButton.setText(" OR ");
		orButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();

				if (mCurrentWorkingText.length() == 0) {
					// Do nothing if it's blank
				} else {
					// if the working TextView isn't zero we need to append
					// the
					// textFromButton to what is already there.
					mWorkingTextView.setText(mCurrentWorkingText
							+ textFromButton);
					mCurrentWorkingText = mWorkingTextView.getText().toString();
				}
				onPassData(mCurrentWorkingText);
			}
		});

		Button nandButton = (Button) secondRow.getChildAt(2);
		nandButton.setText(" NAND ");
		nandButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();

				if (mCurrentWorkingText.length() == 0) {
					// Do nothing if it's blank
				} else {
					// if the working TextView isn't zero we need to append
					// the
					// textFromButton to what is already there.
					mWorkingTextView.setText(mCurrentWorkingText
							+ textFromButton);
					mCurrentWorkingText = mWorkingTextView.getText().toString();
				}
				onPassData(mCurrentWorkingText);
			}
		});

		// get a reference to the third row (NOR, XOR, XNOR)
		TableRow thirdRow = (TableRow) tableLayout.getChildAt(2);
		// the NOR button
		Button norButton = (Button) thirdRow.getChildAt(0);
		norButton.setText(" NOR ");
		norButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();

				if (mCurrentWorkingText.length() == 0) {
					// Do nothing if it's blank
				} else {
					// if the working TextView isn't zero we need to append
					// the
					// textFromButton to what is already there.
					mWorkingTextView.setText(mCurrentWorkingText
							+ textFromButton);
					mCurrentWorkingText = mWorkingTextView.getText().toString();
				}
				onPassData(mCurrentWorkingText);
			}
		});
		// XOR button
		Button xorButton = (Button) thirdRow.getChildAt(1);
		xorButton.setText(" XOR ");
		xorButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();

				if (mCurrentWorkingText.length() == 0) {
					// Do nothing if it's blank
				} else {
					// if the working TextView isn't zero we need to append
					// the
					// textFromButton to what is already there.
					mWorkingTextView.setText(mCurrentWorkingText
							+ textFromButton);
					mCurrentWorkingText = mWorkingTextView.getText().toString();
				}
				onPassData(mCurrentWorkingText);
			}
		});
		// XNOR button
		Button xnorButton = (Button) thirdRow.getChildAt(2);
		xnorButton.setText("NOT");
		xnorButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();

				if (mCurrentWorkingText.length() == 0) {
					mWorkingTextView.setText(mCurrentWorkingText + " "
							+ textFromButton);
					mCurrentWorkingText = mWorkingTextView.getText().toString();

				} else {
					// if the working TextView isn't zero we need to append
					// the
					// textFromButton to what is already there.
					mWorkingTextView.setText(mCurrentWorkingText + " "
							+ textFromButton);
					mCurrentWorkingText = mWorkingTextView.getText().toString();

				}
			}
		});

		// fourth row (1, <<, >>)
		TableRow fourthRow = (TableRow) tableLayout.getChildAt(3);
		// button '1'
		Button oneButton = (Button) fourthRow.getChildAt(0);
		oneButton.setText("1");
		oneButton.setOnClickListener(genericNumberButtonListener);
		// bitwise shift Left button
		Button bitwiseShiftLeftButton = (Button) fourthRow.getChildAt(1);
		bitwiseShiftLeftButton.setText("<<");
		bitwiseShiftLeftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Bitwise shift Left

			}
		});
		// bitwise shift Right button
		Button bitwiseShiftRightButton = (Button) fourthRow.getChildAt(2);
		bitwiseShiftRightButton.setText(">>");
		bitwiseShiftRightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Bitwise shift Right

			}
		});

		// now we need to get the last row of buttons and get them to the
		// screen.
		TableRow lastRow = (TableRow) tableLayout.getChildAt(tableLayout
				.getChildCount() - 1);
		// set the decimal button
		Button zeroButton = (Button) lastRow.getChildAt(2);
		zeroButton.setText(".");
		zeroButton.setOnClickListener(new OnClickListener() {
			// we can't put a "." up there if there has already been one in
			// the current token (number)
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();

				// see if the workingTextView is empty, if so just add the '.'
				if (mCurrentWorkingText.length() == 0) {
					mWorkingTextView.setText(textFromButton);
					mCurrentWorkingText = textFromButton;
				} else {
					StringTokenizer toke = new StringTokenizer(
							mCurrentWorkingText, "+-/x)(", true);
					String currentElement = null;
					// get the current(last) token(number) so we can test if it
					// has a '.' in it.
					while (toke.hasMoreTokens()) {
						currentElement = toke.nextElement().toString();
					}
					// if the working TextView isn't zero we need to append
					// the
					// textFromButton to what is already there. AND we need to
					// check if the current token already has a '.' in it
					// because we can't have something like '2..2' or 2.2.33'
					if (mCurrentWorkingText.endsWith(".")
							|| currentElement.contains(".")) {
						// do nothing here so we don't end up with expressions
						// like "2..2" or "2.3.22"
					} else {
						// otherwise we're all good and just add the ".' up
						// there.
						mWorkingTextView.setText(mCurrentWorkingText
								+ textFromButton);
						mCurrentWorkingText = mWorkingTextView.getText()
								.toString();
					}
				}
				onPassData(mCurrentWorkingText);
			}
		});
		// set the zero button
		Button decimalPointButton = (Button) lastRow.getChildAt(1);
		decimalPointButton.setText("0");
		decimalPointButton.setOnClickListener(genericNumberButtonListener);
		// set the plus button
		Button plusButton = (Button) lastRow.getChildAt(3);
		plusButton.setText("+");
		plusButton.setOnClickListener(genericOperatorButtonListener);
		// set the equals button, it will have it's own separate listener to
		// compute the inputed value
		Button equalsButton = (Button) lastRow.getChildAt(0);
		equalsButton.setText("=");
		equalsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		return v;
	}

	public static SherlockFragment newInstance() {
		CalculatorBinaryFragment binFrag = new CalculatorBinaryFragment();
		return binFrag;
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

	// need to make sure the fragment life cycle complies with the
	// actionBarSherlock support library
	@Override
	public void onAttach(Activity activity) {
		if (!(activity instanceof SherlockFragmentActivity)) {
			throw new IllegalStateException(getClass().getSimpleName()
					+ " must be attached to a SherlockFragmentActivity.");
		}
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
	public void onPassData(String dataToBePassed) {
		mCallback.onDataPassed(dataToBePassed, VIEW_NUMBER, VIEWS_RADIX);
	}

	// converts a decimal fraction (the whole thing, integer AND fraction
	// portion) to binary fraction.
	public String convertToBinaryFraction(double numberToConvert, int radix) {

		BigDecimal bigNum = new BigDecimal(numberToConvert);
		BigDecimal expon = new BigDecimal(2).pow(radix);

		bigNum = bigNum.multiply(expon);

		BigInteger newNum = bigNum.toBigInteger();

		StringBuilder build = new StringBuilder(newNum.toString(VIEWS_RADIX));
		while (build.length() < radix + 1) {
			build.insert(0, "0");
		}
		build.insert(build.length() - radix, ".");

		return build.toString();
	}

	// method to receive the data from the activity/other-fragments and update
	// the textViews accordingly
	public void updateWorkingTextView(String dataToBePassed, int base) {
		if (dataToBePassed.length() != 0) {

			StringTokenizer toke = new StringTokenizer(dataToBePassed,
					"x+-/)( ", true);
			StringBuilder builder = new StringBuilder();

			while (toke.hasMoreElements()) {
				String aToken = (String) toke.nextElement().toString();
				if (aToken.equals("+") || aToken.equals("x")
						|| aToken.equals("-") || aToken.equals("/")
						|| aToken.equals("(") || aToken.equals(")")
						|| aToken.equals(" ")) {

					builder.append(aToken);

				}
				// if our token contains a "." in it then that means that we
				// need to do some conversion trickery
				else if (aToken.contains(".")) {
					if (aToken.endsWith(".")) {
						// don't do any conversions when the number is still
						// being
						// inputed and in the current state of something like
						// this "5."
						return;
					} else {
						// split the string around the "." delimiter.
						String[] parts = aToken.split("\\.");
						StringBuilder tempBuilder = new StringBuilder();

						if (aToken.charAt(0) == '.') {

						} else {

							// add the portion of the number to the left of the
							// "."
							// to our string this doesn't need any conversion
							// nonsense.
							tempBuilder.append(Integer.toBinaryString(Integer
									.parseInt(parts[0], base)));
						}
						// convert the fraction portion
						String getRidOfZeroBeforePoint = null;
						Character letterCheck = (Character) parts[1].charAt(0);
						if (base == 16 && Character.isLetter(letterCheck)) {
							tempBuilder.append(".");
							tempBuilder.append(Integer.toBinaryString(Integer
									.parseInt(parts[1], base)));
							builder.append(tempBuilder.toString());
						} else {
							getRidOfZeroBeforePoint = Fractions
									.convertFractionPortionToDecimal(parts[1],
											base);

							// the conversion returns just the fraction portion
							// with
							// a "0" to the left of the ".", so let's get rid of
							// that extra zero.
							getRidOfZeroBeforePoint = getRidOfZeroBeforePoint
									.substring(1,
											getRidOfZeroBeforePoint.length());

							tempBuilder.append(getRidOfZeroBeforePoint);

							// convert the newly converted decimal fraction to
							// binary. (the first decimal conversion method just
							// converts from some radix to decimal so we have to
							// convert that decimal to binary)
							builder.append(convertToBinaryFraction(
									Double.parseDouble(tempBuilder.toString()),
									base));
						}
					}
				} else {
					BigInteger sizeTestBigInt = new BigInteger(aToken, base);
					if (sizeTestBigInt.bitLength() < 64) {
						mCurrentWorkingText = Long.toBinaryString(Long
								.parseLong(aToken, base));
						builder.append(mCurrentWorkingText);
					}
				}
				mCurrentWorkingText = builder.toString();

				mWorkingTextView.setText(mCurrentWorkingText);
			}
		} else {
			mCurrentWorkingText = "";
			mWorkingTextView.setText(mCurrentWorkingText);
		}
	}
}
