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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mu42.news.R
import com.mu42.news.ui.NewsArticle
import org.json.JSONException

class NewsDeepSeek : AppCompatActivity() {

    private lateinit var adapter: NewsAdapter
    private val newsList = mutableListOf<NewsArticle>()
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var image:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_news_deep_seek)



        swipeRefresh = findViewById(R.id.swipeRefresh)
        setupRecyclerView()
        setupSwipeRefresh()
        fetchNews()


    }


    private fun setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener {
            fetchNews()
        }
    }


    private fun setupRecyclerView() {

        adapter = NewsAdapter(newsList)
        findViewById<RecyclerView>(R.id.rvNews).apply {
            layoutManager = LinearLayoutManager(this@NewsDeepSeek)
            adapter = this@NewsDeepSeek.adapter
        }
    }



    private fun fetchNews() {
        swipeRefresh.isRefreshing = true
        val url = "https://newsapi.org/v2/top-headlines?sources=techcrunch&apiKey=6cdbed29016049d99ce1eb29754afb40"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
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
                Toast.makeText(this, "Network Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }




    private  fun loadMeme(){


        val bar= findViewById<ProgressBar>(R.id.progrssBar)

        bar.visibility= View.VISIBLE

        // In your Activity/Fragment
        val requestQueue = Volley.newRequestQueue(applicationContext)
        val url = "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=6cdbed29016049d99ce1eb29754afb40"
        // Create a GET request
        val jsonObjectRequestV = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
              val   imaeUrl = response.getString("urlToImage")

                Glide.with(this).load(imaeUrl).listener(object : RequestListener<Drawable> {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        bar.visibility= View.GONE
                        return  false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        bar.visibility= View.GONE
                        return  false
                    }
                }


                ).into(image)

            } ,


            { error ->
                // Handle errors
                Log.e("Volley", "Error: ${error.message}")
            }
        )
        // Add the request to the queue
        requestQueue.add(jsonObjectRequestV)



    }







}