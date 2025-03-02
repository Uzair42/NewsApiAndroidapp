package com.mu42.news

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.JsonArray
import com.mu42.news.ui.MySingleton
import com.mu42.news.ui.theme.DataNewsClass
import com.mu42.news.ui.theme.MyAdapter
import com.mu42.news.ui.theme.Ionclicking
import org.json.JSONArray

class MainActivity : ComponentActivity(), Ionclicking {
    lateinit var rv:RecyclerView
    lateinit var  adptr:MyAdapter


    val Adpterobj:MyAdapter=MyAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

       rv=findViewById(R.id.RecyclerViewId)
       rv.layoutManager=LinearLayoutManager(this)
       fetchData()

        rv.adapter=Adpterobj


    }




private fun fetchData()
{





    val url3 = "https://meme-api.com/gimme"
 
val jsonObjectRequestObject=JsonObjectRequest(Request.Method.GET,
    url3,
    null,
    { response ->

        if (response.has("author")) {
          val   articalArrayjson = response.getJSONArray("author")

            Log.d("article", " ${articalArrayjson.length() }")

            val NewsArray=ArrayList<DataNewsClass>()

            for(i in 0 until articalArrayjson.length())
            {
                val NewsObject=articalArrayjson.getJSONObject(i)
                val news1=DataNewsClass(
                    NewsObject.getString("title")
                    ,NewsObject.getString("author")
                    ,NewsObject.getString("url")
                    ,NewsObject.getString("urlToImage")

                )

                NewsArray.add(news1)



            }


            Adpterobj.updateData(NewsArray)

        }
        else {
            Log.e("API Response", "Key 'articles' not found in response!")
        }




    },
    { error ->
        if (error is TimeoutError || error is NoConnectionError) {
            Log.e("API Error", "No response or timeout")
        } else if (error.networkResponse != null) {
            val statusCode = error.networkResponse.statusCode
            val responseData = String(error.networkResponse.data)
            Log.e("API Error", "Error Code: $statusCode, Response: $responseData")
        } else {
            Log.e("API Error", "Unknown error: ${error.toString()}")
        }
    }


)

    MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequestObject)




}

    override fun onClickitem(itemI:DataNewsClass) {
       Toast.makeText(this,"you clicked on  $itemI" , Toast.LENGTH_LONG).show()
    }

}

