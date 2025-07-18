package com.jiyeon.easypicktrip

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.jiyeon.easypicktrip.repository.WeatherRepository
import kotlinx.coroutines.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var titleText: TextView
    private lateinit var subtitleText: TextView
    private lateinit var mainLayout: LinearLayout
    private lateinit var loadingLayout: LinearLayout
    private lateinit var relaxationButton: Button
    private lateinit var sightseeingButton: Button
    private lateinit var adventureButton: Button
    private lateinit var cultureButton: Button
    private lateinit var anywhereButton: Button
    private lateinit var diceImage: ImageView
    private lateinit var loadingText: TextView
    private lateinit var resultCard: CardView
    private lateinit var resultTitle: TextView
    private lateinit var destinationText: TextView
    private lateinit var weatherText: TextView
    private lateinit var budgetText: TextView
    private lateinit var durationText: TextView
    private lateinit var tipText: TextView
    private lateinit var retryButton: Button
    private lateinit var weatherIcon: ImageView
    private lateinit var temperatureText: TextView
    private lateinit var humidityText: TextView
    private lateinit var windSpeedText: TextView

    // OpenWeatherMap API 키
    private val API_KEY = BuildConfig.weatherKey
    private val weatherRepository = WeatherRepository()

    private val destinations = mapOf(
        "휴양" to listOf(
            Destination("몰디브", "Male", "MV", "맑음, 28-32°C", "200-400만원", "5-7일", "🏖️ 스노클링 장비를 꼭 챙기세요!"),
            Destination("발리", "Denpasar", "ID", "맑음, 26-30°C", "80-150만원", "4-6일", "🌺 우기는 피하고 건기에 방문하세요!"),
            Destination("푸켓", "Phuket", "TH", "맑음, 27-31°C", "100-200만원", "5-7일", "🏝️ 피피섬 투어를 놓치지 마세요!"),
            Destination("제주도", "Jeju", "KR", "맑음, 20-25°C", "30-80만원", "3-4일", "🌊 렌터카로 해안도로 드라이브 추천!"),
            Destination("하와이", "Honolulu", "US", "맑음, 24-28°C", "300-500만원", "7-10일", "🌺 선크림을 충분히 발라주세요!"),
            Destination("칸쿤", "Cancun", "MX", "맑음, 25-29°C", "250-400만원", "6-8일", "🏖️ 세노테에서 다이빙 체험해보세요!")
        ),
        "관광" to listOf(
            Destination("파리", "Paris", "FR", "흐림, 15-20°C", "200-350만원", "5-7일", "🗼 에펠탑은 야경이 가장 아름다워요!"),
            Destination("도쿄", "Tokyo", "JP", "맑음, 18-23°C", "150-250만원", "4-6일", "🍣 츠키지 시장 새벽 참치 경매 구경하세요!"),
            Destination("뉴욕", "New York", "US", "구름많음, 12-18°C", "300-500만원", "6-8일", "🗽 브로드웨이 뮤지컬 예약은 미리!"),
            Destination("로마", "Rome", "IT", "맑음, 16-22°C", "180-300만원", "4-6일", "🏛️ 콜로세움은 오전 일찍 가세요!"),
            Destination("런던", "London", "GB", "비, 10-16°C", "250-400만원", "5-7일", "☂️ 우산은 필수! 대영박물관 추천!"),
            Destination("바르셀로나", "Barcelona", "ES", "맑음, 19-24°C", "180-280만원", "4-6일", "🏛️ 사그라다 파밀리아 사전 예약 필수!")
        ),
        "상관없음" to listOf(
            Destination("싱가포르", "Singapore", "SG", "맑음, 26-30°C", "120-200만원", "3-5일", "🌃 마리나 베이 샌즈 야경이 환상적!"),
            Destination("베트남 다낭", "Da Nang", "VN", "맑음, 24-28°C", "60-120만원", "4-6일", "🍜 쌀국수와 바인미 맛집 투어!"),
            Destination("체코 프라하", "Prague", "CZ", "맑음, 14-19°C", "100-180만원", "4-5일", "🏰 구시가지 천문시계 정각에 구경하세요!"),
            Destination("터키 이스탄불", "Istanbul", "TR", "맑음, 17-22°C", "120-200만원", "5-7일", "🕌 블루모스크와 아야소피아 필수 코스!"),
            Destination("아이슬란드", "Reykjavik", "IS", "흐림, 8-12°C", "200-350만원", "6-8일", "🌌 오로라 관측은 9월-3월이 최적!"),
            Destination("페루 쿠스코", "Cusco", "PE", "맑음, 15-20°C", "150-250만원", "7-10일", "🏔️ 고산병 예방약 미리 복용하세요!")
        ),
        "액티비티" to listOf(
            Destination("네팔 포카라", "Pokhara", "NP", "맑음, 10-18°C", "100-200만원", "8-12일", "🏔️ 히말라야 트레킹 장비 대여 가능!"),
            Destination("뉴질랜드 퀸스타운", "Queenstown", "NZ", "맑음, 12-18°C", "250-400만원", "7-10일", "🪂 번지점프 발상지에서 도전해보세요!"),
            Destination("페루 마추픽추", "Cusco", "PE", "맑음, 15-20°C", "200-300만원", "8-10일", "🥾 잉카 트레일 예약은 6개월 전에!"),
            Destination("코스타리카", "San Jose", "CR", "맑음, 22-28°C", "180-280만원", "7-9일", "🦋 생태관광의 천국! 가이드 투어 추천!"),
            Destination("몽골 고비사막", "Ulaanbaatar", "MN", "맑음, 20-35°C", "150-250만원", "6-8일", "🐪 낙타 트레킹과 유목민 체험!"),
            Destination("요단 와디럼", "Amman", "JO", "맑음, 25-35°C", "200-300만원", "5-7일", "🌌 사막에서 별 관측이 압권!")
        ),
        "문화" to listOf(
            Destination("인도 라자스탄", "Jaipur", "IN", "맑음, 20-30°C", "80-150만원", "8-10일", "🏰 궁전 호텔에서 하룻밤 경험해보세요!"),
            Destination("이집트 카이로", "Cairo", "EG", "맑음, 18-25°C", "120-200만원", "6-8일", "🏺 피라미드 내부 투어 예약 필수!"),
            Destination("그리스 아테네", "Athens", "GR", "맑음, 18-25°C", "150-250만원", "5-7일", "🏛️ 아크로폴리스는 일몰 시간이 최고!"),
            Destination("일본 교토", "Kyoto", "JP", "맑음, 16-22°C", "120-200만원", "4-6일", "🌸 기온 지구에서 게이샤 만날 수 있어요!"),
            Destination("멕시코 유카탄", "Merida", "MX", "맑음, 24-30°C", "150-250만원", "6-8일", "🗿 치첸이트사 마야 유적지 탐험!"),
            Destination("캄보디아 앙코르", "Siem Reap", "KH", "맑음, 26-32°C", "80-150만원", "4-6일", "🏛️ 앙코르와트 일출 투어 놓치지 마세요!")
        )
    )

    data class Destination(
        val name: String,
        val cityName: String,
        val countryCode: String,
        val weather: String,
        val budget: String,
        val duration: String,
        val tip: String
    )

    data class WeatherData(
        val temperature: Double,
        val description: String,
        val humidity: Int,
        val windSpeed: Double,
        val iconCode: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupClickListeners()
        showInitialScreen()
    }

    private fun initViews() {
        titleText = findViewById(R.id.titleText)
        subtitleText = findViewById(R.id.subtitleText)
        mainLayout = findViewById(R.id.mainLayout)
        loadingLayout = findViewById(R.id.loadingLayout)
        relaxationButton = findViewById(R.id.relaxationButton)
        sightseeingButton = findViewById(R.id.sightseeingButton)
        adventureButton = findViewById(R.id.adventureButton)
        cultureButton = findViewById(R.id.cultureButton)
        anywhereButton = findViewById(R.id.anywhereButton)
        diceImage = findViewById(R.id.diceImage)
        loadingText = findViewById(R.id.loadingText)
        resultCard = findViewById(R.id.resultCard)
        resultTitle = findViewById(R.id.resultTitle)
        destinationText = findViewById(R.id.destinationText)
        weatherText = findViewById(R.id.weatherText)
        budgetText = findViewById(R.id.budgetText)
        durationText = findViewById(R.id.durationText)
        tipText = findViewById(R.id.tipText)
        retryButton = findViewById(R.id.retryButton)
        weatherIcon = findViewById(R.id.weatherIcon)
        temperatureText = findViewById(R.id.temperatureText)
        humidityText = findViewById(R.id.humidityText)
        windSpeedText = findViewById(R.id.windSpeedText)
    }

    private fun setupClickListeners() {
        relaxationButton.setOnClickListener {
            showDiceAnimation("휴양")
        }

        sightseeingButton.setOnClickListener {
            showDiceAnimation("관광")
        }

        adventureButton.setOnClickListener {
            showDiceAnimation("액티비티")
        }

        cultureButton.setOnClickListener {
            showDiceAnimation("문화")
        }

        anywhereButton.setOnClickListener {
            showDiceAnimation("상관없음")
        }

        retryButton.setOnClickListener {
            showInitialScreen()
        }
    }

    private fun showInitialScreen() {
        // 초기 화면 표시
        mainLayout.visibility = View.VISIBLE

        // 다른 화면 숨기기
        loadingLayout.visibility = View.GONE
        resultCard.visibility = View.GONE
    }

    private fun showDiceAnimation(travelType: String) {
        // 초기 화면 숨기기
        mainLayout.visibility = View.GONE

        // 로딩 화면 표시
        loadingLayout.visibility = View.VISIBLE
        resultCard.visibility = View.GONE

        // 자연스러운 bounce 애니메이션
        animateDiceBounce()

        // 애니메이션 후 결과 표시
        Handler(Looper.getMainLooper()).postDelayed({
            showResult(travelType)
        }, 3000)
    }

    private fun animateDiceBounce() {
        // 연속적인 bounce 애니메이션
        val bounceAnimation = ObjectAnimator.ofFloat(diceImage, "translationY", 0f, -100f, 0f, -60f, 0f, -30f, 0f).apply {
            duration = 1500
            interpolator = AccelerateDecelerateInterpolator()
        }

        val rotateAnimation = ObjectAnimator.ofFloat(diceImage, "rotation", 0f, 360f).apply {
            duration = 1500
            interpolator = AccelerateDecelerateInterpolator()
        }

        val scaleXAnimation = ObjectAnimator.ofFloat(diceImage, "scaleX", 1f, 1.2f, 1f, 1.1f, 1f).apply {
            duration = 1500
            interpolator = AccelerateDecelerateInterpolator()
        }

        val scaleYAnimation = ObjectAnimator.ofFloat(diceImage, "scaleY", 1f, 1.2f, 1f, 1.1f, 1f).apply {
            duration = 1500
            interpolator = AccelerateDecelerateInterpolator()
        }

        // 알파 애니메이션으로 반짝임 효과
        val alphaAnimation = ObjectAnimator.ofFloat(diceImage, "alpha", 1f, 0.7f, 1f, 0.8f, 1f).apply {
            duration = 1500
            interpolator = AccelerateDecelerateInterpolator()
        }

        // 모든 애니메이션 동시 실행
        bounceAnimation.start()
        rotateAnimation.start()
        scaleXAnimation.start()
        scaleYAnimation.start()
        alphaAnimation.start()

        // 두 번째 애니메이션 사이클
        Handler(Looper.getMainLooper()).postDelayed({
            val secondBounce = ObjectAnimator.ofFloat(diceImage, "translationY", 0f, -80f, 0f, -40f, 0f).apply {
                duration = 1200
                interpolator = AccelerateDecelerateInterpolator()
            }

            val secondRotate = ObjectAnimator.ofFloat(diceImage, "rotation", 360f, 720f).apply {
                duration = 1200
                interpolator = AccelerateDecelerateInterpolator()
            }

            secondBounce.start()
            secondRotate.start()
        }, 1500)
    }

    private fun showResult(travelType: String) {
        // 로딩 화면 숨기기
        loadingLayout.visibility = View.GONE

        // 결과 화면 표시
        resultCard.visibility = View.VISIBLE

        // 랜덤 여행지 선택
        val destinationList = destinations[travelType] ?: destinations["상관없음"]!!
        val randomDestination = destinationList[Random.nextInt(destinationList.size)]

        // 결과 표시
        resultTitle.text = "🎲 당신이 찾던 여행지입니다!"
        destinationText.text = "📍 ${randomDestination.name}"
        weatherText.text = "날씨 정보를 가져오는 중..."
        budgetText.text = "💰 예상 경비: ${randomDestination.budget}"
        durationText.text = "📅 추천 기간: ${randomDestination.duration}"
        tipText.text = randomDestination.tip

        // 날씨 정보 가져오기
        fetchWeatherData(randomDestination)

        // 결과 카드 애니메이션
        resultCard.alpha = 0f
        resultCard.scaleX = 0.8f
        resultCard.scaleY = 0.8f

        resultCard.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    private fun fetchWeatherData(destination: Destination) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val weatherData = weatherRepository.getWeatherData(
                    destination.cityName,
                    destination.countryCode,
                    API_KEY
                )

                withContext(Dispatchers.Main) {
                    updateWeatherUI(weatherData)
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching weather", e)
                withContext(Dispatchers.Main) {
                    weatherText.text = "날씨: ${destination.weather}"
                    temperatureText.text = "정보 없음"
                    humidityText.text = "습도: --"
                    windSpeedText.text = "바람: --"
                }
            }
        }
    }

    private fun updateWeatherUI(weatherData: WeatherData?) {
        if (weatherData != null) {
            weatherText.text = "${weatherData.description}"
            temperatureText.text = "${weatherData.temperature.toInt()}°C"
            humidityText.text = "습도: ${weatherData.humidity}%"
            windSpeedText.text = "바람: ${String.format("%.1f", weatherData.windSpeed)}m/s"

            // 날씨 아이콘 설정 (선택사항)
            setWeatherIcon(weatherData.iconCode)
        } else {
            weatherText.text = "날씨 정보를 가져올 수 없습니다"
            temperatureText.text = "정보 없음"
            humidityText.text = "습도: --"
            windSpeedText.text = "바람: --"
        }
    }

    private fun setWeatherIcon(iconCode: String) {
        // 날씨 아이콘 설정
        val iconResource = when (iconCode) {
            "01d" -> R.drawable.ic_sunny_day          // 맑음 (낮)
            "01n" -> R.drawable.ic_clear_night        // 맑음 (밤)
            "02d" -> R.drawable.ic_partly_cloudy_day  // 구름조금 (낮)
            "02n" -> R.drawable.ic_partly_cloudy_night // 구름조금 (밤)
            "03d", "03n", "04d", "04n" -> R.drawable.ic_cloudy      // 구름많음
            "09d", "09n" -> R.drawable.ic_drizzle     // 이슬비
            "10d" -> R.drawable.ic_rain_day           // 비 (낮)
            "10n" -> R.drawable.ic_rain_night         // 비 (밤)
            "11d", "11n" -> R.drawable.ic_thunderstorm // 뇌우
            "13d", "13n" -> R.drawable.ic_snow        // 눈
            "50d", "50n" -> R.drawable.ic_mist        // 안개
            else -> R.drawable.ic_default_weather     // 기본 아이콘
        }

        weatherIcon.setImageResource(iconResource)
    }
}