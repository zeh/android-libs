package com.zehfernando.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.shaklee.OneEighty.utils.DynamicRadioGroup;

public class FormChecker {

	// A class to check for form fields

	// Constants
	public static final int FLAG_CANNOT_BE_EMPTY = 1;
	public static final int FLAG_HAS_TO_BE_NUMBER = 2;

	public static final int ERROR_TEXT_IS_EMPTY = 1;
	public static final int ERROR_TEXT_IS_NOT_NUMBER = 2;
	public static final int ERROR_TEXT_IS_NOT_SAME_VALUE = 3;
	public static final int ERROR_NUMBER_IS_TOO_LOW = 4;
	public static final int ERROR_NUMBER_IS_TOO_HIGH = 5;
	public static final int ERROR_RADIOGROUP_NOT_SELECTED = 6;
	public static final int ERROR_CHECKBOX_NOT_SELECTED = 7;
	public static final int ERROR_DATE_IS_INVALID = 8;

	// Instances
	private final ArrayList<AbstractInputRule> rules;			// All checks
	private final ArrayList<AbstractInputRule> invalidRules;

	// Event listeners
	private OnInputCheckListener onInputCheckListener;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public FormChecker() {
		rules = new ArrayList<AbstractInputRule>();
		invalidRules = new ArrayList<AbstractInputRule>();
	}

	// ================================================================================================================
	// LISTENER INTERFACE ---------------------------------------------------------------------------------------------

	public interface OnInputCheckListener {
		public void onInputCheck(FormChecker __formChecker, View __associatedView, boolean __isValid);
	}

