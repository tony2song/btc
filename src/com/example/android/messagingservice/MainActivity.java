package com.example.android.messagingservice;

import java.util.Iterator;

import com.example.android.BluetoothChat.R;
import com.example.android.BluetoothChat.R.drawable;
import com.example.android.BluetoothChat.R.id;
import com.example.android.BluetoothChat.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * The entry point to the BasicNotification sample.
 */
public class MainActivity extends Activity {
	
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String EOL = "\n";
	    
    /**
     * A numeric value that identifies the notification that we'll be sending.
     * This value needs to be unique within this app, but it doesn't need to be
     * unique system-wide.
     */
    public static final int NOTIFICATION_ID = 123;
    NotificationManager notificationManager = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_layout);
        
        notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        Button doIt = (Button)this.findViewById(R.id.button);
        doIt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showUrlDialog();
            }
        });

    }
    
    final String SUGGESTED_URL = "123"; 
    		//"http://www.vorbis.com/music/Epoq-Lepidoptera.ogg";
    
    void showUrlDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Manual Input");
        alertBuilder.setMessage("Enter a URL (must be http://)");
        final EditText input = new EditText(this);
        alertBuilder.setView(input);

        input.setText(SUGGESTED_URL);

        alertBuilder.setPositiveButton("Play!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int whichButton) {
                
            	String str = input.getText().toString();
            	if(str.contentEquals("123")){
            		if(notificationManager!=null){
                		notificationManager.cancel(NOTIFICATION_ID);
                		sendNotification(input);
                	}else{
                		Log.e("MainActivity", "notificationManager is null");
                	}
            	}
            	
                /*Intent i = new Intent(MusicService.ACTION_URL);
                Uri uri = Uri.parse(input.getText().toString());
                i.setData(uri);
                startService(i);*/
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int whichButton) {}
        });

        alertBuilder.show();
    }

    /**
     * Send a sample notification using the NotificationCompat API.
     */
    public void sendNotification(View view) {

        // BEGIN_INCLUDE(build_action)
        /** Create an intent that will be fired when the user clicks the notification.
         * The intent needs to be packaged into a {@link android.app.PendingIntent} so that the
         * notification service can fire it on our behalf.
         */
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://developer.android.com/reference/android/app/Notification.html"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // END_INCLUDE(build_action)

        // BEGIN_INCLUDE (build_notification)
        /**
         * Use NotificationCompat.Builder to set up our notification.
         */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        /** Set the icon that will appear in the notification bar. This icon also appears
         * in the lower right hand corner of the notification itself.
         *
         * Important note: although you can use any drawable as the small icon, Android
         * design guidelines state that the icon should be simple and monochrome. Full-color
         * bitmaps or busy images don't render well on smaller screens and can end up
         * confusing the user.
         */
        builder.setSmallIcon(R.drawable.ic_stat_notification);

        // Set the intent that will fire when the user taps the notification.
        builder.setContentIntent(pendingIntent);

        // Set the notification to auto-cancel. This means that the notification will disappear
        // after the user taps it, rather than remaining until it's explicitly dismissed.
        builder.setAutoCancel(true);

        /**
         *Build the notification's appearance.
         * Set the large icon, which appears on the left of the notification. In this
         * sample we'll set the large icon to be the same as our app icon. The app icon is a
         * reasonable default if you don't have anything more compelling to use as an icon.
         */
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

        /**
         * Set the text of the notification. This sample sets the three most commononly used
         * text areas:
         * 1. The content title, which appears in large type at the top of the notification
         * 2. The content text, which appears in smaller text below the title
         * 3. The subtext, which appears under the text on newer devices. Devices running
         *    versions of Android prior to 4.2 will ignore this field, so don't use it for
         *    anything vital!
         */
        
        Conversations.Conversation[] conversations = Conversations.getUnreadConversations(
                1, 1);
        Conversations.Conversation conversation = conversations[0];
        StringBuilder messageForNotification = new StringBuilder();
        for (Iterator<String> messages = conversation.getMessages().iterator();
             messages.hasNext(); ) {
            String message = messages.next();
            messageForNotification.append(message);
            if (messages.hasNext()) {
                messageForNotification.append(EOL);
            }
        }
        
        builder.setContentTitle("BasicNotifications Sample");
        //builder.setContentText("Time to learn about notifications!");
        builder.setContentText(messageForNotification.toString());
        builder.setSubText("Tap to view documentation about notifications.");

        // END_INCLUDE (build_notification)

        // BEGIN_INCLUDE(send_notification)
        /**
         * Send the notification. This will immediately display the notification icon in the
         * notification bar.
         */
       
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        // END_INCLUDE(send_notification)
    }
}
