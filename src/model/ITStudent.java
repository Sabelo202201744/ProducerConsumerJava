package model;

import java.util.*;

public class ITStudent {
    private String name;
    private String studentId; // 8 digits
    private String programme;
    private Map<String, Integer> courseMarks; // course -> mark

    public ITStudent() {
        courseMarks = new LinkedHashMap<>();
    }

    public ITStudent(String name, String studentId, String programme, 
        Map<String,Integer> courseMarks) {
        this.name = name;
        this.studentId = studentId;
        this.programme = programme;
        this.courseMarks = new LinkedHashMap<>(courseMarks);
    }

    // getters + setters
    public String getName() { return name; }
    public String getStudentId() { return studentId; }
    public String getProgramme() { return programme; }
    public Map<String,Integer> getCourseMarks() { return courseMarks; }

    public void setName(String name) { this.name = name; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setProgramme(String programme) { this.programme = programme; }
    public void setCourseMarks(Map<String,Integer> courseMarks) { 
        this.courseMarks = new LinkedHashMap<>(courseMarks); 
    }

    public double average() {
        if (courseMarks.isEmpty()) return 0.0;
        double sum = 0;
        for (int m : courseMarks.values()) sum += m;
        return sum / courseMarks.size();
    }

    public boolean passed() {
        return average() >= 50.0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append(System.lineSeparator());
        sb.append("Student ID: ").append(studentId).append(System.lineSeparator());
        sb.append("Programme: ").append(programme).append(System.lineSeparator());
        sb.append("Courses and marks:").append(System.lineSeparator());
        for (Map.Entry<String,Integer> e : courseMarks.entrySet()) {
            sb.append("  ").append(e.getKey()).append(": ")
            .append(e.getValue()).append(System.lineSeparator());
        }
        sb.append(String.format("Average: %.2f%n", average()));
        sb.append("Result: ").append(passed() ? "Pass" : "Fail")
        .append(System.lineSeparator());
        return sb.toString();
    }
}
