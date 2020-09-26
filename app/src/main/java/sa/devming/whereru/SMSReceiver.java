package sa.devming.whereru;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsMessage;
import android.util.Log;

import static android.content.Context.VIBRATOR_SERVICE;

public class SMSReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String strMsgBody = "";
        if ( extras != null ) {
            Object[] smsextras = (Object[]) extras.get( "pdus" );
            for ( int i = 0; i < smsextras.length; i++ ) {
                SmsMessage smsmsg = SmsMessage.createFromPdu((byte[])smsextras[i]);
                strMsgBody = smsmsg.getMessageBody().toString();
                Log.e(TAG, strMsgBody);
            }

            if ("업업업".equalsIgnoreCase(strMsgBody)) {
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                switch (audioManager.getRingerMode()) {
                    case AudioManager.RINGER_MODE_SILENT:
                        Log.e(TAG, "Silent mode");
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        audioManager.setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
                        break;
                    case AudioManager.RINGER_MODE_VIBRATE:
                        Log.e(TAG, "Vibrate mode");
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        audioManager.setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
                        break;
                    case AudioManager.RINGER_MODE_NORMAL:
                        Log.e(TAG, "Normal mode");
                        break;
                }
            } else if (strMsgBody.startsWith("징징징")) {
                ((Vibrator)context.getSystemService(VIBRATOR_SERVICE)).vibrate(3000);
            }
        }
    }
}
