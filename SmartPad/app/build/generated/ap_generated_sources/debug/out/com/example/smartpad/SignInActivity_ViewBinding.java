// Generated code from Butter Knife. Do not modify!
package com.example.smartpad;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SignInActivity_ViewBinding implements Unbinder {
  private SignInActivity target;

  @UiThread
  public SignInActivity_ViewBinding(SignInActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SignInActivity_ViewBinding(SignInActivity target, View source) {
    this.target = target;

    target.backButton = Utils.findRequiredViewAsType(source, R.id.back, "field 'backButton'", ImageButton.class);
    target.signInButton = Utils.findRequiredViewAsType(source, R.id.sign_in_button, "field 'signInButton'", Button.class);
    target.emailText = Utils.findRequiredViewAsType(source, R.id.email_edit_text, "field 'emailText'", AppCompatEditText.class);
    target.passwordText = Utils.findRequiredViewAsType(source, R.id.password_edit_text, "field 'passwordText'", AppCompatEditText.class);
    target.constraintLayout = Utils.findRequiredViewAsType(source, R.id.constraint_layout, "field 'constraintLayout'", ConstraintLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SignInActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.backButton = null;
    target.signInButton = null;
    target.emailText = null;
    target.passwordText = null;
    target.constraintLayout = null;
  }
}
