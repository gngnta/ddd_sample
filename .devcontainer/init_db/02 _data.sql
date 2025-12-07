COPY categories (id, name, description)
FROM '/workspace/.devcontainer/init_db/csv/categories.csv'
WITH (FORMAT CSV, HEADER TRUE);

COPY questions (id, category_id, question_text)
FROM '/workspace/.devcontainer/init_db/csv/questions.csv'
WITH (FORMAT CSV, HEADER TRUE);

COPY question_choices (id, question_id, choice_text, is_correct)
FROM '/workspace/.devcontainer/init_db/csv/question_choices.csv'
WITH (FORMAT CSV, HEADER TRUE);
