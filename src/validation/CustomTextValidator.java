package validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.DefaultProperty;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

/**
 * An example of Number field validation, that is applied on text input controls
 * such as {@link TextField} and {@link TextArea}
 *
 * @author Shadi Shaheen
 * @version 1.0
 * @since 2016-03-09
 */
@DefaultProperty(value = "icon")
public class CustomTextValidator extends ValidatorBase {

	public CustomTextValidator() {
		this.setMessage("Use apenas letras!");
	}

	
    /**
     * {@inheritDoc}
     */
    @Override
    protected void eval() {
        if (srcControl.get() instanceof TextInputControl) {
            evalTextInputField();
        }
    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl) srcControl.get();
        try {
        	@SuppressWarnings("unused")
			boolean allLetters = textField.getText().chars().allMatch(Character::isLetter);
        	Pattern p1 = Pattern.compile("^[ A-Za-z]+$");
        	Matcher m1 = p1.matcher(textField.getText());
        	boolean containLetterOrSpaces = m1.matches();
        	
        	Pattern p2 = Pattern.compile(".*[àâãáèêéìîíòôõóùûúý].*");
        	Matcher m2 = p2.matcher(textField.getText());
        	boolean containAccents = m2.matches();
        	
        	if(containLetterOrSpaces || containAccents){
        		hasErrors.set(false);
        	}else{
        		hasErrors.set(true);
        	}
        } catch (Exception e) {
            hasErrors.set(true);
        }
    }
}