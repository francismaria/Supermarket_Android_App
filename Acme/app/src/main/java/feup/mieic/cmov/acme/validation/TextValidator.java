package feup.mieic.cmov.acme.validation;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public abstract class TextValidator implements TextWatcher {
    private final TextView view;

    public TextValidator(TextView view){
        this.view = view;
    }

    public abstract void validate(TextView view, String text);

    @Override
    final public void afterTextChanged(Editable s){
        String text = view.getText().toString();
        validate(view, text);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after){
        // not used
    }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) {
        // not used
    }
}
