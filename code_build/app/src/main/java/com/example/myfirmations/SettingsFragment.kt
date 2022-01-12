package com.example.myfirmations

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myfirmations.*
import com.example.myfirmations.databinding.FragmentSettingsBinding
import com.example.myfirmations.data.Settings

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment(), TextToSpeech.OnInitListener,
    AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private var binding: FragmentSettingsBinding? = null
    private var allowSpeaking: Boolean = true
    private var scrollSpeed: Float = 0f
    var spnChooseVoice: Spinner? = null
    lateinit var settings: Settings
    private var tts: TextToSpeech? = null
    var selectedVoice: String = ""
    var firmId:Int = 0
    val args: SettingsFragmentArgs by navArgs()
    var voiceChanged = false
    var scrollPos:Int =0
    var btnUpdate: Button? = null

    private val settingsViewModel: SettingsViewModel by activityViewModels {
        SettingsViewModelFactory(
            (activity?.application as FirmApplication)
                .database.settingsDao()
        )
    }

    private val firmViewModel: FirmViewModel by activityViewModels {
        FirmViewModelFactory(
            (activity?.application as FirmApplication)
                .database.firmDao()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        tts = TextToSpeech(context, this, "com.google.android.tts")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding = fragmentSettingsBinding
        return fragmentSettingsBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sldSpeed = binding?.sldScrollSpeed
        val swtAllowSpeak = binding?.swtAllowVoice
        spnChooseVoice= binding?.spnSelectVoice
        btnUpdate = binding?.btnUpdate
        val btnCancel = binding?.btnCancel

        allowSpeaking = args.allowSpeaking
        scrollSpeed = args.scrollSpeed
        selectedVoice= args.selectedVoice
        val firmScrollPos = firmViewModel.getOrdinalPos()-1

        if (firmScrollPos==0)
                scrollPos = 0
        else
             scrollPos = firmViewModel.getOrdinalPos()-1
        Log.d("Firms", "CurrentSettings:::Speed:"+scrollSpeed.toString()+";Speak:"+allowSpeaking.toString()+";Voice:"+selectedVoice)


        sldSpeed!!.value = scrollSpeed

        swtAllowSpeak!!.setChecked(allowSpeaking)
        ArrayAdapter.createFromResource(
            view.context,
            R.array.allVoices,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spnChooseVoice!!.adapter = adapter
        }

        var spnPos=0
        val stringArray = getResources().getStringArray(R.array.allVoices);
        for (i in 0..stringArray.size-1) {
            if (stringArray[i].equals(selectedVoice))
                spnPos = i

        }
        spnChooseVoice!!.setSelection(spnPos);

        spnChooseVoice!!.onItemSelectedListener = this


        btnUpdate?.setOnClickListener({
            scrollSpeed= sldSpeed?.value!!
            allowSpeaking= swtAllowSpeak!!.isChecked
            selectedVoice= spnChooseVoice!!.selectedItem.toString()
            settingsViewModel.updateSettings( scrollSpeed, allowSpeaking,selectedVoice)
            val action = SettingsFragmentDirections
                .actionSettingsFragmentToScrollFragment(selectedVoice, scrollSpeed, allowSpeaking,scrollPos)
            firmViewModel.scrollingPaused = false
            firmViewModel.navigatedBack = true

            findNavController().navigate(action)

        })




        btnCancel?.setOnClickListener({
            val action = SettingsFragmentDirections
                .actionSettingsFragmentToScrollFragment(selectedVoice, scrollSpeed, allowSpeaking,scrollPos)
            firmViewModel.scrollingPaused = false
            firmViewModel.navigatedBack = true

            findNavController().navigate(action)
        })


    }

    override fun onInit(p0: Int) {

        if (p0 == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d("Firms", "The Language specified is not supported!")
            } else {
                Log.d("Firms", "Initilization Success!!!")
                Log.d("Firms", "prev voice = "+tts!!.voice.name.toString() +";Setting voice to "+ selectedVoice)
                val stringArray = getResources().getStringArray(R.array.allVoices);
                val currentVoice = when(selectedVoice)
                {
                    stringArray[0]->"en-US-Language"
                    stringArray[1]->"en-us-x-iom-network"
                    stringArray[2]->"en-GB-language"
                    stringArray[3]->"en-gb-x-rjs-local"
                    else->"en-US-Language"
                }
                val prevVoiceName = tts!!.voice.name.toString()

                for (voice in tts!!.voices)
                {

                    if (currentVoice.length >0)
                    {
                        if (currentVoice.equals(voice.name.toString()))
                            tts!!.setVoice(voice)
                    }


                }
                Log.d("Firms", "oldvoice :" +prevVoiceName+"->newvoice:"+tts!!.voice.name.toString())

                if (voiceChanged) {
                    lifecycleScope.launch {
                       var waitTime:Long =0
                        if (selectedVoice.equals("US Female"))
                            waitTime = 3000
                        else
                            waitTime = 6000
                        tts!!.speak(
                            getString(R.string.voice_sample),
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            ""
                        )
                        delay(waitTime)
                        btnUpdate?.isEnabled= true
                    }
                }

            }



        } else {
            Log.d("Firms", "Initilization Failed!")
        }


    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        val oldVoice = selectedVoice
        selectedVoice = parent.getItemAtPosition(pos).toString()
        voiceChanged = (!(selectedVoice.equals(oldVoice)))

        if (voiceChanged)
        {
            btnUpdate?.isEnabled= false
            onInit(TextToSpeech.SUCCESS)
         }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}