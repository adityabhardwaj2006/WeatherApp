package com.example.weatherapplication

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weatherapp.apiInterface
import com.example.weatherapplication.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var city = "delhi"
        fetchWeather(city)
        SearchCity()
    }

    private fun SearchCity() {
        var search = binding.editTextText3
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                fetchWeather(p0)
                return true
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }



        })
    }
    fun getDayNameFromDateTime(dateTimeString: String): String {
        return try {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val dateTime = LocalDateTime.parse(dateTimeString, inputFormatter)
            dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()) // e.g. "Sunday"
        } catch (e: Exception) {
            "Unknown"
        }

    }
    private fun fetchWeather(city: String?) {
        var retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(apiInterface::class.java)
        var retrofitData = retrofitBuilder.getWeatherData(city,"b952e20d70c230c86d80d0a1dc953145","metric")
        retrofitData.enqueue(object:Callback<weatherData>{
            override fun onResponse(p0: Call<weatherData>, p1: Response<weatherData>) {
                val responseBody = p1.body()
                val city = responseBody?.city?.name
                binding.location.text = "$city, "
                val temp = responseBody?.list[0]?.main?.temp
                val temp2 = responseBody?.list[2]?.main?.temp
                val temp3 = responseBody?.list[10]?.main?.temp
                val day3 = responseBody?.list[10]?.dt_txt.toString()
                binding.txt2.text = getDayNameFromDateTime(day3)
                val split3 = day3.split(" ")
                val temp4 = responseBody?.list[18]?.main?.temp
                val day4 = responseBody?.list[18]?.dt_txt.toString()
                val temp5 = responseBody?.list[26]?.main?.temp
                val day5 = responseBody?.list[26]?.dt_txt.toString()

                val temp6 = responseBody?.list[34]?.main?.temp
                val day6 = responseBody?.list[34]?.dt_txt.toString()


                val country = responseBody?.city?.country
                binding.textView3.text = "$country"
                binding.temp.text = "$temp"
                binding.txt1.text = "Tomorrow"
                binding.txt20.text ="$temp2"
                binding.txt3.text = getDayNameFromDateTime(day4)
                binding.txt12.text = getDayNameFromDateTime(day5)
                binding.txt17.text = getDayNameFromDateTime(day6)
                binding.txt20.text = "$temp2"+"°C"
                binding.txt21.text = "$temp3"+"°C"
                binding.txt22.text = "$temp4"+"°C"
                binding.txt4.text = "$temp5"+"°C"
                binding.txt5.text = "$temp6"+"°C"
                val tempMax = responseBody?.list[0]?.main?.temp_max
                val tempMin = responseBody?.list[0]?.main?.temp_min
                binding.textView5.text = "$tempMax"
                binding.textView6.text = "$tempMin"
                val discription = responseBody?.list[0]?.weather[0]?.description
                binding.textView2.text = " $discription"
                binding.humidity.text = "Humidity"
                binding.windSpeed.text = "Wind Speed"
                binding.condition.text = "Condition"
                val wind = responseBody?.list[0]?.wind?.speed
                binding.windSpeed2.text = "$wind"
                val icon = responseBody?.list[0]?.weather[0]?.icon
                val url = "https://openweathermap.org/img/wn/${icon}@2x.png"
                Picasso.get().load(url).into(binding.imageView)
                val search = binding.editTextText3
                changeImage(discription)
            }


            private fun changeImage(dis: String?){
                when(dis){
                    "overcast clouds"-> {
                        binding.root.setBackgroundResource(R.drawable.overcast
                        )
                    }
                    "moderate rain"-> {
                        binding.root.setBackgroundResource(R.drawable.moderate)
                    }
                    "light rain"-> {
                        binding.root.setBackgroundResource(R.drawable.light)
                    }
                    "broken clouds"-> {
                        binding.root.setBackgroundResource(R.drawable.broken)
                    }
                    "few clouds"-> {
                        binding.root.setBackgroundResource(R.drawable.broken)
                    }
                    "scattered clouds"->{
                        binding.root.setBackgroundResource(R.drawable.overcast)
                    }
                    "clear sky"->{
                        binding.root.setBackgroundResource(R.drawable.clear)
                    }
                    else->{
                        binding.root.setBackgroundResource(R.drawable.wallpaper)
                    }
                }
            }


            override fun onFailure(p0: Call<weatherData>, p1: Throwable) {
                Log.d("MainActivity","onFailure :" + p1.message)
            }

        })
    }
}