# serverCentre

轻量级基于string的跨进程的事件总线
特点：

1，基于string发送消息，标准callback
使modlue之间0耦合。

2，跨进程通信（每一个进程维护一张action表）

3，使用bundle作为参数时，耦合度使用者自行把握。


##使用方法

SCM.getInstance().req(WelcomeActivity.this, ActionConstants.ENTRY_HOME_ACTIVITY_ACTION, new ScCallback() {
                @Override
                public void onCallback(boolean b, Bundle bundle, String s) {
                    if(b) {
                        //do your things
                    }
                }
            });

