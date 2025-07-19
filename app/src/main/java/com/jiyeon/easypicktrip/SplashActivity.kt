package com.jiyeon.easypicktrip

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private var mainContainer: LinearLayout? = null
    private var logoText: TextView? = null
    private var loadingText: TextView? = null
    private var loadingProgress: ProgressBar? = null
    private lateinit var floatingIcons: Array<TextView>

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initViews()
        startAnimations()
        startProgressAnimation()
        navigateToMainActivity()
    }

    private fun initViews() {
        mainContainer = findViewById(R.id.main_container)
        logoText = findViewById(R.id.logo_text)
        loadingText = findViewById(R.id.loading_text)
        loadingProgress = findViewById(R.id.loading_progress)

        floatingIcons = arrayOf(
            findViewById(R.id.floating_icon_1),
            findViewById(R.id.floating_icon_2),
            findViewById(R.id.floating_icon_3),
            findViewById(R.id.floating_icon_4),
            findViewById(R.id.floating_icon_5),
            findViewById(R.id.floating_icon_6)
        )
    }

    private fun startAnimations() {
        // 메인 컨테이너 페이드인 + 위로 슬라이드
        val fadeInUp = AnimationUtils.loadAnimation(this, R.anim.fade_in_up)
        mainContainer!!.startAnimation(fadeInUp)

        // 로고 바운스 애니메이션
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)
        logoText!!.startAnimation(bounce)

        // 로딩 텍스트 페이드인 (1초 후)
        handler.postDelayed({
            val fadeIn =
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.fade_in
                )
            loadingText!!.startAnimation(fadeIn)
            loadingText!!.alpha = 1f
        }, 1000)

        // 떠다니는 아이콘 애니메이션
        val floatAnim = AnimationUtils.loadAnimation(this, R.anim.floating)
        for (i in floatingIcons.indices) {
            val index = i
            handler.postDelayed({
                floatingIcons[index].startAnimation(floatAnim)
            }, (i * 500).toLong()) // 순차적으로 시작
        }
    }

    private fun startProgressAnimation() {
        // 프로그레스 바 애니메이션
        val progressAnimator = ObjectAnimator.ofInt(loadingProgress, "progress", 0, 100)
        progressAnimator.setDuration(SPLASH_DURATION.toLong())
        progressAnimator.start()

        // 진행률에 따른 텍스트 변경
        handler.postDelayed({
            loadingText!!.text = "여행지 정보를 불러오고 있어요..."
        }, 1000)

        handler.postDelayed({
            loadingText!!.text = "거의 다 됐어요!"
        }, 2000)

        handler.postDelayed({
            loadingText!!.text = "준비 완료!"
        }, 2500)
    }

    private fun navigateToMainActivity() {
        handler.postDelayed({
            val intent =
                Intent(
                    this@SplashActivity,
                    MainActivity::class.java
                )
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, SPLASH_DURATION.toLong())
    }

    companion object {
        private const val SPLASH_DURATION = 3000 // 3초
    }
}