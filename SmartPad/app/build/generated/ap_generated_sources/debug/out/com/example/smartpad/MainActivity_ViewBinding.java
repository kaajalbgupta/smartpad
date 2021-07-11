// Generated code from Butter Knife. Do not modify!
package com.example.smartpad;

import android.view.View;
import android.widget.Button;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(MainActivity target, View source) {
    this.target = target;

    target.registerButton = Utils.findRequiredViewAsType(source, R.id.register_button, "field 'registerButton'", Button.class);
    target.signInButton = Utils.findRequiredViewAsType(source, R.id.sign_in_button, "field 'signInButton'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.registerButton = null;
    target.signInButton = null;
  }
}
