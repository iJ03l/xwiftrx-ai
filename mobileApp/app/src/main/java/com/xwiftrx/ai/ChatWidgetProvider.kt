package com.xwiftrx.ai

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class ChatWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)

            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setOnClickPendingIntent(R.id.widgetTextView, pendingIntent)
            views.setOnClickPendingIntent(R.id.widgetButton, getPendingIntentToSendMessage(context))

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun getPendingIntentToSendMessage(context: Context): PendingIntent {
        val intent = Intent(context, ChatWidgetService::class.java)
        intent.action = ChatWidgetService.ACTION_SEND_TEST_MESSAGE
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}