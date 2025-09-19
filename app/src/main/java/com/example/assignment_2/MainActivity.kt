package com.example.assignment_2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit 
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview


class MainActivity : ComponentActivity() {
    private val courseViewModel: CourseViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                CourseAppScreen(courseViewModel)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseAppScreen(viewModel: CourseViewModel) {
    var departmentInput by remember { mutableStateOf("") }
    var courseNumberInput by remember { mutableStateOf("") }
    var locationInput by remember { mutableStateOf("") }
    var editingCourse by remember { mutableStateOf<Course?>(null) } 

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Course Manager") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            CourseInputForm(
                dept = departmentInput,
                onDepartmentChange = { departmentInput = it },
                courseNum = courseNumberInput,
                onCourseNumberChange = { courseNumberInput = it },
                lo = locationInput,
                onLocationChange = { locationInput = it },
                isEditing = editingCourse != null,
                onAddOrUpdateCourse = {
                    if (editingCourse == null) { 
                        if (departmentInput.isNotBlank() && courseNumberInput.isNotBlank() && locationInput.isNotBlank()) {
                            viewModel.addCourse(departmentInput, courseNumberInput, locationInput)
                            departmentInput = ""
                            courseNumberInput = ""
                            locationInput = ""
                        }
                    } else { // ec
                        if (departmentInput.isNotBlank() && courseNumberInput.isNotBlank() && locationInput.isNotBlank()) {
                            viewModel.updateCourse(
                                editingCourse!!.copy(
                                    dept = departmentInput,
                                    courseNum = courseNumberInput,
                                    lo = locationInput
                                )
                            )
                            departmentInput = ""
                            courseNumberInput = ""
                            locationInput = ""
                            editingCourse = null
                        }
                    }
                },
                onCancelEdit = {
                    editingCourse = null
                    departmentInput = ""
                    courseNumberInput = ""
                    locationInput = ""
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Courses", style = MaterialTheme.typography.headlineSmall)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(viewModel.courses, key = { course -> course.id }) { course ->
                    CourseListItem(
                        course = course,
                        onCourseClick = { viewModel.selectCourse(it) },
                        onDeleteClick = { viewModel.deleteCourse(it) },
                        onEditClick = {
                            editingCourse = it
                            departmentInput = it.dept
                            courseNumberInput = it.courseNum
                            locationInput = it.lo
                            viewModel.clearSelectedCourse()
                        }
                    )
                    HorizontalDivider()
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            viewModel.selectedCourse?.let { selectedCourse ->
                CourseDetailView(course = selectedCourse)
            }
        }
    }
}

@Composable
fun CourseInputForm(
    dept: String,
    onDepartmentChange: (String) -> Unit,
    courseNum: String,
    onCourseNumberChange: (String) -> Unit,
    lo: String,
    onLocationChange: (String) -> Unit,
    isEditing: Boolean,
    onAddOrUpdateCourse: () -> Unit,
    onCancelEdit: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = dept,
            onValueChange = onDepartmentChange,
            label = { Text("Department (e.g., CS)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = courseNum,
            onValueChange = onCourseNumberChange,
            label = { Text("Course Number (e.g., 4530)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = lo,
            onValueChange = onLocationChange,
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (isEditing) {
                Button(
                    onClick = onCancelEdit,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Cancel")
                }
            }
            Button(onClick = onAddOrUpdateCourse) {
                Text(if (isEditing) "Update Course" else "Add Course")
            }
        }
    }
}
@Composable
fun CourseListItem(
    course: Course,
    onCourseClick: (Course) -> Unit,
    onDeleteClick: (Course) -> Unit,
    onEditClick: (Course) -> Unit
) {
    //had some help from copilot with the row implementation
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCourseClick(course) }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(course.courseName, style = MaterialTheme.typography.bodyLarge)
        Row {
            IconButton(onClick = { onEditClick(course) }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Course")
            }
            IconButton(onClick = { onDeleteClick(course) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Course")
            }
        }
    }
}

@Composable
fun CourseDetailView(course: Course) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Course Details", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Department: ${course.dept}")
            Text("Course Number: ${course.courseNum}")
            Text("Location: ${course.lo}")
        }
    }
}
@SuppressLint("ViewModelConstructorInComposable") //i don't know why this was an issue, but it was.
@Preview(showBackground = true, name = "App Screen Preview")
@Composable
fun DefaultPreview() {
    MaterialTheme {
        CourseAppScreen(CourseViewModel().apply {
            addCourse("CS", "1010", "WEB L110")
            addCourse("MATH", "2200", "LCB 215")
            selectCourse(courses.first())
        })
    }
}

//example course to show something on startup
@Preview(showBackground = true, name = "Course Detail Preview")
@Composable
fun CourseDetailPreview() {
    MaterialTheme {
        CourseDetailView(Course(dept = "CS", courseNum = "4530", lo = "WEB L112"))
    }
}