package com.sample.tambolanumberpicker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.tambolanumberpicker.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class MainActivity : BaseActivityWithJob() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var numberAdapter: NumberAdapter
    private lateinit var currentNumberAdapter: CurrentNumberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val numberList = MutableList(90) {
            TambolaNumber((it + 1).toString())
        }

        numberAdapter = NumberAdapter(numberList)
        currentNumberAdapter = CurrentNumberAdapter()

        binding.numberHistoryRecyclerView.layoutManager = GridLayoutManager(this, 10, RecyclerView.VERTICAL, false)
        binding.numberHistoryRecyclerView.adapter = numberAdapter

        binding.currentNumberRecyclerView.adapter = currentNumberAdapter

        binding.nextButton.setOnClickListener {
            binding.resetButton.visibility = View.VISIBLE
            binding.nextButton.text = "next"
            val remainingNumbers = numberAdapter.remainingNumbers()
            val size = remainingNumbers.size
            val nextInt: String = remainingNumbers[Random.nextInt(0, size)].number
            currentNumberAdapter.addNumber(nextInt)
            doWithDelayOnMainThread(300) {
                binding.currentNumberRecyclerView.smoothScrollToPosition(currentNumberAdapter.itemCount - 1)
            }
            numberAdapter.setCalledFor(nextInt)
            if (size == 1)
                binding.nextButton.visibility = View.GONE
        }

        binding.resetButton.setOnClickListener {
            reset()
            binding.nextButton.visibility = View.VISIBLE
            binding.nextButton.text = "start"
            binding.resetButton.visibility = View.GONE
        }
    }

    fun reset() {
        currentNumberAdapter.reset()
        numberAdapter.resetNumbers()
    }
}

class TambolaNumber(val number: String) {
    var isCalled = false
}

inline fun CoroutineScope.doWithDelayOnMainThread(delayInMillis: Long, crossinline block: () -> Unit) {
    launch {
        delay(delayInMillis)
        withContext(Dispatchers.Main) {
            block()
        }
    }
}


open class BaseActivityWithJob : AppCompatActivity(), CoroutineScope {
    lateinit var mJob: Job
    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mJob = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        mJob.cancel()
    }
}