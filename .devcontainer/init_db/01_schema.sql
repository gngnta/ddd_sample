-- Schema for quiz application
CREATE TABLE categories (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL UNIQUE,
	description TEXT
);

CREATE TABLE questions (
	id BIGSERIAL PRIMARY KEY,
	category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
	question_text TEXT NOT NULL
);

CREATE TABLE question_choices (
	id BIGSERIAL PRIMARY KEY,
	question_id BIGINT NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
	choice_text TEXT NOT NULL,
	is_correct BOOLEAN NOT NULL DEFAULT FALSE
);
CREATE TABLE attempts (
	id BIGSERIAL PRIMARY KEY,
	category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
	total_questions INTEGER,
	correct_count INTEGER,
	created_at TIMESTAMPTZ,
	completed_at TIMESTAMPTZ
);

CREATE TABLE answers (
	id BIGSERIAL PRIMARY KEY,
	attempt_id BIGINT NOT NULL REFERENCES attempts(id) ON DELETE CASCADE,
	question_id BIGINT NOT NULL REFERENCES questions(id),
	choice_id BIGINT NOT NULL REFERENCES question_choices(id),
	answered_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);