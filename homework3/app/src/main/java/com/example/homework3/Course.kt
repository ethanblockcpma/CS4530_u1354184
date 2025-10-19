package com.example.homework3

//basic course class
data class Course(
    val id: String = java.util.UUID.randomUUID().toString(),
    val courseNum: String,
    val lo: String,
    val dept: String)
{
    val courseName: String get() = "$dept $courseNum"
}