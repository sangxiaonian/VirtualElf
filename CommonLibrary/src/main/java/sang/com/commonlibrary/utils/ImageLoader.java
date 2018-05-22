package sang.com.commonlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * 图片加载代理类。<br>
 * 需要换加载库或者添加方法 直接在这个类里面添加 方便以后修改
 * https://github.com/bumptech/glide
 * 使用的是glide 图片加载库已经实现了单例模式  故直接使用静态类调用 如需更改其他库 请注意调用方式
 *
 * @author dale.liu
 */

public class ImageLoader {
    /**
     * 加载图片
     *
     * @param imageView
     * @param url           图片url
     * @param ctx           上下文  建议用activity
     * @param placeholderId 占位图id
     */
    public static void loadImage(Context ctx, String url, int placeholderId, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderId);
        loadImage(ctx, url, options, imageView);
    }
 /**
     * 加载图片
     *
     * @param imageView
     * @param drawable           图片
     * @param ctx           上下文  建议用activity
     * @param placeholderId 占位图id
     */
    public static void loadImage(Context ctx, Drawable drawable, int placeholderId, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderId);
        loadImage(ctx, drawable, options, imageView);
    } /**
     * 加载图片
     *
     * @param imageView
     * @param drawable           图片
     * @param ctx           上下文  建议用activity
     */
    public static void loadImage(Context ctx, Drawable drawable, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                 ;
        loadImage(ctx, drawable, options, imageView);
    }

    /**
     * 加载图片
     *
     * @param imageView
     * @param id            图片url
     * @param ctx           上下文  建议用activity
     * @param placeholderId 占位图id
     */
    public static void loadImage(Context ctx, int id, int placeholderId, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderId);
        loadImage(ctx, id, options, imageView);
    }

    /**
     * 加载图片
     *
     * @param imageView
     * @param url       图片url
     * @param ctx       上下文  建议用activity
     */
    public static void loadImage(Context ctx, String url, ImageView imageView) {
        loadImage(ctx, url, null, imageView);
    }

    /**
     * 加载图片
     *
     * @param imageView
     * @param resId
     * @param ctx       上下文  建议用activity
     */
    public static void loadImage(Context ctx, int resId, ImageView imageView) {
        loadImage(ctx, resId, null, imageView);
    }

    /**
     * 加载图片
     *
     * @param imageView
     * @param url       图片url
     * @param ctx       上下文  建议用activity
     * @param width     需要的宽度
     * @param height    需要的高度
     */
    public static void loadImage(Context ctx, String url, int width, int height, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .override(width, height);
        loadImage(ctx, url, options, imageView);
    }

    public static void loadImageErr(Context ctx, String url, int error, ImageView imageView) {
        RequestOptions options = new RequestOptions().error(error);
        loadImage(ctx, url, options, imageView);
    }

    public static void loadImageErr(Context ctx, String url, int placeholderId, int error, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderId).error(error);
        loadImage(ctx, url, options, imageView);
    }

    /**
     * 加载图片
     *
     * @param ctx
     * @param url
     * @param placeholderId
     * @param width
     * @param height
     * @param imageView
     */
    public static void loadImage(Context ctx, String url, int placeholderId, int width, int height, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .override(width, height).placeholder(placeholderId);
        loadImage(ctx, url, options, imageView);
    }

    /**
     * 加载图片
     *
     * @param ctx
     * @param url
     * @param skip
     * @param strategy
     * @param imageView
     */
    public static void loadImage(Context ctx, String url, boolean skip, DiskCacheStrategy strategy, ImageView imageView) {
        RequestOptions options = new RequestOptions().skipMemoryCache(skip).diskCacheStrategy(strategy);
        loadImage(ctx, url, options, imageView);
    }

    public static void loadImage(Context ctx, String url, int placeholderId, int error, final LoadImageCallBack loadImageCallBack) {
        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                loadImageCallBack.onBitmap(resource);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                loadImageCallBack.onLoadFailed(errorDrawable);
            }
        };
        RequestOptions options = new RequestOptions().placeholder(placeholderId).error(error);
        Glide.with(ctx).asBitmap().load(url).apply(options).into(target);
    }



    private static void loadImage(Context ctx, String url, RequestOptions options, ImageView imageView) {
        if (ctx instanceof Activity) {
            if (options != null) {
                Glide.with((Activity) ctx).load(url).apply(options).into(imageView);
            } else {
                Glide.with((Activity) ctx).load(url).into(imageView);
            }
        } else {
            if (options != null) {
                Glide.with(ctx).load(url).apply(options).into(imageView);
            } else {
                Glide.with(ctx).load(url).into(imageView);
            }
        }
    }

    private static void loadImage(Context ctx, Drawable url, RequestOptions options, ImageView imageView) {
        if (ctx instanceof Activity) {
            if (options != null) {
                Glide.with((Activity) ctx).load(url).apply(options).into(imageView);
            } else {
                Glide.with((Activity) ctx).load(url).into(imageView);
            }
        } else {
            if (options != null) {
                Glide.with(ctx).load(url).apply(options).into(imageView);
            } else {
                Glide.with(ctx).load(url).into(imageView);
            }
        }
    }


    private static void loadImage(Context ctx, int url, RequestOptions options, ImageView imageView) {
        if (null == ctx)
            return;

        if (ctx instanceof Activity) {
//            if(((Activity)ctx).isDestroyed())
//                return;

            if (options != null) {
                Glide.with((Activity) ctx).load(url).apply(options).into(imageView);
            } else {
                Glide.with((Activity) ctx).load(url).into(imageView);
            }
        } else {
            if (options != null) {
                Glide.with(ctx).load(url).apply(options).into(imageView);
            } else {
                Glide.with(ctx).load(url).into(imageView);
            }
        }
    }

    public abstract static class LoadImageCallBack {
        public abstract void onBitmap(Bitmap resource);

        public void onLoadFailed(Drawable errorDrawable) {
        }

        ;
    }

    public static void getBitamp(Context cxt, String url, int w, final LoadImageCallBack back) {
        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                back.onBitmap(resource);
            }
        };

        RequestOptions options = new RequestOptions()
                .override(w, w);
        Glide.with(cxt).asBitmap().apply(options).load(url).into(target);
    }
}
