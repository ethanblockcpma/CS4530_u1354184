package com.example.assignment_2
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

/*
@param dept: The department the course belongs to
@param courseNum: The course number
@param lo: The location of the course
@param id: The unique identifier for the course
@param courseName: A computed property that combines the department and course number

 */
//some bsaic functions for the course class
class CourseViewModel: ViewModel() {
    private val _courses = mutableStateListOf<Course>()
    val courses: List<Course> = _courses
    private val _selectedCourse = mutableStateListOf<Course?>()
    val selectedCourse: Course? get() = _selectedCourse.firstOrNull()

    fun addCourse(dept: String, courseNum: String, lo: String) {
        val newCourse = Course(dept = dept, courseNum = courseNum, lo = lo)
        _courses.add(newCourse)
    }

    fun deleteCourse(course: Course) {
        _courses.remove(course)
        if(selectedCourse == course) {
            _selectedCourse.clear()
        }
    }
    fun clearSelectedCourse(){
        _selectedCourse.clear()
    }

    fun updateCourse(updatedCourse: Course) {
        val index = _courses.indexOfFirst { it.id == updatedCourse.id }
        if (index != -1) {
            _courses[index] = updatedCourse
            if(selectedCourse?.id == updatedCourse.id) {
                _selectedCourse.clear()
                _selectedCourse.add(updatedCourse)
            }
        }

    }

    fun selectCourse(course: Course) {
        _selectedCourse.clear()
        _selectedCourse.add(course)

    }
}