package com.github.rtyvZ.kitties.extantions

fun <T> MutableList<T>.replaceElement(element: T) {
    val indexOfElement = this.indexOf(element)
    this[indexOfElement] = element
}