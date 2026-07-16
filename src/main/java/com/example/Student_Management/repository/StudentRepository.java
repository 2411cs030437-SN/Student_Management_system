package com.example.Student_Management.repository;

import com.example.Student_Management.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class StudentRepository {

    private static final RowMapper<Student> STUDENT_ROW_MAPPER = (rs, rowNum) -> {
        Student student = new Student();
        student.setId(rs.getLong("id"));
        student.setName(rs.getString("name"));
        student.setRollNo(rs.getString("roll_no"));
        student.setAdmissionYear(rs.getInt("admission_year"));
        student.setTwelfthPercentage(rs.getFloat("twelfth_percentage"));
        student.setEapcetRank(rs.getInt("eapcet_rank"));
        student.setBranch(rs.getString("branch"));
        student.setStatus(rs.getString("status"));
        student.setPhone(rs.getString("phone"));
        student.setAddress(rs.getString("address"));
        return student;
    };

    private final JdbcTemplate jdbcTemplate;

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Student> findAll() {
        String sql = """
                SELECT id, name, roll_no, admission_year, twelfth_percentage,
                       eapcet_rank, branch, status, phone, address
                FROM students
                ORDER BY id DESC
                """;
        return jdbcTemplate.query(sql, STUDENT_ROW_MAPPER);
    }

    public List<Student> findAllByBranch(String branch) {
        String sql = """
                SELECT id, name, roll_no, admission_year, twelfth_percentage,
                       eapcet_rank, branch, status, phone, address
                FROM students
                WHERE branch = ?
                ORDER BY id DESC
                """;
        return jdbcTemplate.query(sql, STUDENT_ROW_MAPPER, branch);
    }

    public List<Student> search(String keyword, String branch) {
        String likeKeyword = "%" + keyword + "%";
        if (branch != null && !branch.isBlank()) {
            String sql = """
                    SELECT id, name, roll_no, admission_year, twelfth_percentage,
                           eapcet_rank, branch, status, phone, address
                    FROM students
                    WHERE branch = ?
                      AND (name LIKE ? OR roll_no LIKE ? OR branch LIKE ?)
                    ORDER BY id DESC
                    """;
            return jdbcTemplate.query(sql, STUDENT_ROW_MAPPER, branch, likeKeyword, likeKeyword, likeKeyword);
        }

        String sql = """
                SELECT id, name, roll_no, admission_year, twelfth_percentage,
                       eapcet_rank, branch, status, phone, address
                FROM students
                WHERE name LIKE ? OR roll_no LIKE ? OR branch LIKE ?
                ORDER BY id DESC
                """;
        return jdbcTemplate.query(sql, STUDENT_ROW_MAPPER, likeKeyword, likeKeyword, likeKeyword);
    }

    public Optional<Student> findById(Long id) {
        String sql = """
                SELECT id, name, roll_no, admission_year, twelfth_percentage,
                       eapcet_rank, branch, status, phone, address
                FROM students
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, STUDENT_ROW_MAPPER, id).stream().findFirst();
    }

    public Optional<Student> findByRollNoAndPhone(String rollNo, String phone) {
        String sql = """
                SELECT id, name, roll_no, admission_year, twelfth_percentage,
                       eapcet_rank, branch, status, phone, address
                FROM students
                WHERE roll_no = ? AND phone = ?
                """;
        return jdbcTemplate.query(sql, STUDENT_ROW_MAPPER, rollNo, phone).stream().findFirst();
    }

    public List<String> findBranches() {
        String sql = """
                SELECT DISTINCT branch
                FROM students
                ORDER BY branch
                """;
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public List<Map<String, Object>> countByBranch() {
        String sql = """
                SELECT branch, COUNT(*) AS total
                FROM students
                GROUP BY branch
                ORDER BY branch
                """;
        return jdbcTemplate.queryForList(sql);
    }

    public Student save(Student student) {
        String sql = """
                INSERT INTO students
                (name, roll_no, admission_year, twelfth_percentage, eapcet_rank, branch, status, phone, address)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getName());
            ps.setString(2, student.getRollNo());
            ps.setInt(3, student.getAdmissionYear());
            ps.setFloat(4, student.getTwelfthPercentage());
            ps.setInt(5, student.getEapcetRank());
            ps.setString(6, student.getBranch());
            ps.setString(7, student.getStatus());
            ps.setString(8, student.getPhone());
            ps.setString(9, student.getAddress());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            student.setId(key.longValue());
        }
        return student;
    }

    public void update(Student student) {
        String sql = """
                UPDATE students
                SET name = ?, roll_no = ?, admission_year = ?, twelfth_percentage = ?,
                    eapcet_rank = ?, branch = ?, status = ?, phone = ?, address = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(
                sql,
                student.getName(),
                student.getRollNo(),
                student.getAdmissionYear(),
                student.getTwelfthPercentage(),
                student.getEapcetRank(),
                student.getBranch(),
                student.getStatus(),
                student.getPhone(),
                student.getAddress(),
                student.getId()
        );
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM students WHERE id = ?", id);
    }
}
