package com.jiyeon.easypicktrip

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Layout
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var titleText: TextView
    private lateinit var subtitleText: TextView
    private lateinit var travelTypeGroup: RadioGroup
    private lateinit var travelLayout: LinearLayout
    private lateinit var relaxationRadio: RadioButton
    private lateinit var sightseeingRadio: RadioButton
    private lateinit var anywhereRadio: RadioButton
    private lateinit var adventureRadio: RadioButton
    private lateinit var cultureRadio: RadioButton
    private lateinit var selectButton: Button
    private lateinit var diceImage: ImageView
    private lateinit var resultCard: CardView
    private lateinit var resultTitle: TextView
    private lateinit var destinationText: TextView
    private lateinit var weatherText: TextView
    private lateinit var budgetText: TextView
    private lateinit var durationText: TextView
    private lateinit var tipText: TextView
    private lateinit var retryButton: Button

    private val destinations = mapOf(
        "휴양" to listOf(
            Destination("몰디브", "맑음, 28-32°C", "200-400만원", "5-7일", "🏖️ 스노클링 장비를 꼭 챙기세요!"),
            Destination("발리", "맑음, 26-30°C", "80-150만원", "4-6일", "🌺 우기는 피하고 건기에 방문하세요!"),
            Destination("푸켓", "맑음, 27-31°C", "100-200만원", "5-7일", "🏝️ 피피섬 투어를 놓치지 마세요!"),
            Destination("제주도", "맑음, 20-25°C", "30-80만원", "3-4일", "🌊 렌터카로 해안도로 드라이브 추천!"),
            Destination("하와이", "맑음, 24-28°C", "300-500만원", "7-10일", "🌺 선크림을 충분히 발라주세요!"),
            Destination("칸쿤", "맑음, 25-29°C", "250-400만원", "6-8일", "🏖️ 세노테에서 다이빙 체험해보세요!")
        ),
        "관광" to listOf(
            Destination("파리", "흐림, 15-20°C", "200-350만원", "5-7일", "🗼 에펠탑은 야경이 가장 아름다워요!"),
            Destination("도쿄", "맑음, 18-23°C", "150-250만원", "4-6일", "🍣 츠키지 시장 새벽 참치 경매 구경하세요!"),
            Destination("뉴욕", "구름많음, 12-18°C", "300-500만원", "6-8일", "🗽 브로드웨이 뮤지컬 예약은 미리!"),
            Destination("로마", "맑음, 16-22°C", "180-300만원", "4-6일", "🏛️ 콜로세움은 오전 일찍 가세요!"),
            Destination("런던", "비, 10-16°C", "250-400만원", "5-7일", "☂️ 우산은 필수! 대영박물관 추천!"),
            Destination("바르셀로나", "맑음, 19-24°C", "180-280만원", "4-6일", "🏛️ 사그라다 파밀리아 사전 예약 필수!")
        ),
        "상관없음" to listOf(
            Destination("싱가포르", "맑음, 26-30°C", "120-200만원", "3-5일", "🌃 마리나 베이 샌즈 야경이 환상적!"),
            Destination("베트남 다낭", "맑음, 24-28°C", "60-120만원", "4-6일", "🍜 쌀국수와 바인미 맛집 투어!"),
            Destination("체코 프라하", "맑음, 14-19°C", "100-180만원", "4-5일", "🏰 구시가지 천문시계 정각에 구경하세요!"),
            Destination("터키 이스탄불", "맑음, 17-22°C", "120-200만원", "5-7일", "🕌 블루모스크와 아야소피아 필수 코스!"),
            Destination("아이슬란드", "흐림, 8-12°C", "200-350만원", "6-8일", "🌌 오로라 관측은 9월-3월이 최적!"),
            Destination("페루 쿠스코", "맑음, 15-20°C", "150-250만원", "7-10일", "🏔️ 고산병 예방약 미리 복용하세요!")
        ),
        "액티비티" to listOf(
            Destination("네팔 포카라", "맑음, 10-18°C", "100-200만원", "8-12일", "🏔️ 히말라야 트레킹 장비 대여 가능!"),
            Destination("뉴질랜드 퀸스타운", "맑음, 12-18°C", "250-400만원", "7-10일", "🪂 번지점프 발상지에서 도전해보세요!"),
            Destination("페루 마추픽추", "맑음, 15-20°C", "200-300만원", "8-10일", "🥾 잉카 트레일 예약은 6개월 전에!"),
            Destination("코스타리카", "맑음, 22-28°C", "180-280만원", "7-9일", "🦋 생태관광의 천국! 가이드 투어 추천!"),
            Destination("몽골 고비사막", "맑음, 20-35°C", "150-250만원", "6-8일", "🐪 낙타 트레킹과 유목민 체험!"),
            Destination("요단 와디럼", "맑음, 25-35°C", "200-300만원", "5-7일", "🌌 사막에서 별 관측이 압권!")
        ),
        "문화" to listOf(
            Destination("인도 라자스탄", "맑음, 20-30°C", "80-150만원", "8-10일", "🏰 궁전 호텔에서 하룻밤 경험해보세요!"),
            Destination("이집트 카이로", "맑음, 18-25°C", "120-200만원", "6-8일", "🏺 피라미드 내부 투어 예약 필수!"),
            Destination("그리스 아테네", "맑음, 18-25°C", "150-250만원", "5-7일", "🏛️ 아크로폴리스는 일몰 시간이 최고!"),
            Destination("일본 교토", "맑음, 16-22°C", "120-200만원", "4-6일", "🌸 기온 지구에서 게이샤 만날 수 있어요!"),
            Destination("멕시코 유카탄", "맑음, 24-30°C", "150-250만원", "6-8일", "🗿 치첸이트사 마야 유적지 탐험!"),
            Destination("캄보디아 앙코르", "맑음, 26-32°C", "80-150만원", "4-6일", "🏛️ 앙코르와트 일출 투어 놓치지 마세요!")
        )
    )

    data class Destination(
        val name: String,
        val weather: String,
        val budget: String,
        val duration: String,
        val tip: String
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
        travelTypeGroup = findViewById(R.id.travelTypeGroup)
        travelLayout = findViewById(R.id.travelLayout)
        relaxationRadio = findViewById(R.id.relaxationRadio)
        sightseeingRadio = findViewById(R.id.sightseeingRadio)
        anywhereRadio = findViewById(R.id.anywhereRadio)
        adventureRadio = findViewById(R.id.adventureRadio)
        cultureRadio = findViewById(R.id.cultureRadio)
        selectButton = findViewById(R.id.selectButton)
        diceImage = findViewById(R.id.diceImage)
        resultCard = findViewById(R.id.resultCard)
        resultTitle = findViewById(R.id.resultTitle)
        destinationText = findViewById(R.id.destinationText)
        weatherText = findViewById(R.id.weatherText)
        budgetText = findViewById(R.id.budgetText)
        durationText = findViewById(R.id.durationText)
        tipText = findViewById(R.id.tipText)
        retryButton = findViewById(R.id.retryButton)
    }

    private fun setupClickListeners() {
        selectButton.setOnClickListener {
            val selectedType = getSelectedTravelType()
            if (selectedType != null) {
                showResult(selectedType)
                //showDiceAnimation(selectedType)
            }
        }

        retryButton.setOnClickListener {
            showInitialScreen()
        }
    }

    private fun getSelectedTravelType(): String? {
        return when (travelTypeGroup.checkedRadioButtonId) {
            R.id.relaxationRadio -> "휴양"
            R.id.sightseeingRadio -> "관광"
            R.id.anywhereRadio -> "상관없음"
            R.id.adventureRadio -> "모험"
            R.id.cultureRadio -> "문화"
            else -> null
        }
    }

    private fun showInitialScreen() {
        // 초기 화면 표시
        titleText.visibility = View.VISIBLE
        subtitleText.visibility = View.VISIBLE
        travelTypeGroup.visibility = View.VISIBLE
        travelLayout.visibility = View.VISIBLE
        selectButton.visibility = View.VISIBLE

        // 다른 화면 숨기기
        diceImage.visibility = View.GONE
        resultCard.visibility = View.GONE

        // 라디오 버튼 초기화
        travelTypeGroup.clearCheck()
    }

    private fun showDiceAnimation(travelType: String) {
        // 초기 화면 숨기기
        titleText.visibility = View.GONE
        subtitleText.visibility = View.GONE
        travelTypeGroup.visibility = View.GONE
        travelLayout.visibility = View.GONE
        selectButton.visibility = View.GONE

        // 주사위 애니메이션 표시
        diceImage.visibility = View.VISIBLE

        // 주사위 회전 애니메이션
        val rotationX = PropertyValuesHolder.ofFloat(View.ROTATION_X, 0f, 360f)
        val rotationY = PropertyValuesHolder.ofFloat(View.ROTATION_Y, 0f, 360f)
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.5f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.5f, 1f)

        val animator = ObjectAnimator.ofPropertyValuesHolder(
            diceImage, rotationX, rotationY, scaleX, scaleY
        ).apply {
            duration = 2000
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = 2
        }

        animator.start()

        // 애니메이션 후 결과 표시
        Handler(Looper.getMainLooper()).postDelayed({
            showResult(travelType)
        }, 6000)
    }

    private fun showResult(travelType: String) {
        // 주사위 숨기기
        diceImage.visibility = View.GONE

        // 결과 화면 표시
        resultCard.visibility = View.VISIBLE

        // 랜덤 여행지 선택
        val destinationList = destinations[travelType] ?: destinations["상관없음"]!!
        val randomDestination = destinationList[Random.nextInt(destinationList.size)]

        // 결과 표시
        resultTitle.text = "🎲 당신이 찾던 여행지입니다!"
        destinationText.text = "📍 ${randomDestination.name}"
        weatherText.text = "🌤️ 날씨: ${randomDestination.weather}"
        budgetText.text = "💰 예상 경비: ${randomDestination.budget}"
        durationText.text = "📅 추천 기간: ${randomDestination.duration}"
        tipText.text = "${randomDestination.tip}"

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
}