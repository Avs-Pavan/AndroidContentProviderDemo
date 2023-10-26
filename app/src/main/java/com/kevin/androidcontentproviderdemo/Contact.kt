package com.kevin.androidcontentproviderdemo

data class Contact(val name: String, val numbers: MutableList<String> = mutableListOf())