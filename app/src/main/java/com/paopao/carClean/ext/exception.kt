package com.paopao.carClean.ext


import android.content.Context
import android.widget.Toast
import com.paopao.carClean.BaseApp
import com.whynuttalk.foreignteacher.ext.e
import org.slf4j.LoggerFactory
import java.io.EOFException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * Created by cz on 3/28/18.
 */

private val logger = LoggerFactory.getLogger("Exceptions")

interface UserDescribableException {
    fun describe(context: Context): CharSequence
}


fun Throwable?.toast() {
    logger.e(this) { "Unhandled error" }
    if(this!!.message !="noMoreData") {
        Toast.makeText(BaseApp.instance, this.message?:"", Toast.LENGTH_LONG).show()
    }
}

data class ServerException (val name: String?, override val message: String?) : RuntimeException(name), UserDescribableException {
    private var errorMessageResolved: String? = null

    override fun describe(context: Context): CharSequence {
        if (message.isNullOrBlank().not()) {
            return message!!
        }

        if (errorMessageResolved?.isNotBlank() ?: false) {
            return errorMessageResolved!!
        }

        errorMessageResolved = context.getString(context.resources.getIdentifier("error_$name", "string", context.packageName)) ?: ""
        if (errorMessageResolved!!.isBlank()) {
                printStackTrace()
                return "未知错误"
        }

        return errorMessageResolved!!
    }
}