package com.absinthe.anywhere_.utils

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.constants.AnywhereType
import com.absinthe.anywhere_.constants.Const
import com.absinthe.anywhere_.constants.GlobalValues
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.receiver.PinShortcutReceiver
import com.absinthe.anywhere_.ui.editor.impl.SWITCH_OFF
import com.absinthe.anywhere_.ui.shortcuts.ShortcutsActivity
import com.absinthe.libraries.utils.extensions.dp
import com.blankj.utilcode.util.Utils

object ShortcutsUtils {

  private const val PINNED_SHORTCUT_ID_PREFIX = "pinned_"

  val SHORTCUT_MANAGER: ShortcutManager? = if (AppUtils.atLeastNMR1()) {
    Utils.getApp().getSystemService(ShortcutManager::class.java)
  } else {
    null
  }

  @RequiresApi(api = Build.VERSION_CODES.N_MR1)
  fun addShortcut(ae: AnywhereEntity) {
    val intent = Intent(Utils.getApp(), ShortcutsActivity::class.java).apply {
      when (ae.type) {
        AnywhereType.Card.IMAGE -> {
          action = ShortcutsActivity.ACTION_START_IMAGE
          putExtra(Const.INTENT_EXTRA_SHORTCUTS_CMD, ae.param1)
        }
        else -> {
          action = ShortcutsActivity.ACTION_START_ENTITY
          putExtra(Const.INTENT_EXTRA_SHORTCUTS_ID, ae.id)
        }
      }
    }

    val info = ShortcutInfo.Builder(Utils.getApp(), ae.id)
      .setShortLabel(ae.appName.ifEmpty { " " })
      .setIcon(Icon.createWithBitmap(UxUtils.getAppIcon(Utils.getApp(), ae, 45.dp).toBitmap()))
      .setIntent(intent)
      .build()
    if (SHORTCUT_MANAGER!!.dynamicShortcuts.size <= 3) {
      SHORTCUT_MANAGER.addDynamicShortcuts(listOf(info))
    }

    val list = GlobalValues.shortcutsList.toMutableList()
    list.add(ae.id)
    GlobalValues.shortcutsList = list
  }

  @RequiresApi(api = Build.VERSION_CODES.N_MR1)
  fun updateShortcut(ae: AnywhereEntity) {
    val intent = Intent(Utils.getApp(), ShortcutsActivity::class.java).apply {
      when (ae.type) {
        AnywhereType.Card.IMAGE -> {
          action = ShortcutsActivity.ACTION_START_IMAGE
          putExtra(Const.INTENT_EXTRA_SHORTCUTS_CMD, ae.param1)
        }
        else -> {
          action = ShortcutsActivity.ACTION_START_ENTITY
          putExtra(Const.INTENT_EXTRA_SHORTCUTS_ID, ae.id)
        }
      }
    }

    val icon = if (ae.type == AnywhereType.Card.SWITCH_SHELL) {
      if (ae.param3 == SWITCH_OFF) {
        ContextCompat.getDrawable(Utils.getApp(), R.drawable.ic_red_dot)!!.toBitmap()
      } else {
        ContextCompat.getDrawable(Utils.getApp(), R.drawable.ic_green_dot)!!.toBitmap()
      }
    } else {
      UxUtils.getAppIcon(Utils.getApp(), ae, 45.dp).toBitmap()
    }
    val info = ShortcutInfo.Builder(Utils.getApp(), ae.id)
      .setShortLabel(ae.appName.ifEmpty { " " })
      .setIcon(Icon.createWithBitmap(icon))
      .setIntent(intent)
      .build()
    SHORTCUT_MANAGER!!.updateShortcuts(listOf(info))
  }

  @RequiresApi(api = Build.VERSION_CODES.N_MR1)
  fun removeShortcut(ae: AnywhereEntity) {
    val list = GlobalValues.shortcutsList.toMutableList()
    list.remove(ae.id)
    GlobalValues.shortcutsList = list
    SHORTCUT_MANAGER!!.removeDynamicShortcuts(listOf(ae.id))
  }

  @RequiresApi(api = Build.VERSION_CODES.N_MR1)
  fun clearAllShortcuts() {
    SHORTCUT_MANAGER!!.removeAllDynamicShortcuts()
    GlobalValues.shortcutsList = listOf()
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  fun addPinnedShortcut(ae: AnywhereEntity, icon: Drawable, name: String) {
    if (SHORTCUT_MANAGER?.isRequestPinShortcutSupported == true) {
      val intent = Intent(Utils.getApp(), ShortcutsActivity::class.java).apply {
        action = ShortcutsActivity.ACTION_START_ENTITY
        putExtra(Const.INTENT_EXTRA_SHORTCUTS_ID, ae.id)
      }

      val finalName = name.ifEmpty { " " }
      val pinShortcutInfo = ShortcutInfo.Builder(Utils.getApp(), pinnedShortcutId(ae.id))
        .setShortLabel(finalName)
        .setIcon(Icon.createWithBitmap(icon.toBitmap()))
        .setIntent(intent)
        .build()

      val callbackIntent = Intent(Utils.getApp(), PinShortcutReceiver::class.java).apply {
        action = PinShortcutReceiver.ACTION_PIN_SHORTCUT_RESULT
        data = Uri.Builder()
          .scheme("anywhere")
          .authority("pinned-shortcut-result")
          .appendPath(pinShortcutInfo.id)
          .build()
      }
      val callback = PendingIntent.getBroadcast(
        Utils.getApp(),
        0,
        callbackIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
      )

      SHORTCUT_MANAGER.requestPinShortcut(pinShortcutInfo, callback.intentSender)
      ToastUtil.makeText(R.string.toast_pinned_shortcut_requested)
    }
  }

  fun pinnedShortcutId(entityId: String) = PINNED_SHORTCUT_ID_PREFIX + entityId
}
