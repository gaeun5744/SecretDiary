package com.example.aop_part2_chapter03

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity:AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    // 메인 스레드에 연결된 핸들러 생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val diaryEditText=findViewById<EditText>(R.id.diaryEditText)
        val detailPreferences=getSharedPreferences("dirary", Context.MODE_PRIVATE)

        diaryEditText.setText(detailPreferences.getString("detail",""))

        val runnable= Runnable {
            getSharedPreferences("diary",Context.MODE_PRIVATE).edit {
                putString("detail",diaryEditText.text.toString())
            }

            Log.d("DiaryActivity","SAVE!!!! ${diaryEditText.text.toString()}")
        }

        // 이렇게 하면 너무 빈번하게, 한자한자 할때마다 저장하기 때문에
        // 잠깐 멈칫했을때 저장하는 걸로 변경하기
        // 스레드를 열었을때 UI에서 처리되는 스레드를 UI스레드 혹은 메인 스레드라고 함
        // 새로운 스레드를 열었을 때 새로운 스레드는 UI 스레드가 아님
        // UI 스레드와 새로운 스레드를 연결 시켜줄 필요가 있는 경우가 있음
        // 메인 스레드가 아닌 곳에서는 ui change를 하는 동작을 수행할 수 없기 때문
        // 메인 스레드와 연결시켜주는 기능을 핸들러에서 많이 사용


        diaryEditText.addTextChangedListener {

            Log.d("DiaryActivity","TextChanged::$it")
            handler.removeCallbacks(runnable)
            // 0.5초 이전에 실행되지 않고 팬딩되어 있는 runnable이 있으면 지우기 위해 사용
            // 0.5초 이내 새로운 textchange가 일어나지 않으면 runnable 실행됨
            handler.postDelayed(runnable,500)

        }


    }
}