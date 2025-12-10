# 実装から入門するドメイン駆動設計　サンプルコード

## ブランチ
- `main`：3層アーキテクチャ
- `ddd`：ドメイン駆動設計

## 実行方法

### 前提
- Dockerがインストール済み
- VSCodeがインストール済み
- VSCodeに**Dev Containers**拡張機能がインストール済み

### 手順
1. リポジトリをクローンする
2. VSCodeでクローンしたフォルダを開く
3. コマンドパレット（`Ctrl + Shift + P`）を開き、「Dev Containers: Reopen in Container」を実行する（初回はコンテナのビルドに時間がかかる）
4. `src/main/java/com/example/demo/DemoApplication.java`を右クリックして「Run Java」をクリック
5. ブラウザで`http://localhost:8080/index.html`にアクセス
    - APIドキュメントは`http://localhost:8080/swagger-ui.html`で確認可能

## クイズデータの初期化
- `.devcontainer/init_db/csv`フォルダ内のCSVファイルを編集してクイズデータを変更
- コンテナ外（ターミナルなど）から以下のコマンドを実行してデータベースを初期化
  ```bash
  cd .devconatainer
  docker compose down db
  docker compose up db -d --build
  ```