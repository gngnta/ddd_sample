# Spring Boot REST API 開発のための Copilot 指示書

## プロジェクト概要
- **目的**: シンプルなクイズ REST API バックエンドの構築
- **技術スタック**: Java, Spring Boot, Spring Data JPA, PostgreSQL
- **使用ライブラリ**: Lombok, MapStruct

## コーディング指針
- **シンプルさ**: シンプルで動作するコードを重視。**テスト、例外処理、バリデーションは不要**
- **Javaレベル**: 中級レベル。過度に複雑な構文は避けること 
- **Controller**: 必ず DTO (Response オブジェクト) を返し、Entity を直接返さないこと
- **Repository**: Spring Data JPA を使用すること
- **コメント**: 英語。「What（何をしているか）」ではなく「Why（なぜそうするのか）」のみを説明すること

## 回答指針
- **言語**: 日本語。
- **プロセス**: 大きな変更を行う場合は、実装前に計画を提案し、承認を得ること

## 仕様書
- **場所**: `docs/quiz-specification.md` に記載
- **内容**: API エンドポイント、データモデル、ビジネスロジックの詳細

## ドキュメント
- 形式: Markdown (日本語)
- 図: Mermaid
- 要求された場合のみ作成すること

## ディレクトリ構成

```
├── build.gradle
├── .devcontainer
│   ├── app.Dockerfile
│   ├── compose.yaml
│   ├── devcontainer.json
│   └── init_db # データベース初期化用スクリプト
│       ├── 01_schema.sql  # スキーマ定義
│       ├── 02_data.sql # 初期データ投入
│       └── csv # CSVデータ格納用ディレクトリ
├── .dockerignore
├── docs
│   └── quiz-specification.md # クイズ仕様書
├── .gitattributes
├── .github
│   └── copilot-instructions.md
├── .gitignore
├── .gradle
├── gradlew
├── gradlew.bat
├── settings.gradle
├── src
│   ├── .DS_Store
│   └── main
│       ├── java
│       │   └── com
│       │       └── example
│       │           └── demo # ベースパッケージ名
│       │               ├── controller 
│       │               ├── exception
│       │               ├── repository
│       │               ├── service
│       │               └── DemoApplication.java
│       └── resources
│           └── application.yml
└── wrapper
```
