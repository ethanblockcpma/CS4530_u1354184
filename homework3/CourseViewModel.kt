package com.example.homework3

import androidx.lifecycle.*
import com.example.homework3.data.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map

class CourseViewModel(private val repository: CourseRepository) : ViewModel() {

    // Observe all courses from Room as LiveData
    val courses: LiveData<List<Course>> = repository.getAllCourses().map { entities ->
        entities.map { e ->
            Course(id = e.id.toString(), dept = e.dept, courseNum = e.courseNum, lo = e.lo)
        }
    }.asLiveData()

    private val _selectedCourse = MutableLiveData<Course?>()
    val selectedCourse: LiveData<Course?> = _selectedCourse

    fun addCourse(dept: String, courseNum: String, lo: String) {
        viewModelScope.launch {
            repository.insert(CourseEntity(dept = dept, courseNum = courseNum, lo = lo))
        }
    }

    fun updateCourse(course: Course) {
        viewModelScope.launch {
            val idInt = course.id.toIntOrNull() ?: return@launch
            repository.update(CourseEntity(idInt, course.dept, course.courseNum, course.lo))
        }
    }

    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            val idInt = course.id.toIntOrNull() ?: return@launch
            repository.delete(CourseEntity(idInt, course.dept, course.courseNum, course.lo))
        }
    }

    fun selectCourse(course: Course) = _selectedCourse.postValue(course)
    fun clearSelectedCourse() = _selectedCourse.postValue(null)
}
