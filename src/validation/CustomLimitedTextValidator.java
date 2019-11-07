package validation;

import com.jfoenix.validation.base.ValidatorBase;

import javafx.scene.control.TextInputControl;

public class CustomLimitedTextValidator extends ValidatorBase {
	
	private int limit;
	
	public CustomLimitedTextValidator() {
		this.setMessage("Valor inserido muito longo!");
	}
	
	@Override
	protected void eval() {
		if (srcControl.get() instanceof TextInputControl) {
            evalTextInputField();
        }
	}
	
	private void evalTextInputField() {
        TextInputControl textField = (TextInputControl) srcControl.get();
        try {
        	if(textField.getText().length() > limit) {
                hasErrors.set(true);
        	}
        	else {
        		hasErrors.set(false);
        	}
        } catch (Exception e) {
            hasErrors.set(true);
        }
    }

	public  void setLimit(int limit) {
		this.limit = limit;
	}
	
	public int getLimit() {
		return limit;
	}
}