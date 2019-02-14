package net.sharksystem.bubble.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import net.sharksystem.aasp.AASPEngineFS;
import net.sharksystem.aasp.AASPException;
import net.sharksystem.bubble.BubbleMessage;
import net.sharksystem.bubble.model.BubbleMessageStorage;
import net.sharksystem.bubble.model.BubbleMessageStorageFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class BubbleApp {
    public static final String EXTRA_TOPIC_KEY = "topic";

    private static BubbleMessageStorage storageAnyTopic = null;

    private static HashMap<CharSequence, BubbleMessageStorage> bubbleStorages = new HashMap<>();

    /**
     *
     * @return root directory where persistent data can be stored
     */
    public static File getAASPRootDirectory(Context ctx) {
        return (Environment.getExternalStoragePublicDirectory(
                AASPEngineFS.DEFAULT_ROOT_FOLDER_NAME));

//        return ctx.getFilesDir();
    }

    /**
     * get storageAnyTopic singleton
     * @param ctx
     * @return
     * @throws IOException
     * @throws AASPException
     */
    public static BubbleMessageStorage getBubbleMessageStorage(Context ctx)
            throws IOException, AASPException {

        if(storageAnyTopic == null) {
            storageAnyTopic = BubbleMessageStorageFactory.getStorage(ctx);
        }

        return storageAnyTopic;
    }

    public static boolean isAnyTopic(CharSequence topic) {
        String tString = topic.toString();
        return tString.equalsIgnoreCase(BubbleMessage.ANY_TOPIC);
    }

    public static BubbleMessageStorage getBubbleMessageStorage(Context ctx, CharSequence topic)
            throws IOException, AASPException {

        if(BubbleApp.isAnyTopic(topic)) {
            return BubbleApp.getBubbleMessageStorage(ctx);
        }

        BubbleMessageStorage storage = BubbleApp.bubbleStorages.get(topic);
        if(storage == null) {
            storage = BubbleMessageStorageFactory.getStorage(ctx, topic);
            BubbleApp.bubbleStorages.put(topic, storage);
        }

        return storage;
    }

    public static CharSequence getTopicNameFromIntentExtras(Intent intent) {
        CharSequence topic = null;
        if(intent != null) {
            Bundle extras = intent.getExtras();
            if(extras != null) {
                CharSequence topicIExtras = extras.getCharSequence(BubbleApp.EXTRA_TOPIC_KEY);
                if(topicIExtras != null) {
                    topic = topicIExtras;
                }
            }
        }


        if(topic == null) {
            return BubbleMessage.ANY_TOPIC;
        }

        return topic;

    }
}
