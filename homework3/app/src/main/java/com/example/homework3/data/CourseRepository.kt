package com.example.homework3.data

import kotlinx.coroutines.flow.Flow

class CourseRepository(private val dao: CourseDao) {

    fun getAllCourses(): Flow<List<CourseEntity>> = dao.getAllCourses()

    suspend fun insert(course: CourseEntity) = dao.insert(course)
    suspend fun update(course: CourseEntity) = dao.update(course)
    suspend fun delete(course: CourseEntity) = dao.delete(course)
}
