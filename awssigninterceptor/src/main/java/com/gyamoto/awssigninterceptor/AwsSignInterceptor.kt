package com.gyamoto.awssigninterceptor

import android.net.Uri
import com.amazonaws.DefaultRequest
import com.amazonaws.auth.AWS4Signer
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.http.HttpMethodName
import com.amazonaws.regions.Regions
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.ByteArrayInputStream
import java.net.URI

class AwsSignInterceptor(
    private val credentialProvider: AWSCredentialsProvider,
    private val region: Regions,
    private val serviceName: String = DEFAULT_SERVICE_NAME
) : Interceptor {

    private val signer = AWS4Signer()
        .apply {
            setServiceName(serviceName)
            setRegionName(region.getName())
        }

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(sign(chain.request()))
    }

    private fun sign(request: Request): Request {

        val awsRequest = DefaultRequest<Any>(serviceName)
            .apply {
                setPath(request)
                setQueryParameters(request)
                setMethod(request)
                setBody(request)
            }

        signer.sign(awsRequest, credentialProvider.credentials)

        return request.newBuilder()
            .apply {
                setHeader(awsRequest.headers)
            }
            .build()
    }

    private fun DefaultRequest<*>.setPath(request: Request) {

        val url = request.url()
        resourcePath = url.pathSegments().joinToString(prefix = "/", separator = "/")

        val uri = Uri.Builder()
            .scheme(url.scheme())
            .authority(url.host())
            .build()
        endpoint = URI(uri.toString())
    }

    private fun DefaultRequest<*>.setQueryParameters(request: Request) {

        val url = request.url()
        url.queryParameterNames().forEach {
            addParameter(it, url.queryParameter(it))
        }
    }

    private fun DefaultRequest<*>.setMethod(request: Request) {
        httpMethod = HttpMethodName.valueOf(request.method())
    }

    private fun DefaultRequest<*>.setBody(request: Request) {

        request.body()?.let {
            val buffer = Buffer()
            it.writeTo(buffer)
            content = ByteArrayInputStream(buffer.readByteArray())
            addHeader("Content-Length", it.contentLength().toString())
            buffer.clone()
        }
    }

    private fun Request.Builder.setHeader(headers: Map<String, String>) {
        headers.forEach { key, value -> header(key, value) }
    }

    companion object {
        private const val DEFAULT_SERVICE_NAME = "execute-api"
    }
}
