package ru.pearx.cheapcsv

import org.jsoup.Jsoup
import java.nio.file.Paths

fun main(vararg args: String) {
    val outputDir = Paths.get("") //todo
    println("[1/3] Getting the TLD list from $TLD_LIST_URL...")
    val doc = Jsoup.connect(TLD_LIST_URL).get()

    println("[2/3] Parsing the HTML...")
    val tables = doc.getElementById("pricing").getElementsByTag("table")
    val map = hashMapOf<String, List<Tld>>()
    for (table in tables) {
        val fileName = table.getElementsByTag("thead").first().child(0).child(0).text().split(' ').joinToString("_") { it.toLowerCase() } // thead/tr/tr.text
        val lst = mutableListOf<Tld>()
        for (row in table.getElementsByTag("tbody").first().children()) { // tbody/*
            val name = row.child(0).child(0).text() // th/a.text

            val whoIsGuard = row.child(1).child(0).text() == "yes"

            val amountTag = row.getElementsByClass("amount").first()
            val price = amountTag.child(1).text().toDouble()
            val fee = amountTag.children().size > 2

            val renewalPrice = row.getElementsByClass("renewal-price").first().child(0).text().toDouble()

            lst.add(Tld(name, whoIsGuard, price, fee, renewalPrice))
        }
        map[fileName] = lst
    }

    println("[3/3] Writing the CSV files...")
    for ((name, list) in map) {
        printTldsToFile(outputDir, name, iterator { for(tld in list) yield(tld) })
    }
    printTldsToFile(outputDir, "all", iterator {
        for(list in map.values) {
            for(tld in list) {
                yield(tld)
            }
        }
    })
}

