// Generated code from Butter Knife. Do not modify!
package com.example.smartpad;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatEditText;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RegisterActivity_ViewBinding implements Unbinder {
  private RegisterActivity target;

  @UiThread
  public RegisterActivity_ViewBinding(RegisterActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public RegisterActivity_ViewBinding(RegisterActivity target, View source) {
    this.target = target;

    target.backButton = Utils.findRequiredViewAsType(source, R.id.back, "field 'backButton'", ImageButton.class);
    target.continueButton = Utils.findRequiredViewAsType(source, R.id.continue_button, "field 'continueButton'", Button.class);
    target.nameText = Utils.findRequiredViewAsType(source, R.id.name_sign_up_edit_text, "field 'nameText'", AppCompatEditText.class);
    target.emailText = Utils.findRequiredViewAsType(source, R.id.email_sign_up_edit_text, "field 'emailText'", AppCompatEditText.class);
    target.passwordText = Utils.findRequiredViewAsType(source, R.id.password_sign_up_edit_text, "field 'passwordText'", AppCompatEditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RegisterActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.backButton = null;
    target.continueButton = null;
    target.nameText = null;
    target.emailText = null;
    target.passwordText = null;
  }
}
