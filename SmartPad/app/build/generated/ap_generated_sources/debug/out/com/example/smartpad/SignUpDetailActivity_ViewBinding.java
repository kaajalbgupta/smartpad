// Generated code from Butter Knife. Do not modify!
package com.example.smartpad;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SignUpDetailActivity_ViewBinding implements Unbinder {
  private SignUpDetailActivity target;

  @UiThread
  public SignUpDetailActivity_ViewBinding(SignUpDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SignUpDetailActivity_ViewBinding(SignUpDetailActivity target, View source) {
    this.target = target;

    target.backButton = Utils.findRequiredViewAsType(source, R.id.back, "field 'backButton'", ImageButton.class);
    target.titleView = Utils.findRequiredViewAsType(source, R.id.title, "field 'titleView'", TextView.class);
    target.ageText = Utils.findRequiredViewAsType(source, R.id.age_edit_text, "field 'ageText'", AppCompatEditText.class);
    target.lengthText = Utils.findRequiredViewAsType(source, R.id.length_edit_text, "field 'lengthText'", AppCompatEditText.class);
    target.betweenText = Utils.findRequiredViewAsType(source, R.id.between_edit_text, "field 'betweenText'", AppCompatEditText.class);
    target.signUpButton = Utils.findRequiredViewAsType(source, R.id.sign_up_button, "field 'signUpButton'", Button.class);
    target.constraintLayout = Utils.findRequiredViewAsType(source, R.id.constraint_layout, "field 'constraintLayout'", ConstraintLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SignUpDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.backButton = null;
    target.titleView = null;
    target.ageText = null;
    target.lengthText = null;
    target.betweenText = null;
    target.signUpButton = null;
    target.constraintLayout = null;
  }
}
