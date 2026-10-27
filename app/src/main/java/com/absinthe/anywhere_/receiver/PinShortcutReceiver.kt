package com.absinthe.anywhere_.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.utils.ToastUtil

class PinShortcutReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    if (intent.action != ACTION_PIN_SHORTCUT_RESULT) {
      return
    }

    ToastUtil.makeText(context, R.string.toast_pinned_shortcut_success)
  }

  companion object {
    const val ACTION_PIN_SHORTCUT_RESULT =
      "com.absinthe.anywhere_.action.PINNED_SHORTCUT_RESULT"
  }
}
