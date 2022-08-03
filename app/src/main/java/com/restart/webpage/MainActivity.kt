package com.restart.webpage

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private val webView: WebView by lazy {
        findViewById(R.id.webView)
    }

    private val addressBar: EditText by lazy {
        findViewById(R.id.addressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        bindViews()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews(){
        webView.webViewClient = WebViewClient() //디폴트 웹브라우저를 우리가 지정한 웹뷰로 지정.
        webView.settings.javaScriptEnabled = true // 웹뷰로 웹페이지 접속시 자바스크립트도 사용하겠다.
        webView.loadUrl("http://www.google.com")
    }

    private fun bindViews(){
        //주소입력창 에디트텍스트 세팅
        addressBar.setOnEditorActionListener { v, actionId, event ->
            //주소창에 주소 입력후 에뮬레이터 우측하단 DONE버튼 눌리면(xml에서 imeOptions="actionDone"속성 작성해둠)
            if (actionId == EditorInfo.IME_ACTION_DONE){
                webView.loadUrl(v.text.toString())//웹뷰에 입력한 주소를 보여줘라.
            }

            return@setOnEditorActionListener false //키보드를 내려야 하기 때문에 false반환
        }

    }
}