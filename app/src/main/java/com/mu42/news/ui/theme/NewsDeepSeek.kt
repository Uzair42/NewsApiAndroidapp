package com.mu42.news.ui.theme

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.Volley.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mu42.news.R
import com.mu42.news.ui.NewsArticle
import org.json.JSONException

class NewsDeepSeek : AppCompatActivity() {
    lateinit var request :JsonObjectRequest
    private lateinit var adapter: NewsAdapter
    private val newsList = ArrayList<NewsArticle>()
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_news_deep_seek)
  dummyNewsData()


        swipeRefresh = findViewById(R.id.swipeRefresh)
        setupRecyclerView()
        setupSwipeRefresh()
        fetchNews()


    }



    fun dummyNewsData()
    {
        newsList.add(NewsArticle("title","author","image.url.dumy.png","urlString"))
        newsList.add(NewsArticle("title","author","image.url.dumy.png","urlString"))
        newsList.add(NewsArticle("title","author","image.url.dumy.png","urlString"))
        newsList.add(NewsArticle("title","author","image.url.dumy.png","urlString"))
        newsList.add(NewsArticle("title","author","image.url.dumy.png","urlString"))
        newsList.add(NewsArticle("title","author","image.url.dumy.png","urlString"))
        newsList.add(NewsArticle("title","author","image.url.dumy.png","urlString"))
        newsList.add(NewsArticle("title","author","image.url.dumy.png","urlString"))
        newsList.add(NewsArticle("title","author","image.url.dumy.png","urlString"))
        newsList.add(NewsArticle("title","author","image.url.dumy.png","urlString"))
    }


    private fun setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener {
            fetchNews()
        }
    }


    private fun setupRecyclerView() {

        adapter = NewsAdapter(this, newsList)
        findViewById<RecyclerView>(R.id.rvNews).apply {
            layoutManager = LinearLayoutManager(this@NewsDeepSeek)
            adapter = this@NewsDeepSeek.adapter
        }
    }


    private fun fetchNews() {

        swipeRefresh.isRefreshing = true
        val url0="https://saurav.tech/NewsAPI/top-headlines/category/health/in.json"
        val url =
            "https://newsapi.org/v2/top-headlines?sources=techcrunch";
        val url2="https://newsapi.org/v2/top-headlines?sources=techcrunch&apiKey=6cdbed29016049d99ce1eb29754afb40"

try {


    request = object : JsonObjectRequest(
        Method.GET, url0, null,
        { response ->
            swipeRefresh.isRefreshing = false
            try {
                if (response.getString("status") == "ok") {
                    val articlesJsonArray = response.getJSONArray("articles")
                    newsList.clear()

                    for (i in 0 until articlesJsonArray.length()) {
                        val articleJson = articlesJsonArray.getJSONObject(i)
                        newsList.add(
                            NewsArticle(
                                title = articleJson.optString("title"),
                                author = articleJson.optString("author"),
                                urlToImage = articleJson.optString("urlToImage"),
                                url = articleJson.optString("url")
                            )
                        )
                    }
                    adapter.notifyDataSetChanged()

                    if (newsList.isEmpty()) {
                        Toast.makeText(this, "No articles found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val message = response.optString("message", "Unknown error")
                    Toast.makeText(this, "API Error: $message", Toast.LENGTH_LONG).show()
                }
            } catch (e: JSONException) {
                Toast.makeText(this, "Parsing error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        },
        { error ->
            swipeRefresh.isRefreshing = false
            Log.e("error listener ", "Volley Error: ${error.message}")
            error.networkResponse?.let { response ->
                val statusCode = response.statusCode
                val data = String(response.data ?: ByteArray(0)) // Get the error body as a string
                Log.e("error listener ", "Status Code: $statusCode")
                Log.e("error listener ", "Error Data: $data")
                Toast.makeText(this, "Network Error (Code: $statusCode)", Toast.LENGTH_LONG).show()
            } ?: run {
                Log.e("error listener ", "No Network Response")
                Toast.makeText(this, "Network Error: No response received", Toast.LENGTH_LONG)
                    .show()
            }
        }
    ) {
//        override fun getHeaders(): MutableMap<String, String> {
//            val headers = HashMap<String, String>()
//            headers["User-Agent"] = System.getProperty("http.agent") ?: "Volley/0.0"
//            headers["X-Api-Key"] =
//                "6cdbed29016049d99ce1eb29754afb40" // Example: If API expects key in X-Api-Key header
//            return headers
//        }


//        override fun getHeaders(): MutableMap<String, String> {
//            val headers = HashMap<String, String>()
//            headers["User-Agent"] = System.getProperty("http.agent") ?: "Volley/0.0"
//            headers["Referer"] = "https://www.google.com/" // If your browser sends a Referer header
//            headers["Accept-Language"] = "en-US,en;q=0.9" // If your browser sends an Accept-Language header
//            // Add any other relevant headers you see in your browser's request
//            return headers
//        }
    }
}
catch (e:Exception)
{
    Log.i("exception","${e.message.toString()}")
}
        finally {
            // Create a RequestQueue
            val requestQueue = newRequestQueue(this)

            // Add the request to the RequestQueue
            requestQueue.add(request)

        }

    }



}












