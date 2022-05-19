package com.kjy.recorderproject

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private val resetButton: Button by lazy {
        findViewById(R.id.resetButton)
    }

    private val recordButton: RecordButton by lazy {
        findViewById(R.id.recordButton)
    }

    private val requiredPermssions = arrayOf(Manifest.permission.RECORD_AUDIO,
                                            Manifest.permission.READ_EXTERNAL_STORAGE)


    private val recordingFilePath: String by lazy {
       "${externalCacheDir?.absolutePath}/recording.3gp"
    }

    // recorder 프로퍼티 설정
    private var recorder: MediaRecorder? = null

    // player 프로퍼티 설정
    private var player: MediaPlayer? = null

    // 초기 상태 구현
    private var state = State.BEFORE_RECORDING
    set(value) {
        field = value
        resetButton.isEnabled = (value == State.AFTER_RECORDING)
                                || (value ==  State.ON_PLAYING)
        recordButton.updateIconWithState(value)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestAudioPermission()
        initView()
        bindView()
        initVariables()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // 요청한 권한에 대한 결과를 받음
        val audioRecordPermissionGranted = requestCode == Request_RECORD_AUDIO_PEMISSION &&
                grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        // 권한을 승인하지 않으면 앱을 종료함.
        if(!audioRecordPermissionGranted) {
            finish()
        }
    }

    // 권한 요청을 위한 선언
    private fun requestAudioPermission() {
       requestPermissions(requiredPermssions, Request_RECORD_AUDIO_PEMISSION)
    }

    fun initView() {
        recordButton.updateIconWithState(state)
    }
    private fun bindView() {
        resetButton.setOnClickListener {
            stopPlaying()
            state = State.BEFORE_RECORDING
        }
        recordButton.setOnClickListener {
            when(state) {
                State.BEFORE_RECORDING -> {
                    startRecording()
                }
                State.ON_RECORDING -> {
                    stopRecording()
                }
                State.AFTER_RECORDING -> {
                    startPlaying()
                }
                State.ON_PLAYING -> {
                    stopPlaying()
                }
            }
        }
    }

    private fun initVariables() {
        state = State.BEFORE_RECORDING
    }

    // 녹음할 수 있는 상태를 위한 메서드
    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordingFilePath)
            prepare()
        }
        recorder?.start()
        state = State.ON_RECORDING
    }

    // 녹음 정지 메서드
    private fun stopRecording() {
        recorder?.run {
            stop()
            release()
        }
        recorder = null
        state = State.AFTER_RECORDING
    }

    // 재생 기능 메서드
    private fun startPlaying() {
        player = MediaPlayer()
            .apply {
                setDataSource(recordingFilePath)
                prepare()
            }
        player?.start()
        state = State.ON_PLAYING
    }

    // 재생 멈춤 메서드
    private fun stopPlaying() {
        player?.release()
        player = null
        state = State.AFTER_RECORDING
    }

    companion object {
        private const val Request_RECORD_AUDIO_PEMISSION = 201
    }
}