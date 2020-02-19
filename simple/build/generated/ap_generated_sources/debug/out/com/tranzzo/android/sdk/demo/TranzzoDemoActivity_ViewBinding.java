// Generated code from Butter Knife. Do not modify!
package com.tranzzo.android.sdk.demo;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.SwitchCompat;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.tranzzo.android.sdk.view.CardNumberEditText;
import com.tranzzo.android.sdk.view.CvcEditText;
import com.tranzzo.android.sdk.view.ExpiryDateEditText;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TranzzoDemoActivity_ViewBinding implements Unbinder {
  private TranzzoDemoActivity target;

  @UiThread
  public TranzzoDemoActivity_ViewBinding(TranzzoDemoActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TranzzoDemoActivity_ViewBinding(TranzzoDemoActivity target, View source) {
    this.target = target;

    target.tvResult = Utils.findRequiredViewAsType(source, R.id.tvResult, "field 'tvResult'", TextView.class);
    target.tvBrand = Utils.findRequiredViewAsType(source, R.id.tvBrand, "field 'tvBrand'", TextView.class);
    target.btnTokenize = Utils.findRequiredViewAsType(source, R.id.btnTokenize, "field 'btnTokenize'", Button.class);
    target.btnFillInDefault = Utils.findRequiredViewAsType(source, R.id.btnFillDefault, "field 'btnFillInDefault'", Button.class);
    target.btnFillInWrong = Utils.findRequiredViewAsType(source, R.id.btnFillWrong, "field 'btnFillInWrong'", Button.class);
    target.btnClear = Utils.findRequiredViewAsType(source, R.id.btnClearInputs, "field 'btnClear'", Button.class);
    target.btnCheckFormValid = Utils.findRequiredViewAsType(source, R.id.btnCheckFormValid, "field 'btnCheckFormValid'", Button.class);
    target.imgBrand = Utils.findRequiredViewAsType(source, R.id.imgBrand, "field 'imgBrand'", ImageView.class);
    target.etCardNumber = Utils.findRequiredViewAsType(source, R.id.etCardNumber, "field 'etCardNumber'", CardNumberEditText.class);
    target.etExpiration = Utils.findRequiredViewAsType(source, R.id.etExpiration, "field 'etExpiration'", ExpiryDateEditText.class);
    target.swEnv = Utils.findRequiredViewAsType(source, R.id.swEnv, "field 'swEnv'", SwitchCompat.class);
    target.swRich = Utils.findRequiredViewAsType(source, R.id.swRich, "field 'swRich'", SwitchCompat.class);
    target.etCvc = Utils.findRequiredViewAsType(source, R.id.etCvc, "field 'etCvc'", CvcEditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TranzzoDemoActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvResult = null;
    target.tvBrand = null;
    target.btnTokenize = null;
    target.btnFillInDefault = null;
    target.btnFillInWrong = null;
    target.btnClear = null;
    target.btnCheckFormValid = null;
    target.imgBrand = null;
    target.etCardNumber = null;
    target.etExpiration = null;
    target.swEnv = null;
    target.swRich = null;
    target.etCvc = null;
  }
}
