package com.weiyao.zuzuapp.wxapi


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.common.view.toastInfo
import com.damo.loginshared.Const
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory


class WXEntryActivity : AppCompatActivity(), IWXAPIEventHandler {
    private val msgApi: IWXAPI by lazy {
        WXAPIFactory.createWXAPI(this, Const.WX_APPID)
                .apply {
                    registerApp(Const.WX_APPID)
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        msgApi.handleIntent(intent, this)
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    override fun onReq(req: BaseReq) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    override fun onResp(resp: BaseResp?) {
        when (resp?.type) {
            ConstantsAPI.COMMAND_SENDAUTH -> {
                resp.apply {
                    when (errCode) {
                        BaseResp.ErrCode.ERR_OK -> {
                            val code = (resp as SendAuth.Resp).code
                            toastInfo("登录成功")
                        }
                        BaseResp.ErrCode.ERR_USER_CANCEL -> {
                            toastInfo("登录取消")
                        }
                        BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                            toastInfo("登录失败")
                        }
                        else -> {
                            toastInfo("登录未知错误")
                        }
                    }
                }
            }
            ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX -> {
                resp.apply {
                    when (errCode) {
                        BaseResp.ErrCode.ERR_OK -> {
                            val code = (resp as SendAuth.Resp).code
                            toastInfo("分享成功")
                        }
                        BaseResp.ErrCode.ERR_USER_CANCEL -> {
                            toastInfo("用户取消")
                        }
                        BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                            toastInfo("分享失败")
                        }
                        else -> {
                            toastInfo("分享未知错误")
                        }
                    }
                }
            }
        }

        finish()
    }
}
