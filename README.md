# ANRandFCdispose
Android应用之异常处理
对android应用而言最常出现的异常是Force close和ANR（Application is not response）.
对于这两类错误而言，应用是可以进行相关处理的。
一 Forceclose这类问题主要通过Thread.UncaughtExceptionHandler这个类来捕获异常。通过实现类里面的方法uncaughtException来实现应用在捕获到异常后进行相关的处理。一般这里处理基本放在应用的Application类中。为了方便大家进行相关处理，我这里写了个类，大家直接在Application回调即可。
new ExceptionHandler(mContext).setFCListener(new ExceptionHandler.FCListener() {
            
            @Override
            public void onFCDispose(Throwable paramThrowable) {
                Log.d(TAG, "onFCListerner enter!!!");
                new Thread(){
                    public void run(){
                        Looper.prepare();
                        //处理force close异常
                        Toast.makeText(mContext, "APP is Force Close do what you want!", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }.start();
            }
        });
同样的对于ANR问题，应用也可以做相关处理。对ANR，我们可以这样处理。通过一个看门狗来实时的检测主线程，一旦主线程发生阻塞，则通知Application 做相关处理。
主要方法是在线程中每隔一段时间(Activity一般是5S，广播一般是10S)，向主线程发送一个messager，使计数器加1，如果到点没有加1，则表明主线程阻塞。
private final Runnable tickerRunnable = new Runnable() {
        @Override public void run() {
            mTick = (mTick + 1) % 10;
        }
    };
@Override
    public void run() {
        setName("|ANR-WatchDog|");

        int lastTick;
        while (!isInterrupted()) {
            lastTick = mTick;
            mUIHandler.post(tickerRunnable);
            try {
                Thread.sleep(mTimeoutInterval);
            }
            catch (InterruptedException e) {
                mInterruptionListener.onInterrupted(e);
                return ;
            }

            // If the main thread has not handled _ticker, it is blocked. ANR.
            if (mTick == lastTick) {
                ANRError error;
                if (mNamePrefix != null)
                    error = ANRError.New(mNamePrefix, mLogThreadsWithoutStackTrace);
                else
                    error = ANRError.NewMainOnly();
                mAnrListener.onAppNotResponding(error);
                return ;
            }
        }
    }
