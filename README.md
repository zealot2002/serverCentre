# serverCentre

轻量级、跨进程的事件总线
特点：

1，基于bundle发送消息，标准callback,
使用者自行控制modlue之间是否需要耦合。

2，跨进程通信（每一个进程维护一张action表）

使用：

api "com.github.zealot2002:serverCentre:last_version

##使用方法

1，提供服务
<pre><code>

//服务1 进入home Activity
@ScActionAnnotation(ActionConstants.ENTRY_HOME_ACTIVITY_ACTION)
public class HomeEntryAction implements ScAction {
    private WeakReference<Context> reference;
    @Override
    public void invoke(Context context, Bundle bundle, String s, ScCallback scCallback) {
        reference = new WeakReference<>(context);
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(reference.get(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        reference.get().startActivity(intent);
        if (scCallback != null) {
            scCallback.onCallback(true, new Bundle(), "");
        }
    }
}

//服务2 获取全局配置

@ScActionAnnotation(ActionConstants.GET_GLOBAL_CONFIG_ACTION)
public class GetGlobalConfigAction implements ScAction {
    @Override
    public void invoke(final Context context, final Bundle bundle, final String s, final ScCallback scCallback) {
        HttpProxy.getGlobalConfig(new HInterface.DataCallback() {
            @Override
            public void requestCallback(int result, Object data, Object tagData) {
                if (result == HConstant.SUCCESS) {
                    try {
                        JSONObject object = (JSONObject) data;
                        if(scCallback!=null)
                            scCallback.onCallback(true, object.toString(), "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        if(scCallback!=null)
                            scCallback.onCallback(false, null, e.toString());
                    }
                }else if(result == HConstant.INTERCEPTED){

                } else {
                    if(scCallback!=null)
                        scCallback.onCallback(false, null, (String) data);
                }
            }
        });
    }
}
</code></pre>

2,使用服务
<pre><code>

//使用服务1场景 进入home Activity
  SCM.getInstance().req(WelcomeActivity.this, ActionConstants.ENTRY_HOME_ACTIVITY_ACTION, new ScCallback() {
                @Override
                public void onCallback(boolean b, Bundle bundle, String s) {
                    if(b) {
                        //do your things
                    }
                }
            });

//使用服务2场景 获取全局配置
//module1获取 module2的数据  module之间通过bundle传输数据，解耦
//
      SCM.getInstance().req(application, ActionConstants.GET_GLOBAL_CONFIG_ACTIONnew ScCallback() {
                @Override
                public void onCallback(boolean b, Bundle bundle, String s) {
                    if(b) {
                        //从bundle中取数据
                    }
                }
            });

                
</code></pre>

