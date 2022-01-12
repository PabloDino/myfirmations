package com.example.myfirmations

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myfirmations.*
import com.example.myfirmations.data.Firmation
import com.example.myfirmations.databinding.FragmentAddEditBinding
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEditFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var binding:FragmentAddEditBinding?= null
    lateinit var firm: Firmation


    var editText: EditText? =null
    var imageView: ImageView? = null
    var txtSnoozeTill: TextView? = null
    var chkSayIt: CheckBox? = null


    var imageName:String=""
    var quote:String = ""//editText?.text.toString()
    var snoozeTill:Long=0//txtSnoozeTill?.text.toString().toLong()
    var sayThis:Boolean   = false

    val args:AddEditFragmentArgs by navArgs()
    var selectedVoice =""
    var scrollSpeed:Float =0f
    var allowSpeaking = false
    var scrollPos:Int =0

    private var firmId:Int =0

    val firmViewModel: FirmViewModel by activityViewModels() {
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
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            val fragmentAddEditBinding = FragmentAddEditBinding.inflate(inflater, container, false)
            binding = fragmentAddEditBinding
            return fragmentAddEditBinding.root
        }



    private fun bind(firm: Firmation)
    {

        val dateFormatter= SimpleDateFormat("E MMM d", Locale.getDefault())


        editText?.setText(firm.quote)
        val imgId: Int? = context?.resources?.getIdentifier(firm.imgName,"drawable", context?.packageName)
        if (imgId != null) {
            imageView?.setImageResource(imgId)
        }
        if (firm.sayThis)
            chkSayIt?.isChecked = true
        else
            chkSayIt?.isChecked= false

        if ((firm.snoozeTill>0)||(firm.snoozeTill<0)) {

            txtSnoozeTill?.setText("Snoozed till " + dateFormatter.format(firm.snoozeTill))
        }
        else
        {
            txtSnoozeTill?.setText("")

        }



    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val btnSave= binding?.btnSave
            val btnCopy = binding?.btnCopy
            val btnCancel = binding?.btnCancel

            selectedVoice =args.selectedVoice
            scrollSpeed= args.scrollSpeed
            allowSpeaking = args.allowSpeaking

            editText= binding?.txtFirm
            imageView = binding?.imgFirm
            chkSayIt= binding?.chkSayIt

            firmId = args.firmId
            scrollPos = firmViewModel.getOrdinalPos()-1
            firmViewModel.getFirmById(firmId.toString()).observe(this.viewLifecycleOwner){
                selectedFirm->firm = selectedFirm
                imageName = firm.imgName
                quote = firm.quote
                snoozeTill= firm.snoozeTill
                sayThis = firm.sayThis

                bind(firm)

        }

            btnSave?.setOnClickListener({
                quote = editText!!.text.toString()
                sayThis= chkSayIt!!.isChecked
                firmViewModel.updateFirm(firmId, quote, imageName, sayThis, snoozeTill)
                Log.d("Firms","sending back data at pos "+scrollPos.toString())
                firmViewModel.scrollingPaused = false
                firmViewModel.navigatedBack = true

                val action = AddEditFragmentDirections
                    .actionAddEditFragmentToScrollFragment(selectedVoice,scrollSpeed,allowSpeaking, scrollPos)
                findNavController().navigate(action)

            })
            btnCopy?.setOnClickListener({
                quote = editText!!.text.toString()
                sayThis= chkSayIt!!.isChecked
                firmViewModel.addFirm(quote, imageName, sayThis, snoozeTill)
                val action = AddEditFragmentDirections
                    .actionAddEditFragmentToScrollFragment(selectedVoice,scrollSpeed,allowSpeaking, scrollPos)

                firmViewModel.scrollingPaused = false
                firmViewModel.navigatedBack = true
                findNavController().navigate(action)
            })

            chkSayIt?.setOnClickListener({
                var willSay= false
                if (chkSayIt?.isChecked!!)
                    willSay = true
                firmViewModel.updateFirm(id, quote, imageName, willSay, snoozeTill)
            })


            btnCancel?.setOnClickListener({
                val action = AddEditFragmentDirections
                    .actionAddEditFragmentToScrollFragment(selectedVoice,scrollSpeed,allowSpeaking, scrollPos)
                firmViewModel.scrollingPaused = false
                firmViewModel.navigatedBack = true

                findNavController().navigate(action)
            })

    }




}