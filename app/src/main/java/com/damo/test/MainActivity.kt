package com.damo.test

import android.os.Bundle
import android.widget.Toast
import com.app.common.api.RequestFileManager
import com.app.common.api.subscribeExtApi
import com.app.common.api.transformer.composeLife
import com.app.common.api.util.LifeCycleEvent
import com.app.common.logger.Logger
import com.app.common.utils.StorageUtils
import com.app.common.view.toastInfo
import com.damo.libdb.Dao
import com.damo.libdb.objectbox.ObjectBoxInit
import com.damo.test.activity.AnkoActivity
import com.damo.test.api.ApiManager
import com.damo.test.api.composeDefault
import com.damo.test.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import java.util.*

class MainActivity : BaseActivity() {
    override fun bindLayout(): Int = R.layout.activity_main

    var name by Dao<String>(String::class.java, "name")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ObjectBoxInit.build(applicationContext);
        tvPutCache.setOnClickListener {
            name = "张三${Random().nextInt(100)}"
        }
        tvGetCache.setOnClickListener {
            Toast.makeText(applicationContext, name, Toast.LENGTH_SHORT).show()
        }

        //todo 需要读写权限
        tvDownload.setOnClickListener {
            RequestFileManager.downloadFile(
                    "http://wangru.oss-cn-qingdao.aliyuncs.com/test/erp-v1.0.0-20190404.apk",
                    StorageUtils.getPublicStorageFile("test/wanban.apk")!!,
                    { file -> Toast.makeText(applicationContext, "下载成功${file.name}", Toast.LENGTH_SHORT).show() },
                    { e -> Toast.makeText(applicationContext, "下载失败", Toast.LENGTH_SHORT).show() },
                    { totalLength, contentLength, done ->
                        Logger.d("totalLength=$totalLength contentLength=$contentLength")
                    });
        }

        tvRequest.setOnClickListener {
            ApiManager.apiService
                    .update("1.0")
                    .compose(composeLife(LifeCycleEvent.DESTROY, lifecycleSubject))
                    .compose(composeDefault())
                    .subscribeExtApi({
                        toastInfo(it.toString())
                    }, context = activity, isShowLoad = true, isToast = true)
        }

        tvRN.setOnClickListener {
            startActivity<MainActivityRN>()
        }

        tvAnko.setOnClickListener {
            startActivity<AnkoActivity>()
        }

    }

    override fun onStop() {
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
    }
}
