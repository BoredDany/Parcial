package com.example.parcial

class Fact {
    var name: String
    var category: String
    var description: String
    var image_url: String
    var facts: ArrayList<FactDetail>

    constructor(
        name: String,
        category: String,
        description: String,
        image_url: String,
        facts: ArrayList<FactDetail>
    ) {
        this.name = name
        this.category = category
        this.description = description
        this.image_url = image_url
        this.facts = facts
    }
}