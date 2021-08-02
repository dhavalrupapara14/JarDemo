package com.jar.demo.utils.glide;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

@GlideModule
public final class JarAppGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        // Glide default Bitmap Format is set to RGB_565 since it
        // consumed just 50% memory footprint compared to ARGB_8888.
        // Increase memory usage for quality with:
//        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888));

        /**
         * Glide calculates optimum cache sizes and bitmap pool size.
         * If you need more cache, you can set it using following code.
         * For this example, default values of Glide looks sufficient.
         * Hence, not setting cache manually.
         *
         * Default disk cache size of Glide: https://bumptech.github.io/glide/doc/configuration.html#disk-cache
         */
        /*int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        int bitmapPoolSizeBytes = 1024 * 1024 * 30; // 30mb
        builder.setBitmapPool(new LruBitmapPool(bitmapPoolSizeBytes));*/
        super.applyOptions(context, builder);
    }
}
