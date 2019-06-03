package com.damo.test

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.app.common.api.RequestFileManager
import com.app.common.api.subscribeExtApi
import com.app.common.api.transformer.composeLife
import com.app.common.api.util.LifeCycleEvent
import com.app.common.extensions.limitLengthExt
import com.app.common.extensions.setOnClickExtNoFast
import com.app.common.json.toJsonExt
import com.app.common.logger.logd
import com.app.common.utils.SpanUtils
import com.app.common.utils.StorageUtils
import com.app.common.view.toastInfo
import com.damo.libdb.Dao
import com.damo.libdb.objectbox.ObjectBoxInit
import com.damo.test.activity.AnkoActivity
import com.damo.test.activity.test.Test2Activity
import com.damo.test.activity.test.Test3Activity
import com.damo.test.activity.test.TestActivity
import com.damo.test.api.ApiManager
import com.damo.test.api.composeDefault
import com.damo.test.base.BaseActivity
import com.damo.test.service.TestJobSchedulerService
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import java.io.File
import java.util.*

class MainActivity : BaseActivity() {
    override fun bindLayout(): Int = R.layout.activity_main

    data class UserBean(var name: String, var age: Int)

    var userBean by Dao<UserBean>(UserBean::class.java, "user")


    override fun initData() {
        super.initData()
    }

    override fun initListener() {
        super.initListener()
        tvPutCache.setOnClickListener {
            userBean = UserBean("张三", Random().nextInt(100))
        }
        tvGetCache.setOnClickListener {
            Toast.makeText(applicationContext, userBean.toJsonExt(), Toast.LENGTH_SHORT).show()
        }

        //todo 需要读写权限
        tvDownload.setOnClickListener {
            RequestFileManager.downloadFile(
                    "http://wangru.oss-cn-qingdao.aliyuncs.com/test/erp-v1.0.0-20190404.apk",
                    StorageUtils.getPublicStoragePath("test/wanban.apk"),
                    { file -> toastInfo("下载成功") },
                    { e -> toastInfo("下载失败${e.message}") },
                    { totalLength, contentLength, done ->
                        logd("totalLength=$totalLength contentLength=$contentLength")
                    });
        }
        tvUp.setOnClickListener {
            RequestFileManager.uploadFileByKey(
                    "http://www.wxjishu.com:9999/file/upload",
                    "file",
                    File(StorageUtils.getPublicStoragePath("test/wanban.apk")),
                    { str -> toastInfo("上传成功$str") },
                    { e -> toastInfo("上传失败$e") },
                    { progress, total -> logd("up=$progress total=$total") }
            )
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

        tvRN.setOnClickExtNoFast {
            startActivity<MainActivityRN>()
        }

        tvAnko.setOnClickExtNoFast {
            startActivity<AnkoActivity>()
        }
        edtInput.limitLengthExt(5, { toastInfo("最多输入字数为5") })

        tvSpan.text = SpanUtils.generateSideIconText(true, 10, "左边一个图 10px", ContextCompat.getDrawable(mContext, R.drawable.common_loading_icon))

        tvSpan2.text = SpanUtils.generateHorIconText("左右2个图 20px", 20, ContextCompat.getDrawable(mContext, R.drawable.common_toast_info), 20, ContextCompat.getDrawable(mContext, R.drawable.common_toast_error))

        tvService.setOnClickListener {
            TestJobSchedulerService.startJobScheduler(applicationContext)
        }

        tvTest.setOnClickListener {
            startActivity<TestActivity>()
        }

        tvTest2.setOnClickListener {
            startActivity<Test2Activity>()
        }

        tvTest3.setOnClickListener {
            startActivity<Test3Activity>()
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
    }
}
