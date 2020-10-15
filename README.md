# serverCentre

轻量级、跨进程的事件总线
特点：

1，基于bundle发送消息，标准callback,
使用者自行控制modlue之间是否需要耦合。

2，跨进程通信（每一个进程维护一张action表）


##使用方法
<pre><code>
SCM.getInstance().req(WelcomeActivity.this, ActionConstants.ENTRY_HOME_ACTIVITY_ACTION, new ScCallback() {
                @Override
                public void onCallback(boolean b, Bundle bundle, String s) {
                    if(b) {
                        //do your things
                    }
                }
            });


</code></pre>