	private void dispatchOnRequestDelete(AbstractInputRule __rule, boolean __isValid) {
		if (onInputCheckListener != null) onInputCheckListener.onInputCheck(this, __rule.getAssociatedView(), __isValid);
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void addEditTextFlags(EditText __editText, int __flags) {
		// Adds a new input to be checked
		rules.add(new EditTextInputRule(__editText, __flags));
	}

	public void addEditTextMinMax(EditText __editText, double __minimum, double __maximum) {
		// Adds a new input to be checked
		rules.add(new EditTextMinMaxInputRule(__editText, __minimum, __maximum));
	}

	public void addEditTextSameValue(EditText __editText1, EditText __editText2) {
		// Adds a new input to be checked
		rules.add(new EditTextSameValueInputRule(__editText1, __editText2));
	}

	public void addEditTextDate(EditText __editTextDay, EditText __editTextMonth, EditText __editTextYear) {
		// Adds a new input to be checked
		rules.add(new EditTextDateInputRule(__editTextDay, __editTextMonth, __editTextYear));
	}

	public void addDynamicRadioGroupFlags(DynamicRadioGroup __radioGroup, int __flags) {
		rules.add(new DynamicRadioGroupInputRule(__radioGroup, __flags));
	}

	public void addCheckBoxFlags(CheckBox __checkBox, int __flags) {
		rules.add(new CheckBoxInputRule(__checkBox, __flags));
	}

	public void setOnInputCheckListener(OnInputCheckListener __listener) {
		onInputCheckListener = __listener;
	}

	public boolean check() {
		// Checks if the data is valid, and returns true if it is

		invalidRules.clear();
		boolean isValid;
		AbstractInputRule rule;

		for (int i = 0; i < rules.size(); i++) {
			rule = rules.get(i);

			// Checks validity of field
			isValid = rule.isValid();

			if (!isValid) F.log("Field " + rule + " is invalid.");
			if (!isValid) invalidRules.add(rule);

			// Dispatch results
			dispatchOnRequestDelete(rule, isValid);
		}

		boolean isAnyInvalid = invalidRules.size() > 0;

		F.log("Final isAnyInvalid = " + isAnyInvalid);

		return !isAnyInvalid;
	}

	public View getFirstInvalidInputView() {
		// Returns the first incorrect view, if any (to focus)
		return invalidRules.size() == 0 ? null : invalidRules.get(0).getAssociatedView();
	}

	public int getFirstInvalidInputErrorCode() {
		// Returns the first incorrect view, if any (to focus)
		return invalidRules.size() == 0 ? null : invalidRules.get(0).error;
	}

	public void setFieldsEnabled(boolean __enabled) {
		for (int i = 0; i < rules.size(); i++) {
			rules.get(i).setEnabled(__enabled);
		}
	}

	// ================================================================================================================
	// AUXILIARY INTERFACE --------------------------------------------------------------------------------------------

	// Abstract

	class AbstractInputRule {

		int error;

		public AbstractInputRule() {
			error = 0;
		}

		public boolean isValid() {
			error = 0;
			return true;
		}

		public View getAssociatedView() {
			return null;
		}

		public void setEnabled(boolean __enabled) {

		}
	}

	// Checkbox, simple

	class EditTextInputRule extends AbstractInputRule {
		public EditText editText;
		public int      flags;

		public EditTextInputRule(EditText __editText, int __flags) {
			super();
			editText = __editText;
			flags = __flags;
		}

		@Override
		public boolean isValid() {
			if ((flags & FLAG_CANNOT_BE_EMPTY) > 0) {
				if (editText.getText().toString().length() == 0) {
					error = ERROR_TEXT_IS_EMPTY;
					return false;
				}
			}

			if ((flags & FLAG_HAS_TO_BE_NUMBER) > 0) {
				try {
					@SuppressWarnings("unused")
					int val = Integer.parseInt(editText.getText().toString());
				} catch (NumberFormatException e) {
					error = ERROR_TEXT_IS_NOT_NUMBER;
					return false;
				}
			}

			return super.isValid();
		}

		@Override
		public View getAssociatedView() {
			return editText;
		}

		@Override
		public void setEnabled(boolean __enabled) {
			super.setEnabled(__enabled);
			editText.setEnabled(__enabled);
		}
	}

	class EditTextMinMaxInputRule extends EditTextInputRule {
		public double minimum;
		public double maximum;

		public EditTextMinMaxInputRule(EditText __editText, double __minimum, double __maximum) {
			super(__editText, 0);
			minimum = __minimum;
			maximum = __maximum;
		}

		@Override
		public boolean isValid() {
			try {
				@SuppressWarnings("unused")
				float val = FractionUtils.toFloat(editText.getText().toString());
				//double val = Double.parseDouble(editText.getText().toString());
				if (val < minimum) {
					error = ERROR_NUMBER_IS_TOO_LOW;
					return false;
				} else if (val > maximum) {
					error = ERROR_NUMBER_IS_TOO_HIGH;
					return false;
				}
			} catch (NumberFormatException e) {
				error = ERROR_TEXT_IS_NOT_NUMBER;
				return false;
			}

			return super.isValid();
		}

	}

	class EditTextSameValueInputRule extends AbstractInputRule {
		public EditText editText1;
		public EditText editText2;

		public EditTextSameValueInputRule(EditText __editText1, EditText __editText2) {
			super();
			editText1 = __editText1;
			editText2 = __editText2;
		}

		@Override
		public boolean isValid() {
			if (!editText1.getText().toString().equals(editText2.getText().toString())) {
				error = ERROR_TEXT_IS_NOT_SAME_VALUE;
				return false;
			}

			return super.isValid();
		}

		@Override
		public View getAssociatedView() {
			return editText2;
		}

		@Override
		public void setEnabled(boolean __enabled) {
			super.setEnabled(__enabled);
			editText1.setEnabled(__enabled);
			editText2.setEnabled(__enabled);
		}
	}

	class EditTextDateInputRule extends AbstractInputRule {
		public EditText editTextDay;
		public EditText editTextMonth;
		public EditText editTextYear;

		public EditTextDateInputRule(EditText __editTextDay, EditText __editTextMonth, EditText __editTextYear) {
			super();
			editTextDay = __editTextDay;
			editTextMonth = __editTextMonth;
			editTextYear = __editTextYear;
		}

		@Override
		public boolean isValid() {
			if (editTextDay.getText().length() == 0 || editTextMonth.getText().length() == 0 || editTextYear.getText().length() == 0) {
				error = ERROR_DATE_IS_INVALID;
				return false;
			}

			int day, month, year;

			try {
				day = Integer.parseInt(editTextDay.getText().toString(), 10);
				month = Integer.parseInt(editTextMonth.getText().toString(), 10);
				year = Integer.parseInt(editTextYear.getText().toString(), 10);
			} catch (NumberFormatException e) {
				error = ERROR_DATE_IS_INVALID;
				return false;
			}

			Date d = (new GregorianCalendar(year, month-1, day)).getTime();

			if (d == null || year < 1900) {
				error = ERROR_DATE_IS_INVALID;
				return false;
			}

			return super.isValid();
		}

		@Override
		public View getAssociatedView() {
			return editTextDay;
		}

		@Override
		public void setEnabled(boolean __enabled) {
			super.setEnabled(__enabled);
			editTextDay.setEnabled(__enabled);
			editTextMonth.setEnabled(__enabled);
			editTextYear.setEnabled(__enabled);
		}
	}

	// Radio group, simple

	class DynamicRadioGroupInputRule extends AbstractInputRule {
		public DynamicRadioGroup radioGroup;
		public int               flags;

		public DynamicRadioGroupInputRule(DynamicRadioGroup __radioGroup, int __flags) {
			super();
			radioGroup = __radioGroup;
			flags = __flags;
		}

		@Override
		public boolean isValid() {
			if ((flags & FLAG_CANNOT_BE_EMPTY) > 0) {
				if (!radioGroup.isAnyRadioButtonChecked()) {
					error = ERROR_RADIOGROUP_NOT_SELECTED;
					return false;
				}
			}

			return super.isValid();
		}

		@Override
		public View getAssociatedView() {
			// Returns the first RadioButton
			return radioGroup.size() > 0 ? radioGroup.get(0) : null;
		}

		@Override
		public void setEnabled(boolean __enabled) {
			super.setEnabled(__enabled);
			radioGroup.setButtonsEnabled(__enabled);
		}
	}

	// Checkbox, simple

	class CheckBoxInputRule extends AbstractInputRule {
		public CheckBox checkbox;
		public int      flags;

		public CheckBoxInputRule(CheckBox __checkbox, int __flags) {
			super();
			checkbox = __checkbox;
			flags = __flags;
		}

		@Override
		public boolean isValid() {
			if ((flags & FLAG_CANNOT_BE_EMPTY) > 0) {
				if (!checkbox.isChecked()) {
					error = ERROR_CHECKBOX_NOT_SELECTED;
					return false;
				}
			}

			return super.isValid();
		}

		@Override
		public View getAssociatedView() {
			return checkbox;
		}

		@Override
		public void setEnabled(boolean __enabled) {
			super.setEnabled(__enabled);
			checkbox.setEnabled(__enabled);
		}

	}

}
