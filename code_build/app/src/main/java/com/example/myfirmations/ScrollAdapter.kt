package com.example.myfirmations

import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirmations.data.Firmation
import com.example.myfirmations.databinding.ListItemBinding
import com.example.myfirmations.databinding.ListItemBinding.inflate
import java.util.*

class ScrollAdapter(private val parentFragment: ScrollFragment)
    :ListAdapter<Firmation, ScrollAdapter.ItemViewHolder>(DiffCallBack) {

    private var quote: String = ""
    private var imgName: String = ""
    private var snoozeTill: Long = 0
    private var sayThis: Boolean = true
    private var id: Int = 1

    fun getId():Int{
        return id
    }



    fun getImgName():String{
        return imgName
    }
    fun getSnoozeTill():Long{
        return snoozeTill
    }
    fun getSayThis():Boolean{
        return sayThis
    }
    fun getQuote():String{
        return quote
    }

    //class ItemViewHolder(binding: ListItemBinding)
    //    :RecyclerView.ViewHolder(binding.root), TextToSpeech.OnInitListener{
        class ItemViewHolder(val binding: ListItemBinding,parentFragment: ScrollFragment) :
             RecyclerView.ViewHolder(binding.root), TextToSpeech.OnInitListener{

        val txtFirm = binding.txtFirm
        val imgFirm = binding.imgFirm
        val context = binding.root.context
        private var tts: TextToSpeech? = TextToSpeech(context, this,"com.google.android.tts")
        //TextToSpeech tts = new TextToSpeech(context, this, "com.google.android.tts");
        var ttsInitialized = false
        var quote = ""
        var allowSpeaking = parentFragment.allowSpeaking
        var selectedVoice = parentFragment.selectedVoice
        val parentFrag = parentFragment
        var firmId = 0


        fun bind(firm: Firmation)
        {
            txtFirm.text = firm.quote
            firmId = firm.id
            val resid = context.resources.getIdentifier(firm.imgName, "drawable", context.packageName)
            imgFirm.setImageResource(resid)
            quote = firm.quote
            if (parentFrag.getFirmVM().boundIdChanged(firmId)) {
                parentFrag.getFirmVM().increaseOrdinalPos()
                if (ttsInitialized)
                   if ((allowSpeaking)&&(parentFrag.getFirmVM().autoscrolled)) {
/*
                       if (!(tts!!.voice.name.toString()
                               .equals(parentFrag.settings.selectedVoice))
                       ) {
                           Log.d("Firms","tts Voice::"+tts!!.voice.name.toString()+";parent voice:"+ parentFrag.settings.selectedVoice)
                           selectedVoice = parentFrag.settings.selectedVoice
                           onInit(TextToSpeech.SUCCESS)
                       } else {

 */
                           tts!!.speak(firm.quote, TextToSpeech.QUEUE_ADD, null, "")
                           parentFrag.getFirmVM().setBoundId(firmId)
                      // }
                       parentFrag.getFirmVM().autoscrolled = false
                   }
                    }


        }
       override fun onInit(p0: Int) {
       // fun onInit(p0: Int) {
            Log.d("Firms", "Begin Init TTS : Selected voice is [" + selectedVoice+"]")

            if (p0 == TextToSpeech.SUCCESS) {
                // set US English as language for tts
                val result = tts!!.setLanguage(Locale.US)

                val stringArray = context.resources.getStringArray(R.array.allVoices);
                //val selection = parent.getItemAtPosition(pos).toString()
                val currentVoice = when(selectedVoice)
                {
                    stringArray[0]->"en-US-Language"
                    stringArray[1]->"en-us-x-iom-network"
                    stringArray[2]->"en-GB-language"
                    stringArray[3]->"en-gb-x-rjs-local"
                    else->"en-US-Language"
                }
                Log.d("Firms", "Ready to set TTS :currentVoice is " + currentVoice)

                for (voice in tts!!.voices)
                {

                    if (currentVoice.length >0)
                    {
                        if (currentVoice.equals(voice.name.toString()))
                            tts!!.setVoice(voice)
                    }


                }

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.d("Firms", "The Language specified is not supported!")
                } else {
                    Log.d("Firms", "Initilization Success!!!")
                    if (allowSpeaking)
                        if (parentFrag.getFirmVM().boundIdChanged(firmId)){
                            tts!!.speak(quote, TextToSpeech.QUEUE_FLUSH, null, "")
                            parentFrag.getFirmVM().setBoundId(firmId)
                        }
                }

            } else {
                Log.d("Firms", "Initilization Failed!")
            }
            ttsInitialized = true

        }




    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val current = getItem(position)
            quote = current.quote
            imgName = current.imgName
            snoozeTill = current.snoozeTill
            sayThis = current.sayThis
            id = current.id
            holder.bind(current)
    }

    companion object{
        private val DiffCallBack = object:DiffUtil.ItemCallback<Firmation>()
        {
            override fun areItemsTheSame(oldItem: Firmation, newItem: Firmation): Boolean {
                return oldItem.id==newItem.id
            }

            override fun areContentsTheSame(oldItem: Firmation, newItem: Firmation): Boolean {
                return oldItem==newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = inflate(layoutInflater, parent, false)
        return ItemViewHolder(binding, parentFragment)
    }
}