package com.zzy.core.serverCenter;

import android.content.Context;
import android.os.Bundle;

/**
 * @author zzy
 * @date 2018/8/13
 */

public interface ScAction {
    void invoke(final Context context,Bundle param,final String tag,final ScCallback callback);
}
