package com.example.myfirmations

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.*
import com.example.myfirmations.data.Firmation
import com.example.myfirmations.data.FirmationDao
import kotlinx.coroutines.launch
import kotlin.random.Random

class FirmViewModel(private val firmationDao: FirmationDao): ViewModel() {


    //@Query("Select * from Firmation")
    fun viewFirms(): LiveData<List<Firmation>> = firmationDao.getFirms().asLiveData()
    fun getFirmById(id:String):LiveData<Firmation> = firmationDao.getFirmById(id).asLiveData()
    private val seed = System.currentTimeMillis()
    val firms:LiveData<List<Firmation>> = Transformations.map(viewFirms()){
        it.shuffled(Random(seed))
    }

    var scrollingPaused:Boolean = false
    var navigatedBack:Boolean = false
    var autoscrolled:Boolean = false // will only speak if autoscrolled - will use this to implement

    private var boundFirmId = 0
    private var ordinalPos:Int =0
    fun increaseOrdinalPos(){
        ordinalPos+=1
    }
    fun resetOrdinalPos(){
        ordinalPos = 0
    }
    fun getOrdinalPos():Int{
        return ordinalPos
    }
    fun getBoundId(): Int{
        return boundFirmId
    }

    fun boundIdChanged(testId:Int):Boolean {
        return (testId!=boundFirmId)
    }
    fun setBoundId(newId:Int){
        boundFirmId = newId
    }

    //@Insert(onConflict=  OnConflictStrategy.IGNORE)
    fun insertFirm(firm: Firmation)
    {
        viewModelScope.launch {
            firmationDao.insert(firm)
        }
    }


   // fun insertList(firmList:List<Firmation>)


    fun update(firm: Firmation)
    {
        viewModelScope.launch{
            firmationDao.update(firm)
        }
    }

    fun addFirm(quote:String,
    imgName:String,
    willSay:Boolean,
    snoozeTill:Long)
    {
        val newFirm = createFirm(quote, imgName, willSay, snoozeTill)
        insertFirm(newFirm)
    }

    fun createFirm(quote:String,
                imgName:String,
                willSay:Boolean,
                snoozeTill:Long): Firmation
    {
        return Firmation(0, quote, imgName, willSay, snoozeTill)
    }

    fun updateFirm(id:Int,
                   quote:String,
                   imgName:String,
                   willSay:Boolean,
                   snoozeTill:Long)
    {
        val updatedFirm = getUpdatedFirm(id, quote, imgName, willSay, snoozeTill)
        update(updatedFirm)
    }


    fun getUpdatedFirm(id:Int,
                       quote:String,
                   imgName:String,
                   willSay:Boolean,
                   snoozeTill:Long): Firmation
    {
        return Firmation(id, quote, imgName, willSay, snoozeTill)
    }





}

class FirmViewModelFactory(
    private val firmationDao: FirmationDao
):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FirmViewModel::class.java))
        {
            @Suppress("UNCHECKED_CAST")
            return FirmViewModel(firmationDao) as T
    }
    throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
