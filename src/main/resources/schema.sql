CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    roll_no VARCHAR(30) NOT NULL UNIQUE,
    admission_year INT NOT NULL,
    twelfth_percentage FLOAT NOT NULL,
    eapcet_rank INT NOT NULL,
    branch VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'Active',
    phone VARCHAR(20),
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
