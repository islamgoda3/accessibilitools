package com.novoda.accessibility;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.accessibility.CaptioningManager;

class ClosedCaptionManagerFactory {

    public static CaptionManager createCaptionManager(Context context) {
        if (osSupportsNativeClosedCaptioning()) {
            return ClosedCaptionManager.newInstance(context);
        } else {
            return DummyClosedCaptionManager.newInstance();
        }
    }

    private static boolean osSupportsNativeClosedCaptioning() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    private static final class ClosedCaptionManager implements CaptionManager {

        private final CaptioningManager captioningManager;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        private static CaptionManager newInstance(Context context) {
            CaptioningManager captioningManager = (CaptioningManager) context.getSystemService(Context.CAPTIONING_SERVICE);
            return new ClosedCaptionManager(captioningManager);
        }

        private ClosedCaptionManager(CaptioningManager captioningManager) {
            this.captioningManager = captioningManager;
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public boolean isClosedCaptioningEnabled() {
            return captioningManager.isEnabled();
        }

    }

    private static class DummyClosedCaptionManager implements CaptionManager {

        private static CaptionManager newInstance() {
            return new DummyClosedCaptionManager();
        }

        private DummyClosedCaptionManager() {
            // no-op
        }

        @Override
        public boolean isClosedCaptioningEnabled() {
            return false;
        }

    }

    interface CaptionManager {

        boolean isClosedCaptioningEnabled();

    }

}