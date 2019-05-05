/*
 * MIT License
 *
 * Copyright (c) 2018 Alibaba Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tmall.wireless.vaf.framework;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewConfiguration;
import com.tmall.wireless.vaf.expr.engine.ExprEngine;
import com.tmall.wireless.vaf.expr.engine.NativeObjectManager;
import com.tmall.wireless.vaf.framework.cm.ComContainerTypeMap;
import com.tmall.wireless.vaf.framework.cm.ContainerService;
import com.tmall.wireless.vaf.virtualview.Helper.BeanManager;
import com.tmall.wireless.vaf.virtualview.Helper.DataOpt;
import com.tmall.wireless.vaf.virtualview.Helper.ImageLoader;
import com.tmall.wireless.vaf.virtualview.Helper.ImageLoader.IImageLoaderAdapter;
import com.tmall.wireless.vaf.virtualview.Helper.NativeViewManager;
import com.tmall.wireless.vaf.virtualview.Helper.ServiceManager;
import com.tmall.wireless.vaf.virtualview.core.IContainer;
import com.tmall.wireless.vaf.virtualview.core.ViewBase;
import com.tmall.wireless.vaf.virtualview.event.ClickProcessorManager;
import com.tmall.wireless.vaf.virtualview.event.EventData;
import com.tmall.wireless.vaf.virtualview.event.EventManager;
import com.tmall.wireless.vaf.virtualview.loader.StringLoader;

/**
 * Created by gujicheng on 16/9/19.
 */

public class VafContext {
    private final static String TAG = "PageContext_TMTEST";

    protected Context mContext;

    public static int SLOP;
    protected ExprEngine mExprEngine = new ExprEngine();
    protected ViewManager mViewManager = new ViewManager();
    protected BeanManager mBeanManager = new BeanManager();
    protected NativeViewManager mCompactNativeManager = new NativeViewManager();
    protected NativeObjectManager mNativeObjManager = new NativeObjectManager();
    protected static StringLoader mStringLoader = new StringLoader();
    protected ContainerService mContainerService;
    protected ImageLoader mImageLoader;
    protected EventManager mEventManager = new EventManager();
    protected UserData mUserData = new UserData();
    protected ComContainerTypeMap mComContainerTypeMap = new ComContainerTypeMap();
    protected ServiceManager serviceManager = new ServiceManager();

    protected ClickProcessorManager mClickProcessorManager = new ClickProcessorManager();

    protected Activity mCurActivity;

    public ComContainerTypeMap getComContainerTypeMap() {
        return mComContainerTypeMap;
    }

    public VafContext(Context context) {
        this(context, false);
    }

    public VafContext(Activity context) {
        this(context.getApplicationContext(), false);
        mCurActivity = context;
    }

    public VafContext(Context context, boolean coreOnly) {
        mContext = context;

        DataOpt.setStringLoader(mStringLoader);

        mViewManager.setPageContext(this);
        //mExprEngine.setPageContext(this);

        mNativeObjManager.setStringManager(mStringLoader);

        mExprEngine.setNativeObjectManager(mNativeObjManager);
        mExprEngine.setStringSupport(mStringLoader);
        mExprEngine.initFinished();

        if (!coreOnly) {
            mContainerService = new ContainerService();
            mContainerService.setPageContext(this);
        }
        mImageLoader = ImageLoader.build(context);
        try {
            SLOP = ViewConfiguration.get(context).getScaledTouchSlop();
        } catch (Exception e) {
            e.printStackTrace();
            SLOP = 8;
        }
    }

    public void init(Context context) {
        mContext = context;
    }

    public void uninit() {
        mContext = null;
        mCurActivity = null;
        EventData.clear();
    }

    public void onResume(int pageId) {
        if (pageId > -1) {
            mStringLoader.setCurPage(pageId);
        }
    }

    public void onDestroy(int pageId) {
        if (pageId > -1) {
            mStringLoader.remove(pageId);
        }
    }

    public UserData getUserData() {
        return mUserData;
    }

    public ClickProcessorManager getClickProcessorManager() {
        return mClickProcessorManager;
    }

    public void setCurActivity(Activity activity) {
        mCurActivity = activity;
    }

    final public EventManager getEventManager() { return mEventManager; }

    final public BeanManager getBeanManager() {return mBeanManager; }

    final public NativeViewManager getCompactNativeManager() {
        return mCompactNativeManager;
    }

    final public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    final public void setImageLoaderAdapter(IImageLoaderAdapter iImageLoaderAdapter) {
        mImageLoader.setImageLoaderAdapter(iImageLoaderAdapter);
    }

    final public ExprEngine getExprEngine() {
        return mExprEngine;
    }

    /**
     * Use {@link #getApplicationContext()} instead if you need application context. <br />
     * Use {@link #getActivityContext()} or {@link #getCurActivity()} if you need activity to create view. This require you provide activity instance by {@link #setCurActivity(Activity)} or creating {@link VafContext} with {@link #VafContext(Activity)}<br />
     * For convenience and compatibility, use {@link #forViewConstruction()} to create view.
     * @return
     */
    @Deprecated
    final public Context getContext() {
        return mContext;
    }

    final public Context getApplicationContext() {
        return mContext.getApplicationContext();
    }

    final public Context getActivityContext() {
        return mCurActivity;
    }

    final public Context forViewConstruction() {
        return mCurActivity != null ? mCurActivity : mContext;
    }

    final public NativeObjectManager getNativeObjectManager() {
        return mNativeObjManager;
    }

    final public StringLoader getStringLoader() {
        return mStringLoader;
    }

    final public ViewManager getViewManager() {
        return mViewManager;
    }

    final public ContainerService getContainerService() {
        return mContainerService;
    }

    final public Activity getCurActivity() {
        return mCurActivity;
    }

    public View createContainer(String type) {
        return mContainerService.getContainer(type);
    }

    public void recycleContainer(IContainer container) {
        mContainerService.recycle(container, false);
    }

    public ViewBase createView(String type) {
        return mViewManager.getView(type);
    }

    public void recycleView(ViewBase v) {
        mViewManager.recycle(v);
    }

    public <S> void registerService(@NonNull Class<S> type, @NonNull S service) {
        serviceManager.register(type, service);
    }

    public <S> S getService(@NonNull Class<S> type) {
        return serviceManager.getService(type);
    }

    public void onDestroy() {
        mContext = null;
        mCurActivity = null;
        EventData.clear();

        if (null != mExprEngine) {
            mExprEngine.destroy();
            mExprEngine = null;
        }

        if (null != mNativeObjManager) {
            mNativeObjManager.destroy();
            mNativeObjManager = null;
        }

        if (null != mViewManager) {
            mViewManager.destroy();
            mViewManager = null;
        }

        //if (null != mStringLoader) {
        //    mStringLoader.destroy();
        //    mStringLoader = null;
        //}

        if (null != mContainerService) {
            mContainerService.destroy();
            mContainerService = null;
        }
    }
}
