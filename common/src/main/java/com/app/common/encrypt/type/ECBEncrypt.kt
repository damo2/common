package com.app.common.encrypt.type

import androidx.annotation.NonNull
import com.app.common.encrypt.IEncrypt
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

/**
 * Created by wangru
 * Date: 2018/7/3  20:50
 * mail: 1902065822@qq.com
 * describe:
 * ECB模式不安全，不建议使用。
 */

class ECBEncrypt : IEncrypt {
    private val key: String get() = "qwertyuiopasdfgh"

    /**
     * @param key 秘钥
     * 加密
     */
    override fun encrypt(@NonNull data: String, @NonNull key: String): String {
        check(data.isNotBlank())
        try {
            val desKey = DESKeySpec(key.toByteArray())
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            val keyFactory = SecretKeyFactory.getInstance("DES")
            val secureKey = keyFactory.generateSecret(desKey)
            // Cipher对象实际完成加密操作
            val cipher = Cipher.getInstance("DES")
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, secureKey)
            val dofi = cipher.doFinal(data.toByteArray(charset("UTF-8")))
            return Base64.encode(dofi)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }

    override fun decrypt(data: String, key: String): String {
        check(data.isNotBlank())
        try {
            val bytesrc = Base64.decode(data)
            val desKey = DESKeySpec(key.toByteArray())
            val keyFactory = SecretKeyFactory.getInstance("DES")
            val securekey = keyFactory.generateSecret(desKey)
            // Cipher对象实际完成加密操作
            val cipher = Cipher.getInstance("DES")
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey)
            val retByte = cipher.doFinal(bytesrc)
            return String(retByte)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }
}
