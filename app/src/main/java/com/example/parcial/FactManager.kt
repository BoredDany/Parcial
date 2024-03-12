package com.example.parcial

import android.app.Activity
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets

class FactManager {
    lateinit var facts: JSONArray
    var factsList: MutableList<Fact> = mutableListOf()
    var favs: MutableList<Fact> = mutableListOf()
    val jsonFile = "facts.json"

    companion object{
        val instance: FactManager by lazy { FactManager()}
    }

    fun loadJSONFromAsset(activity: Activity): String? {
        try {
            val assets = activity.assets
            val inputStream = assets.open(jsonFile)
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()
            return String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
    }

    fun getFacts(activity: Activity): JSONArray {
        val json = JSONObject(loadJSONFromAsset(activity))
        return json.getJSONArray("facts")
    }

    fun buildAdapter(activity: Activity): MutableList<String> {
        try {
            val jsonArray = getFacts(activity)
            val factsArr = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val name: String = jsonObject.getString("name")
                val category: String = jsonObject.getString("category")
                val description: String = jsonObject.getString("description")
                val image_url: String = jsonObject.getString("image_url")
                val facts = jsonObject.getJSONArray("factos")
                val factDetailList = ArrayList<FactDetail>()
                for (j in 0 until facts.length()) {
                    val factObject = facts.getJSONObject(j)
                    val title = factObject.getString("title")
                    val text = factObject.getString("text")
                    val factDetail = FactDetail(title, text, category)
                    factDetailList.add(factDetail)
                }

                factsArr.add(name)

                var fact = Fact(name, category, description, image_url, factDetailList)

                if(!isAdded(name)){
                    factsList.add(fact)
                }
            }
            return factsArr
        } catch (ex: Exception) {
            ex.printStackTrace()
            return mutableListOf()
        }
    }

    fun buildFactosAdapter(name: String): MutableList<String> {
        val factsArr = mutableListOf<String>()
        for(facto: Fact in factsList){
            if(facto.name.equals(name)){
                for(detail: FactDetail in facto.facts){
                    factsArr.add(detail.title)
                }
            }
        }
        return factsArr
    }

    fun getFactByName(name: String): Fact? {
        for(fact: Fact in factsList){
            if(fact.name.equals(name)){
                return fact
            }
        }
        return null
    }

    fun isAddedToFav(fact: Fact?): Boolean{
        if(fact != null){
            for(factF: Fact in favs){
                if(factF.name.equals(fact.name)){
                    return true
                }
            }
        }
        return false
    }

    fun isAdded(name: String): Boolean{
        for(fact: Fact in factsList){
            if(fact.name == name){
                return true
            }
        }
        return false
    }
    fun addFav(fact: Fact?){
        if(fact != null){
            favs.add(fact)
        }
    }

    fun buildFav(): MutableList<String>{
        val favsFacts = mutableListOf<String>()
        for(fact: Fact in favs){
            favsFacts.add(fact.name)
        }
        return favsFacts
    }

    fun buildRecomendations(): String {
        val recomendations: MutableList<String> = mutableListOf()
        val favsCategories = mutableMapOf<String, Int>()
        var maxFrequency = 0

        for (fact: Fact in favs) {
            val category = fact.category
            favsCategories[category] = favsCategories.getOrDefault(category, 0) + 1
            maxFrequency = maxOf(maxFrequency, favsCategories[category]!!)
        }

        for ((category, frequency) in favsCategories) {
            if (frequency == maxFrequency) {
                for (fact in favs) {
                    if (fact.category == category) {
                        recomendations.add(fact.name)
                    }
                }
            }
        }
        if(recomendations.size == 0){
            return ""
        }
        return recomendations.random()
    }
}