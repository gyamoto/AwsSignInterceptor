package com.gyamoto.sample

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.amazonaws.auth.CognitoCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.regions.Regions.AP_NORTHEAST_1
import com.gyamoto.awssigninterceptor.AwsSignInterceptor
import com.gyamoto.sample.api.Api
import com.gyamoto.sample.model.Sweets
import kotlinx.android.synthetic.main.activity_main.delete
import kotlinx.android.synthetic.main.activity_main.get
import kotlinx.android.synthetic.main.activity_main.post
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val credentialProvider =
        CognitoCredentialsProvider("identity pool id", Regions.AP_NORTHEAST_1)

    private val authInterceptor = AwsSignInterceptor(credentialProvider, AP_NORTHEAST_1)

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl("base url")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(Api::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        get.setOnClickListener {
            api.getSweet().enqueue(object : Callback<List<Sweets>> {

                override fun onResponse(
                    call: Call<List<Sweets>>, response: Response<List<Sweets>>
                ) {
                    Snackbar.make(it, response.toString(), Snackbar.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<List<Sweets>>, error: Throwable) {
                    Snackbar.make(it, error.localizedMessage, Snackbar.LENGTH_SHORT).show()
                }
            })
        }

        post.setOnClickListener {
            api.postSweet().enqueue(object : Callback<Unit> {

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    Snackbar.make(it, response.toString(), Snackbar.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<Unit>, error: Throwable) {
                    Snackbar.make(it, error.localizedMessage, Snackbar.LENGTH_SHORT).show()
                }
            })
        }
        delete.setOnClickListener {
            api.deleteSweet().enqueue(object : Callback<Unit> {

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    Snackbar.make(it, response.toString(), Snackbar.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<Unit>, error: Throwable) {
                    Snackbar.make(it, error.localizedMessage, Snackbar.LENGTH_SHORT).show()
                }
            })
        }
    }
}
