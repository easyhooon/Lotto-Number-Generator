package kr.ac.konkuk.lotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {


    private val btn_clear: Button by lazy {
        findViewById<Button>(R.id.btn_clear)
    }

    private val btn_add: Button by lazy {
        findViewById<Button>(R.id.btn_add)
    }

    private val btn_run: Button by lazy {
        findViewById<Button>(R.id.btn_run)
    }

    private val numberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker)
    }

    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById<TextView>(R.id.tv_number1),
            findViewById<TextView>(R.id.tv_number2),
            findViewById<TextView>(R.id.tv_number3),
            findViewById<TextView>(R.id.tv_number4),
            findViewById<TextView>(R.id.tv_number5),
            findViewById<TextView>(R.id.tv_number6)
        )
    }

    private var didRun = false

    private val pickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //numberPicker를 작동시키려면 min, max값을 설정해주어야한다.
        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()
    }

    private fun initRunButton() {
        btn_run.setOnClickListener {
            val list = getRandomNumber()

            didRun = true

            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]

                textView.text = number.toString()
                textView.isVisible = true

                setNumberBackgroud(number, textView)
            }
//            Log.d("MainActivity", "${list.toString()}")
        }
    }

    private fun initAddButton() {
        btn_add.setOnClickListener {
            if (didRun) {
                Toast.makeText(this, "초기화 후에 시도해주세요. ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
                //다음이 실행되지 않도록 setOnClickListener자체를 리턴 (setOnClickListener안에 있는 function만 리턴해주기 위해서 표시)
            }

            if (pickNumberSet.size >= 5) {
                Toast.makeText(this, "번호는 5개까지만 선택할 수 있습니다. ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //contains는 set 안에서도 호출가능
            if (pickNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호입니다. ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = numberTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker.value.toString()

            //setBackground
            //drawable값을 가져오는 방법: drawable이 안드로이드 앱에 저장된 것이기 때문에 context에서 가져와야한다.

            setNumberBackgroud(numberPicker.value, textView)

            pickNumberSet.add(numberPicker.value)
        }
    }

    private fun setNumberBackgroud(number: Int, textView: TextView) {
        when (number) {
            //.. 점 세개 아니고 두개인다
            in 1..10 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
        }
    }

    private fun initClearButton() {
        btn_clear.setOnClickListener {
            pickNumberSet.clear()

            //앞에서부터 하나하나 꺼내서 실행시켜주는 블록 함수
            numberTextViewList.forEach() {
                it.isVisible = false
            }
            didRun = false
        }
    }

    private fun getRandomNumber(): List<Int> {
        //list에 1부터 45까지의 숫자를 넣어주는 것을 '적용'함(주로 초기화를 하거나, 데이터를 미리 넣어놓을때 사용)
        val numberList = mutableListOf<Int>().apply {
            for (i in 1..45) {
                if (pickNumberSet.contains(i)) {
                    continue
                }

                this.add(i);
            }
        }
        numberList.shuffle()
        //이미 선택된 set를 리스트로 변경 + 나머지 번호의 갯수만큼 들어있는 리스트를 뒤에 붙혀줌
        val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)

        //정렬
        return newList.sorted()
    }
}
