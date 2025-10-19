package com.example.homework3

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.homework3.data.CourseRepository
import com.example.homework3.data.CourseDatabase

class MainActivity : ComponentActivity() {

    private val courseViewModel: CourseViewModel by viewModels {
        CourseViewModelFactory(
            CourseRepository(
                CourseDatabase.getDatabase(this).courseDao()
            )
        )
    }

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
    val courses by viewModel.courses.observeAsState(emptyList())
    val selectedCourse by viewModel.selectedCourse.observeAsState()

    var departmentInput by remember { mutableStateOf("") }
    var courseNumberInput by remember { mutableStateOf("") }
    var locationInput by remember { mutableStateOf("") }
    var editingCourse by remember { mutableStateOf<Course?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Course Manager") }) }
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
                    if (departmentInput.isNotBlank() && courseNumberInput.isNotBlank() && locationInput.isNotBlank()) {
                        if (editingCourse == null) {
                            viewModel.addCourse(departmentInput, courseNumberInput, locationInput)
                        } else {
                            viewModel.updateCourse(
                                editingCourse!!.copy(
                                    dept = departmentInput,
                                    courseNum = courseNumberInput,
                                    lo = locationInput
                                )
                            )
                            editingCourse = null
                        }
                        departmentInput = ""
                        courseNumberInput = ""
                        locationInput = ""
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
                items(courses, key = { it.id }) { course ->
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
            selectedCourse?.let { CourseDetailView(it) }
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
        OutlinedTextField(value = dept, onValueChange = onDepartmentChange, label = { Text("Department") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = courseNum, onValueChange = onCourseNumberChange, label = { Text("Course Number") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = lo, onValueChange = onLocationChange, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            if (isEditing) {
                Button(onClick = onCancelEdit, modifier = Modifier.padding(end = 8.dp)) { Text("Cancel") }
            }
            Button(onClick = onAddOrUpdateCourse) { Text(if (isEditing) "Update" else "Add") }
        }
    }
}

@Composable
fun CourseListItem(course: Course, onCourseClick: (Course) -> Unit, onDeleteClick: (Course) -> Unit, onEditClick: (Course) -> Unit) {
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
            IconButton(onClick = { onEditClick(course) }) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
            IconButton(onClick = { onDeleteClick(course) }) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
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

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun CourseDetailPreview() {
    MaterialTheme {
        CourseDetailView(Course(dept = "CS", courseNum = "4530", lo = "WEB L112"))
    }
}
