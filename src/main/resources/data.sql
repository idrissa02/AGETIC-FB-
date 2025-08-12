-- -- Insert into categories (matches entity)
-- INSERT INTO categories(id, type) VALUES (1, 'General');

-- -- Insert into admins (matches entity: hash_password, email, enabled, created_at are NOT NULL)
-- INSERT INTO admins(
--     id, username, hash_password, email, enabled, created_at
-- ) VALUES (
--     1, 'admin', 'pass', 'admin@example.com', true, NOW()
-- );

-- -- Insert into exams (matches entity: all NOT NULL fields provided)
-- INSERT INTO exams(
--     id, title, exam_date, application_deadline, quota, hours, created_at, category_id, created_by
-- ) VALUES (
--     1, 'Test Exam', '2025-12-01', '2025-11-01', 100, 3, NOW(), 1, 1
-- );