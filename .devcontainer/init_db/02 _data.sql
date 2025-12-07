COPY categories (id, name, description)
FROM '/docker-entrypoint-initdb.d/csv/categories.csv'
WITH (FORMAT CSV, HEADER TRUE);

COPY questions (id, category_id, question_text)
FROM '/docker-entrypoint-initdb.d/csv/questions.csv'
WITH (FORMAT CSV, HEADER TRUE);

COPY question_choices (id, question_id, choice_text, is_correct)
FROM '/docker-entrypoint-initdb.d/csv/question_choices.csv'
WITH (FORMAT CSV, HEADER TRUE);
