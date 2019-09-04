package taxi.kassa.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReciveSMS extends BroadcastReceiver {

    //private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        /*Log.i("My Logs", "onRecive");
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            Log.i("My Logs", "intent.getAction");
            if (bundle != null) {
                Log.i("My Logs", "bundle");
                // get sms objects
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus.length == 0) {
                    return;
                }
                // large message might be broken into many
                SmsMessage[] messages = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sb.append(messages[i].getMessageBody());
                }
                String sender = messages[0].getOriginatingAddress();
                String message = sb.toString();
                Log.i("My Logs", "sms: " + message);
                //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                // prevent any other broadcast receivers from receiving broadcast
                // abortBroadcast();
            }
        }*/
    }
}