package com.example.myfirmations

import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirmations.*
import com.example.myfirmations.data.Settings
import com.example.myfirmations.databinding.FragmentScrollBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.system.exitProcess

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScrollFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScrollFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var settings: Settings
    var scrollSpeed: Float = 15f
    var allowSpeaking: Boolean = true
    lateinit var selectedVoice: String
    var firmId: Int = 1
    var txtFirm: TextView? = null
    var firmArg: Int = 0
    private var scrollPos: Int = 0

    lateinit var rclFirms: RecyclerView
    val scrollAdapter = ScrollAdapter(this)
    var autoScrollStarted = false


    val args: ScrollFragmentArgs by navArgs()


    private var binding: FragmentScrollBinding? = null

    private val firmViewModel: FirmViewModel by activityViewModels {
        FirmViewModelFactory(
            (activity?.application as FirmApplication)
                .database.firmDao()
        )
    }


    fun getFirmVM(): FirmViewModel {
        return firmViewModel
    }

    private val setViewModel: SettingsViewModel by activityViewModels {
        SettingsViewModelFactory(
            (activity?.application as FirmApplication)
                .database.settingsDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        firmViewModel.firms.observe(this) { items ->
            items.let { items ->
                items.let {
                    scrollAdapter.submitList(it)
                    //quote = it.get(0).quote
                }
            }
        }

        setViewModel.currentSettings.observe(this) { sets ->
            settings = sets
            scrollSpeed = settings!!.scrollSpeed
            allowSpeaking = settings!!.allowSpeaking
            selectedVoice = settings!!.selectedVoice
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentScrollBinding = FragmentScrollBinding.inflate(inflater, container, false)
        binding = fragmentScrollBinding
        return fragmentScrollBinding.root

        //return inflater.inflate(R.layout.fragment_scroll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val btnAdd = binding!!.btnAddEdit
        val btnSnooze = binding!!.btnSnooze
        val btnSetup = binding!!.btnSettings
        val btnExit = binding!!.btnExit
        rclFirms = binding!!.rclFirms

        selectedVoice = args.selectedVoice
        scrollPos = args.scrollPos

        rclFirms.adapter = scrollAdapter
        rclFirms.layoutManager =
            LinearLayoutManager(binding!!.root!!.context, LinearLayoutManager.HORIZONTAL, false)
        rclFirms.getRecycledViewPool().setMaxRecycledViews(0, 1)

        btnAdd?.setOnClickListener({
            firmId = scrollAdapter.getId()
            val action = ScrollFragmentDirections
                .actionScrollFragmentToAddEditFragment(
                    firmId,
                    selectedVoice,
                    scrollSpeed,
                    allowSpeaking,
                    scrollPos
                )
            firmViewModel.scrollingPaused = true
            findNavController().navigate(action)
        })

        btnSetup?.setOnClickListener({
            firmViewModel.scrollingPaused = true
            val action = ScrollFragmentDirections
                .actionScrollFragmentToSettingsFragment(
                    scrollSpeed, allowSpeaking, selectedVoice, scrollPos
                )
            findNavController().navigate(action)
        })


        btnExit.setOnClickListener({
            exitProcess(-1)
        })

        btnSnooze?.setOnClickListener({
            var snoozeTill: Long = 0
            var calendar = Calendar.getInstance()
            val popupMenu = PopupMenu(view.context, btnSnooze)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_snooze5min -> {
                        calendar.add(Calendar.MINUTE, 5)
                        snoozeTill = calendar.timeInMillis
                    }
                    R.id.action_snoozeday -> {
                        calendar.add(Calendar.DATE, 1)
                        snoozeTill = calendar.timeInMillis
                    }
                    R.id.action_snoozeweek -> {
                        calendar.add(Calendar.DATE, 7)
                        snoozeTill = calendar.timeInMillis
                    }
                    R.id.action_snoozeforever -> {
                        snoozeTill = -1
                    }
                }
                firmViewModel.updateFirm(
                    scrollAdapter.getId(),
                    scrollAdapter.getQuote(),
                    scrollAdapter.getImgName(),
                    scrollAdapter.getSayThis(),
                    snoozeTill
                )
                lifecycleScope.launch {
                    tailRecursiveScroll(
                        rclFirms,
                        scrollAdapter
                    )
                }
                true
            })
            popupMenu.show()
        })

        firmViewModel.scrollingPaused = false
        scrollPos = args.scrollPos
        Log.d("Firms", "Deciding whether to custom scroll to id " + scrollPos)
        if (scrollPos > 0)// {
            rclFirms.scrollToPosition(scrollPos)

        // }
        // else {
        //  if (!(autoScrollStarted)) {
        //     autoScrollStarted = true

        lifecycleScope.launch {
            tailRecursiveScroll(rclFirms, scrollAdapter)
        }
        // }
        //}

    }

    private tailrec suspend fun tailRecursiveScroll(
        recyclerView: RecyclerView, scrollAdapter: ScrollAdapter
    ) {

        delay(scrollSpeed.toLong() * 1000)
        firmViewModel.autoscrolled = true
        /*
        if (firmViewModel.navigatedBack)
        {
            firmViewModel.increaseOrdinalPos()
            firmViewModel.navigatedBack=false
            firmViewModel.setBoundId(scrollAdapter.getId())

        }
*/
        if (recyclerView.canScrollHorizontally(RecyclerView.EdgeEffectFactory.DIRECTION_RIGHT)) {
            val targetpos = getFirmVM().getOrdinalPos()
            Log.d(
                "Firms",
                "Scrolling to pos " + targetpos.toString() + " to say " + scrollAdapter.getQuote()
            )
            recyclerView.scrollToPosition(targetpos)
        } else {
            getFirmVM().resetOrdinalPos()
            recyclerView.scrollToPosition(0)
        }
        Log.d("Firms", "ScrollingPaused is " + firmViewModel.scrollingPaused)
        if (!firmViewModel.scrollingPaused) {
            tailRecursiveScroll(recyclerView, scrollAdapter)
        }

    }


}