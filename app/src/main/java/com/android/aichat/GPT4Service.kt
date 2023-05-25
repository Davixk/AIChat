package com.android.aichat
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class GPT4Service {
    private val client = OkHttpClient()

    fun post(messages: List<Pair<String, String>>, callback: Callback) {
        val jsonArray = JSONArray()
        for (message in messages) {
            val jsonMessage = JSONObject()
            jsonMessage.put("role", message.first)
            jsonMessage.put("content", message.second)
            jsonArray.put(jsonMessage)
        }

        val jsonObject = JSONObject()
        jsonObject.put("model", "gpt-3.5-turbo")
        jsonObject.put("messages", jsonArray)

        val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("https://api.openai.com/v1/engines/davinci-codex/completions")
            .addHeader("Authorization", "Bearer YOUR_OPENAI_KEY")
            .post(body)
            .build()

        client.newCall(request).enqueue(callback)
    }
}
