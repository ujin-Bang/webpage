package com.restart.webpage

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.ContentLoadingProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    private val progressBar: ContentLoadingProgressBar by lazy {
        findViewById(R.id.progressBar)
    }

    private val goHomeButton: ImageButton by lazy {
        findViewById(R.id.goHomeButton)
    }

    private val goBackButton: ImageButton by lazy {
        findViewById(R.id.goBackButton)
    }

    private val goForwardButton: ImageButton by lazy {
        findViewById(R.id.goForwardButton)
    }

    private val webView: WebView by lazy {
        findViewById(R.id.webView)
    }

    private val refreshLayout: SwipeRefreshLayout by lazy {
        findViewById(R.id.refreshLayout)
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

    //핸드폰 하단 백버튼 눌렀을 때 상황에 맞게 액티비티 종료되게
    override fun onBackPressed() {
        if (webView.canGoBack()){//히스토리에 액티비티가 쌓여있어서 뒤로 갈 수 있다면
            webView.goBack()// 뒤로가기
        }else{
            super.onBackPressed() //히스토리에 액티비티가 없다면 액티비티 종료
        }



    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews(){
        webView.apply {
            webViewClient = WebViewClient() //디폴트 웹브라우저를 우리가 지정한 웹뷰로 지정.
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true // 웹뷰로 웹페이지 접속시 자바스크립트도 사용하겠다.
            loadUrl(DEFAULT_URL)
        }
    }

    private fun bindViews(){

        goHomeButton.setOnClickListener {
            webView.loadUrl(DEFAULT_URL)
        }
        //주소입력창 에디트텍스트 세팅
        addressBar.setOnEditorActionListener { v, actionId, event ->
            //주소창에 주소 입력후 에뮬레이터 우측하단 DONE버튼 눌리면(xml에서 imeOptions="actionDone"속성 작성해둠)
            if (actionId == EditorInfo.IME_ACTION_DONE){
                //Done버튼이 눌리면 http 또는 https 가있는지 확인해서 http를 붙이지 않았더라도 붙여주는 코드.
                val loadingUrl = v.text.toString()
                if (URLUtil.isNetworkUrl(loadingUrl)){
                    webView.loadUrl(loadingUrl)
                }else {
                    webView.loadUrl("http://$loadingUrl")
                }
                webView.loadUrl(v.text.toString())//웹뷰에 입력한 주소를 보여줘라.
            }

            return@setOnEditorActionListener false //키보드를 내려야 하기 때문에 false반환
        }

        //백버튼클릭시 뒤로가기
        goBackButton.setOnClickListener {
            webView.goBack()
        }
        //포워드버튼 클릭시 앞으로가기
        goForwardButton.setOnClickListener {
            webView.goForward()
        }
            //리프레쉬 레이아웃 새로고침.
        refreshLayout.setOnRefreshListener {
            webView.reload()
        }

    }


    inner class WebViewClient: android.webkit.WebViewClient(){

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            progressBar.show() //페이지가 시작될 때 프로그래스바(진행률표시바)가 나타남
        }

        //페이지 로딩이 끝났을 때
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

          refreshLayout.isRefreshing = false //새로고침 아이콘 사라지게
            progressBar.hide() //로딩이 끝났을 때 프로그래스바(진행률표시바) 사라짐
            goBackButton.isEnabled = webView.canGoBack()// 백버튼의 히스토리가 있을때만 뒤로가기 버튼이 눌리게.
            goForwardButton.isEnabled = webView.canGoForward()// 앞으로 갈 수 있을때만 앞으로가기 버튼이 눌리게.
            addressBar.setText(url)//최종적으로 로딩된 주소가 전달됨.
        }

    }

    inner class WebChromeClient: android.webkit.WebChromeClient(){

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)

        progressBar.progress = newProgress
        }
    }

    companion object {
        private const val DEFAULT_URL = "http://www.google.com"
    }
}