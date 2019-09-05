package com.yogi.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import com.yogi.core.model.YtItemsMdl;

/**
 * Created by oohyugi on 2019-08-26.
 * github: https://github.com/oohyugi
 */
public class MediaDescri {

    public static MediaDescriptionCompat getMediaDescription(Context context, YtItemsMdl itemsMdl) {
        Bundle extras = new Bundle();


        return new MediaDescriptionCompat.Builder()
                .setMediaId(itemsMdl.getSnippet().getResourceId().getVideoId())
                .setTitle(itemsMdl.getSnippet().getTitle())
                .setDescription("")
                .setExtras(extras)
                .build();
    }

}
