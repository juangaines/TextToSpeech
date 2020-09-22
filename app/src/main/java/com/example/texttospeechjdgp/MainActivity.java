package com.example.texttospeechjdgp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.mlsdk.common.MLApplication;
import com.huawei.hms.mlsdk.tts.MLTtsAudioFragment;
import com.huawei.hms.mlsdk.tts.MLTtsCallback;
import com.huawei.hms.mlsdk.tts.MLTtsConfig;
import com.huawei.hms.mlsdk.tts.MLTtsConstants;
import com.huawei.hms.mlsdk.tts.MLTtsEngine;
import com.huawei.hms.mlsdk.tts.MLTtsError;
import com.huawei.hms.mlsdk.tts.MLTtsWarn;

public class MainActivity extends AppCompatActivity {


    private MLTtsConfig mlConfigs;
    private MLTtsEngine mlTtsEngine;

    private Button buttonTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonTest =findViewById(R.id.button);
        String apiKey = AGConnectServicesConfig.fromContext(this).getString("client/api_key");
        MLApplication.getInstance().setApiKey(apiKey);
        mlConfigs = new MLTtsConfig()
                .setLanguage(MLTtsConstants.TTS_LAN_ES_ES)
                .setPerson(MLTtsConstants.TTS_SPEAKER_FEMALE_ES)
                .setSpeed(1.0f)
                .setVolume(1.0f);

        mlTtsEngine = new MLTtsEngine(mlConfigs);

        mlTtsEngine.setTtsCallback(setCallbackConfiguration());

        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlTtsEngine.speak("Probando 1 2 3 1 2 3 probando", MLTtsEngine.QUEUE_APPEND);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //mlTtsEngine.speak("Probando 1 2 3 1 2 3 probando", MLTtsEngine.QUEUE_APPEND);
            }
        },300);


    }



    private MLTtsCallback setCallbackConfiguration() {

        MLTtsCallback callback = new MLTtsCallback() {
            @Override
            public void onError(String taskId, MLTtsError err) {
                // Processing logic for TTS failure.
            }

            @Override
            public void onWarn(String taskId, MLTtsWarn warn) {
                // Alarm handling without affecting service logic.
            }

            @Override
            // Return the mapping between the currently played segment and text. start: start position of the audio segment in the input text; end (excluded): end position of the audio segment in the input text.
            public void onRangeStart(String taskId, int start, int end) {
                // Process the mapping between the currently played segment and text.
            }

            @Override
            // taskId: ID of an audio synthesis task corresponding to the audio.
            // audioFragment: audio data.
            // offset: offset of the audio segment to be transmitted in the queue. One audio synthesis task corresponds to an audio synthesis queue.
            // range: text area where the audio segment to be transmitted is located; range.first (included): start position; range.second (excluded): end position.
            public void onAudioAvailable(String taskId, MLTtsAudioFragment audioFragment, int offset, Pair<Integer, Integer> range,
                                         Bundle bundle) {
                // Audio stream callback API, which is used to return the synthesized audio data to the app.
            }

            @Override
            public void onEvent(String taskId, int eventId, Bundle bundle) {
                // Callback method of an audio synthesis event. eventId: event name.
                boolean isInterrupted;
                switch (eventId) {
                    case MLTtsConstants.EVENT_PLAY_START:
                        // Called when playback starts.
                        break;
                    case MLTtsConstants.EVENT_PLAY_STOP:
                        // Called when playback stops.
                        isInterrupted = bundle.getBoolean(MLTtsConstants.EVENT_PLAY_STOP_INTERRUPTED);
                        break;
                    case MLTtsConstants.EVENT_PLAY_RESUME:
                        // Called when playback resumes.
                        break;
                    case MLTtsConstants.EVENT_PLAY_PAUSE:
                        // Called when playback pauses.
                        break;
                    // Pay attention to the following callback events when you focus on only synthesized audio data but do not use the internal player for playback:
                    case MLTtsConstants.EVENT_SYNTHESIS_START:
                        // Called when audio synthesis starts.
                        break;
                    case MLTtsConstants.EVENT_SYNTHESIS_END:
                        // Called when audio synthesis ends.
                        break;
                    case MLTtsConstants.EVENT_SYNTHESIS_COMPLETE:
                        // Audio synthesis is complete. All synthesized audio streams are passed to the app.
                        isInterrupted = bundle.getBoolean(MLTtsConstants.EVENT_SYNTHESIS_INTERRUPTED);
                        break;
                    default:
                        break;
                }
            }


        };

        return callback;
    }
}