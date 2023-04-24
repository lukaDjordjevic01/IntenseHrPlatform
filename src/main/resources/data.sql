INSERT INTO job_candidates (full_name, date_of_birth, contact_number, email)
VALUES ('John Doe', '1990-01-01', '123456789', 'johndoe@example.com');
INSERT INTO job_candidates (full_name, date_of_birth, contact_number, email)
VALUES ('Jane Smith', '1985-05-15', '5551234', 'janesmith@example.com');
INSERT INTO job_candidates (full_name, date_of_birth, contact_number, email)
VALUES ('Bob Johnson', '1995-11-30', '5559876', 'bob.johnson@example.com');
INSERT INTO job_candidates (full_name, date_of_birth, contact_number, email)
VALUES ('Alice Lee', '1992-06-03', '5554567', 'alice.lee@example.com');
INSERT INTO job_candidates (full_name, date_of_birth, contact_number, email)
VALUES ('Chris Davis', '1988-03-18', '5557777', 'chris.davis@example.com');


INSERT INTO skills (skill_name) VALUES ('Java Programming');
INSERT INTO skills (skill_name) VALUES ('Angular');
INSERT INTO skills (skill_name) VALUES ('Python');
INSERT INTO skills (skill_name) VALUES ('React');


INSERT INTO job_candidate_skill (job_candidate_id, skill_id) VALUES (1, 1);
INSERT INTO job_candidate_skill (job_candidate_id, skill_id) VALUES (1, 2);
INSERT INTO job_candidate_skill (job_candidate_id, skill_id) VALUES (2, 1);
INSERT INTO job_candidate_skill (job_candidate_id, skill_id) VALUES (2, 3);
INSERT INTO job_candidate_skill (job_candidate_id, skill_id) VALUES (3, 1);
INSERT INTO job_candidate_skill (job_candidate_id, skill_id) VALUES (4, 4);
INSERT INTO job_candidate_skill (job_candidate_id, skill_id) VALUES (4, 2);
INSERT INTO job_candidate_skill (job_candidate_id, skill_id) VALUES (5, 1);
INSERT INTO job_candidate_skill (job_candidate_id, skill_id) VALUES (5, 2);
INSERT INTO job_candidate_skill (job_candidate_id, skill_id) VALUES (5, 3);

