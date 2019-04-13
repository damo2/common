package com.app.common.json

import com.google.gson.Gson
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * json数据转对象
 * Created by wangru
 * Date: 2018/4/4  11:08
 * mail: 1902065822@qq.com
 * describe:
 * 主要用于转换有泛型的对象
 */

object GsonConvert {
    inline fun <reified T : Any> fromJson(json: String): T {
        return GsonUtil().fromJson(json, T::class.java)
    }

    fun <T> jsonToObj(jsonStr: String?, type: Type): T {
        return Gson().fromJson(jsonStr, type)
    }

    /**
     * json 转对象  对象有一个泛型
     *
     * @param reader    json 数据
     * @param clazz     要转换成的对象类 eg：BaseBean<ResumeTxtBean>()::class.java
     * @param classType 要转换成的对象的泛型类 eg: ResumeTxtBean::class.java
     * @param <T>       要转换成的对象里面的泛型类名 eg: ResumeTxtBean
     * @return 要转换成的对象类  eg:BaseBean<ResumeTxtBean>()
     * eg: MessageBean<ImageBean> message = GsonConvert.jsonToBean(jsonString, MessageBean.class, ImageBean.class);
    </ImageBean></ResumeTxtBean></T></ResumeTxtBean> */
    fun <T> jsonToBean(reader: String?, clazz: Class<T>, classType: Class<*>): T? {
        val typeT = ParameterizedTypeImpl(clazz, arrayOf(classType))
        return Gson().fromJson<T>(reader, typeT)
    }

    /**
     * json 转List对象  对象里面有一个泛型
     *
     * @param json      json 数据
     * @param classType List对象的泛型类
     * @param <T>       List对象的泛型类名
     * @return List对象
     * eg: List<I> info=GsonConvert.jsonToBeanList(jsonString,iclass);
    </I></T> */
    fun <T> jsonToBeanList(json: String?, classType: Class<T>): List<T>? {
        val listType = ParameterizedTypeImpl(List::class.java, arrayOf(classType))
        return Gson().fromJson<List<T>>(json, listType)
    }

    /***
     *
     * @param json json 数据
     * @param clazz eg: SelectTypeBeam::class.java
     * @param classType eg:BaseBean::class.java
     * @return BaseBean<List></List><SelectTypeBeam>>
    </SelectTypeBeam> */
    @Deprecated("  val resultData = GsonConvert.fromJsonToBeanDataList(result,BaseBean::class.java,SelectTypeTwoBean::class.java) as BaseBean<List<SelectTypeTwoBean>>")
    fun <T> fromJsonToBeanDataList(json: String?, classType: Class<*>, clazz: Class<T>): Any? {
        // 生成List<T> 中的 List<T>
        val listType = ParameterizedTypeImpl(List::class.java, arrayOf(clazz))
        // 根据List<T>生成完整的Result<List<T>>
        val type = ParameterizedTypeImpl(classType, arrayOf(listType))
        return Gson().fromJson<Any>(json, type)
    }

    class ParameterizedTypeImpl(private val raw: Class<*>, args: Array<Type>?) : ParameterizedType {
        private val args: Array<Type>

        init {
            this.args = args ?: arrayOf()
        }

        override fun getActualTypeArguments(): Array<Type> {
            return args
        }

        override fun getRawType(): Type {
            return raw
        }

        override fun getOwnerType(): Type? {
            return null
        }
    }
}
