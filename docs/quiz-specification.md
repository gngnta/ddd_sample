# クイズアプリケーション 仕様書

## 1. 概要

カテゴリー単位でクイズに挑戦し、回答履歴を管理するシステム。

## 2. 主要概念

### 2.1 カテゴリー（Category）
- クイズ問題のまとまり
- 複数の問題を保持

### 2.2 挑戦（Attempt）
- 1つのカテゴリーに対して行う1回のクイズ挑戦
- `attempt_id` が自動採番される
- 1つの挑戦内で出題された問題は重複しない
- 回答状況は `completed_at` で判定（null = 進行中、値あり = 完了）

### 2.3 問題（Question）
- 選択式問題（複数の選択肢を持つ）
- カテゴリーに属する
- 選択肢の中に1つの正解を含む

### 2.4 回答（Answer）
- 問題に対して送信する選択肢
- 問題ごとに回答履歴として保存
- 正誤判定を含む

### 2.5 挑戦結果（AttemptResult）
- 1回の挑戦全体の集計結果
- 正解数

## 3. ユースケース

### 3.1 クイズ挑戦開始
```
カテゴリーを選択 → 挑戦（Attempt）を作成 → attempt_id 取得
```

### 3.2 問題出題と回答
```
1. attempt_id で指定された挑戦の次の問題を取得
2. 問題を表示（選択肢を含む）
3. 選択肢を選び、回答を送信
4. 回答を保存（正誤判定を含む）
5. 1～4を繰り返す
```

### 3.3 挑戦完了
```
すべての問題を回答 → 挑戦結果を計算・保存
```

### 3.4 過去の挑戦一覧を参照
```
すべての挑戦履歴（Attempt）を取得
各挑戦の結果を表示
```

### 3.5 挑戦の詳細を参照
```
特定の挑戦（attempt_id）の全問題の回答履歴を取得
問題ごとの正誤を確認
```

## 4. 機能一覧

| # | 機能 | 説明 |
|---|------|------|
| 1 | カテゴリー一覧取得 | 利用可能なカテゴリーを取得 |
| 2 | 挑戦開始 | カテゴリーを指定して挑戦を作成し、attempt_id を取得 |
| 3 | 次の問題取得 | 指定された挑戦内で未回答の問題を取得 |
| 4 | 回答送信 | 問題に対する回答を送信し、結果を保存 |
| 5 | 挑戦結果取得 | 指定された挑戦の最終結果を取得 |
| 6 | 挑戦一覧取得 | すべての挑戦履歴を取得 |
| 7 | 挑戦詳細取得 | 指定された挑戦のすべての回答履歴を取得 |

## 5. データモデル

### 5.1 エンティティ関連図

```mermaid
erDiagram
    CATEGORY ||--o{ QUESTION : ""
    CATEGORY ||--o{ ATTEMPT : ""
    ATTEMPT ||--o{ ANSWER : ""
    QUESTION ||--o{ QUESTION_CHOICE : ""
    QUESTION ||--o{ ANSWER : ""
    QUESTION_CHOICE ||--o{ ANSWER : ""

    CATEGORY {
        long id PK
        string name
        string description
    }

    QUESTION {
        long id PK
        long category_id FK
        string question_text
    }

    QUESTION_CHOICE {
        long id PK
        long question_id FK
        string choice_text
        boolean is_correct
    }

    ATTEMPT {
        long id PK
        long category_id FK
        int total_questions
        int correct_count
        timestamp created_at
        timestamp completed_at
    }

    ANSWER {
        long id PK
        long attempt_id FK
        long question_id FK
        long choice_id FK
        timestamp answered_at
    }
```

### 5.2 主要カラム説明

**CATEGORY**
- `id`: カテゴリーの一意識別子
- `name`: カテゴリー名

**QUESTION**
- `id`: 問題の一意識別子
- `category_id`: 所属するカテゴリー
- `question_text`: 問題文

**QUESTION_CHOICE**
- `id`: 選択肢の一意識別子
- `question_id`: 所属する問題
- `choice_text`: 選択肢テキスト
- `is_correct`: 正解かどうか（true = 正解、false = 不正解）

**ATTEMPT**
- `id`: 挑戦の一意識別子（自動採番）
- `category_id`: 挑戦するカテゴリー
- `total_questions`: 出題問題数
- `correct_count`: 正解数
- `created_at`: 挑戦開始時刻
- `completed_at`: 挑戦完了時刻（null = 進行中、値あり = 完了）

**ANSWER**
- `id`: 回答の一意識別子
- `attempt_id`: 所属する挑戦
- `question_id`: 回答した問題
- `choice_id`: ユーザーが選んだ選択肢
- `answered_at`: 回答時刻


## 6. API仕様（概要）

### 6.1 RESTエンドポイント

```
GET    /api/categories                      # カテゴリー一覧取得
POST   /api/attempts                        # 挑戦開始
GET    /api/attempts/{attempt_id}/question  # 次の問題取得
POST   /api/attempts/{attempt_id}/answer    # 回答送信
GET    /api/attempts/{attempt_id}/result    # 挑戦結果取得
GET    /api/attempts                        # 挑戦一覧取得
GET    /api/attempts/{attempt_id}/answers   # 挑戦の回答履歴取得
```

## 7. ビジネスロジック

### 7.1 次の問題取得
- 指定された挑戦内で、まだ回答していない問題をランダムに1問選択
- 同じ挑戦内では同じ問題を出題しない

### 7.2 回答送信時の処理
1. 回答を受け取る
2. 正誤判定を行う
3. 回答履歴として保存
4. すべての問題に回答したかを判定
   - まだ未回答の問題がある場合：挑戦を続行
   - すべて回答完了：挑戦結果を計算し、`completed_at` を設定

