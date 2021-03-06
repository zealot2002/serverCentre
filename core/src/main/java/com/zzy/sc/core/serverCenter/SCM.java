package com.zzy.sc.core.serverCenter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.zzy.sc.core.utils.ClassUtils;
import com.zzy.sc.core.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Server Center Management
 * @author zzy
 * @date 2018/8/13
 */

public class SCM {
    private static final String TAG = "SCM";
    private static final String SCACTION_ROOT_PAKCAGE = "com.zzy.processor.generated";

    private static final SCM ourInstance = new SCM();
    public static SCM getInstance() {
        return ourInstance;
    }

    private SCM() {
    }

    private HashMap<String, ScAction> actionMap = new HashMap<>();
    private volatile boolean isReady;

/******************************************************************************************************/

    /**
     * scm init
     * @param context
     */
    public void init(Context context){
        Log.e(TAG,"init "+android.os.Process.myPid());
        try {
            List<String> classFileNames = ClassUtils.getFileNameByPackageName(context, SCACTION_ROOT_PAKCAGE);
            for (String className : classFileNames) {
                String s = Class.forName(className).newInstance().toString();
                Map<String,String> map = StringUtils.mapStringToMap(s);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    Class clazz = Class.forName(entry.getValue());
                    registerAction(entry.getKey().trim(), (ScAction) clazz.newInstance());
                }
            }
            isReady = true;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void req(Context context,String action) throws Exception{
        req(context,action,null,"",null);
    }
    public void req(Context context,String action,Bundle param) throws Exception{
        req(context,action,param,"",null);
    }

    public void req(Context context,String action,ScCallback callback) throws Exception{
        req(context,action,null,"",callback);
    }
    public void req(Context context,String action,Bundle param,ScCallback callback) throws Exception{
        req(context,action,param,"",callback);
    }
    /**
     * 请求服务
     *
     * @param action   请求的服务名称
     * @param param    携带参数
     * @param tag       请求标签
     * @param callback  标准返回
     * @throws Exception
     */
    public void req(Context context, String action, Bundle param, String tag ,ScCallback callback) throws Exception{
        Log.e(TAG,"req "+android.os.Process.myPid());
        if(!isReady){
            throw new RuntimeException("SCM is not ready! pls wait!");
        }
        if(!actionMap.containsKey(action)){
            throw new RuntimeException("SCM action not found! name:"+action);
        }
        ScAction scAction = actionMap.get(action);
        scAction.invoke(context,param,tag,callback);
    }

    /**
     * 取消服务
     *
     * @param action   请求的服务名称
     * @throws Exception
     */
    public void cancel(String action) throws Exception{
        if(!isReady){
            throw new RuntimeException("SCM is not ready! pls wait!");
        }
        if(!actionMap.containsKey(action)){
            throw new RuntimeException("SCM action not found! name:"+action);
        }
        ScAction scAction = actionMap.get(action);
        scAction.cancel();
    }

    /**
     * 注册服务
     *
     * @param action
     * @param scAction
     * @throws Exception
     */
    private void registerAction(String action,ScAction scAction)throws Exception{
        if(scAction == null||action == null){
            throw new Exception("bad input param!");
        }
        if(!actionMap.containsKey(action)){
//            throw new Exception("SCM: the same name action has already exist! action:"+action);
            actionMap.put(action,scAction);
        }
    }
}
