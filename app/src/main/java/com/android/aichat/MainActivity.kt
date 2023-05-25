package com.android.aichat

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapter: MessageAdapter
    private lateinit var editTextMessage: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = MessageAdapter(messages)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "Bearer YOUR_OPENAI_API_KEY")
                .build()
            chain.proceed(request)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()

        val api = retrofit.create(OpenAIApi::class.java)

        val buttonSend: Button = findViewById(R.id.buttonSend)
        editTextMessage = findViewById(R.id.editTextMessage)

        buttonSend.setOnClickListener {
            val userMessage = editTextMessage.text.toString()
            if (userMessage.isNotEmpty()) {
                messages.add(ChatMessage(userMessage, Sender.USER))
                adapter.notifyItemInserted(messages.size - 1)

                val request = OpenAIRequest("gpt-4", listOf(Message("user", userMessage)))
                val call = api.createChatCompletion(request)
                call.enqueue(object : Callback<OpenAIResponse> {
                    override fun onResponse(call: Call<OpenAIResponse>, response: Response<OpenAIResponse>) {
                        if (response.isSuccessful) {
                            val chatResponse = response.body()
                            chatResponse?.let {
                                val assistantMessage = it.choices.first().message.content
                                messages.add(ChatMessage(assistantMessage, Sender.AI))
                                adapter.notifyItemInserted(messages.size - 1)
                            }
                        } else {
                            // TODO: handle the error here
                        }
                    }

                    override fun onFailure(call: Call<OpenAIResponse>, t: Throwable) {
                        // TODO: handle the error here
                    }
                })

                editTextMessage.text.clear()
            }
        }
    }
}