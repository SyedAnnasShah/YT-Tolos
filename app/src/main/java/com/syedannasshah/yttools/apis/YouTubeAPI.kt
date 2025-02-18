package com.syedannasshah.yttools.apis

import android.os.AsyncTask
import android.util.Log
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.json.JsonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchListResponse
import com.google.api.services.youtube.model.SearchResult
import java.io.IOException

class YouTubeAPI {

    companion object {
        private const val API_KEY = "YOUR_API_KEY"
        private const val CHANNEL_ID = "UCtJ49vCEAlqEsuSkI4t6huQ" // BayaanOfficial Channel ID

        fun fetchTopVideos() {
            object : AsyncTask<Void, Void, List<SearchResult>>() {
                override fun doInBackground(vararg params: Void?): List<SearchResult>? {
                    return try {
                        val youtubeService = getYouTubeService()
                        val request = youtubeService.search().list("snippet")
                        request.key = API_KEY
                        request.channelId = CHANNEL_ID
                        request.order = "viewCount" // Order by views
                        request.maxResults = 3L // Fetch top 3 videos

                        val response: SearchListResponse = request.execute()
                        response.items
                    } catch (e: IOException) {
                        Log.e("YouTubeAPI", "Error fetching YouTube videos", e)
                        null
                    }
                }

                override fun onPostExecute(result: List<SearchResult>?) {
                    result?.let {
                        for (searchResult in it) {
                            val snippet = searchResult.snippet
                            val videoTitle = snippet.title
                            val videoUrl = "https://www.youtube.com/watch?v=${searchResult.id.videoId}"
                            Log.d("YouTubeAPI", "Video: $videoTitle URL: $videoUrl")
                        }
                    }
                }
            }.execute()
        }

        private fun getYouTubeService(): YouTube {
            val transport = AndroidHttp.newCompatibleTransport()
            val jsonFactory: JsonFactory = com.google.api.client.json.JsonFactory.getDefaultInstance()
            return YouTube.Builder(transport, jsonFactory, null)
                .setApplicationName("YouTube Data API")
                .build()
        }
    }
}
