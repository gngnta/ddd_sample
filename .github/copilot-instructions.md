# Copilot Instructions for Spring Boot REST API Development

## このドキュメントについて

- このドキュメントは、GitHub Copilotを使用してSpring Bootを用いたREST API開発を支援するための指示を提供します。
- Copilotが生成するコードがプロジェクトの既存のコードスタイル、アーキテクチャ、およびベストプラクティスに準拠するようにします。
- 不確かな点がある場合には、無理にコードを生成せず、ユーザーに確認を促してください。
- ユーザーとのチャットの内容に、本ドキュメントに含めるべき重要な情報があれば、確認の上、適宜追加してください

## プロジェクト概要

- 本プロジェクトは、Spring Bootを使用したシンプルなREST APIアプリケーションです。
- クイズアプリのバックエンドとして機能し、ユーザー管理、クイズ問題の取得、履歴の保存などの機能を提供します。
- データベースにはPostgreSQLを使用し、Spring Data JPAを利用してデータアクセスを行います。

## 共通の指示

- 回答は日本語で行ってください。
- 大きな変更を加える場合、まず何をするのかの計画を提示し、ユーザーの承認を得てから実行してください。

## ディレクトリ構成

```
.
├── build.gradle
├── .devcontainer
│   ├── app.Dockerfile
│   ├── compose.yaml
│   ├── devcontainer.json
│   └── init_db # データベース初期化用スクリプト
│       ├── 01_schema.sql  # スキーマ定義
│       ├── 02 _data.sql # 初期データ投入
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

## 開発環境について

- 開発環境はdevcontainerにより構築されています。
- 環境に関する情報が必要な場合は、`.devcontainer`ディレクトリ内のファイルを参照してください。
- Spring Bootアプリケーションの設定は、`src/main/resources/application.yml`に記述されています。

## コーディングスタイル

- 一般的なJavaのコーディング規約に従ってください。
- 本プロジェクトは実務で使用されないので、シンプルさを重視しています。
- testコード、例外処理、バリデーションは不要です。
- Javaの中級者レベルの知識を持つ開発者を想定し、高度な文法は避けてください。
- MapStruct、Lombokを使用してください。
- コメントは英語で記述してください。
- コメントを基本不要です。必要な場合には Why を記述してください。

## アーキテクチャ

- シンプルな3層アーキテクチャを採用しています。
- `controller` 
  - レスポンスは必ず dto クラスを使用してください。
- `repository`
  - Spring Data JPAを使用してください。

## ドキュメント

- 日本語で記述してください。
- ユーザーから支持があった場合にのみ、ドキュメントの生成を行ってください。
- ドキュメントのフォーマットはMarkdown形式を使用してください。
- ドキュメントに使用する図には mermaid を使用してください。
- ドキュメントの内容は簡潔にし、冗長な説明は避けてください。
- クイズ仕様は `docs/quiz-specification.md` を参照してください。
