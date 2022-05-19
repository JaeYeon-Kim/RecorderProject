package com.kjy.recorderproject

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton

class RecordButton (
    context: Context,
    attrs: AttributeSet
        ): AppCompatImageButton(context, attrs) {

            fun updateIconWithState(state: State) {
                // 각각의 녹음 State에 맞게 drawable 이미지 부여
                // enum 클래스로 정의했기 때문에 when으로 상태 구현
                when(state) {
                    State.BEFORE_RECORDING -> {
                        setImageResource(R.drawable.ic_record)
                    }

                    State.ON_RECORDING -> {
                        setImageResource(R.drawable.ic_stop)
                    }
                    State.AFTER_RECORDING ->  {
                        setImageResource(R.drawable.ic_play)
                    }
                    State.ON_PLAYING -> {
                        setImageResource(R.drawable.ic_stop)
                    }
                }
            }
        }