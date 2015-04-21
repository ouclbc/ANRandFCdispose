# ANRandFCdispose
volley源码解析
volley是2013年Google I/O大会上推出的一个新的网络通信框架
具体怎么用的，网上有很多实例，在这里就不给大家讲了，主要是讲一下其原理。
先看时序图，如下：
 
最后落在了CacheDispatcher和NetworkDispatcher这两个类上面。
看一下源码可知，这两个都是继承于线程类。
我们在使用它的时候，先new一个，不设置线程的话最后默认会创建四个NetworkDispatcher
最后要add到队列里面去mQueue.add(stringRequest); 
public <T> Request<T> add(Request<T> request) {
        // Tag the request as belonging to this queue and add it to the set of current requests.
        request.setRequestQueue(this);
        synchronized (mCurrentRequests) {
            mCurrentRequests.add(request);
        }

        // Process requests in the order they are added.
        request.setSequence(getSequenceNumber());
        request.addMarker("add-to-queue");

        // If the request is uncacheable, skip the cache queue and go straight to the network.
        if (!request.shouldCache()) {
            mNetworkQueue.add(request);
            return request;
        }

        // Insert request into stage if there's already a request with the same cache key in flight.
        synchronized (mWaitingRequests) {
            String cacheKey = request.getCacheKey();
            if (mWaitingRequests.containsKey(cacheKey)) {
                // There is already a request in flight. Queue up.
                Queue<Request<?>> stagedRequests = mWaitingRequests.get(cacheKey);
                if (stagedRequests == null) {
                    stagedRequests = new LinkedList<Request<?>>();
                }
                stagedRequests.add(request);
                mWaitingRequests.put(cacheKey, stagedRequests);
                if (VolleyLog.DEBUG) {
                    VolleyLog.v("Request for cacheKey=%s is in flight, putting on hold.", cacheKey);
                }
            } else {
                // Insert 'null' queue for this cacheKey, indicating there is now a request in
                // flight.
                mWaitingRequests.put(cacheKey, null);
                mCacheQueue.add(request);
            }
            return request;
        }
}
Add里面做了以下几件事情
1.	如果不带缓存，直接把请求加入到networkQueue里面。
2.	带缓存，如果缓存里面有这个请求，直接读取这个请求结果。？
 





























Android微信页面滑动之实现
通过android.support.v4.view.ViewPager控件实现。
先获取屏幕的分辨率，主要是获取宽度。
Display display = getWindowManager().getDefaultDisplay();//获取屏幕当前分辨率
int displayWidth = display.getWidth();
int displayHeight = display.getHeight();
one = displayWidth/4; //设置水平动画平移大小
two = one*2;
three = one*3;
最后通过监听动画移动到tab所指向的页面
public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_pressed));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 1:
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 2:
			mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 3:
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, three, 0, 0);
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				}
				else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}
@Override
public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

@Override
public void onPageScrollStateChanged(int arg0) {
		}
	}
具体可参考
https://github.com/astuetz/PagerSlidingTabStrip
https://github.com/JakeWharton/ViewPagerIndicator

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
