package com.example.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * アプリケーションクラス（Hilt対応版）
 *
 * Androidアプリケーション全体のライフサイクルを管理するクラスです。
 * Hiltによる依存関係注入により、手動でのインスタンス管理が不要になりました。
 *
 * 主な変更点:
 * - @HiltAndroidAppアノテーションの追加
 * - 手動でのデータベースとリポジトリ管理を削除
 * - Hiltが自動的に依存関係を管理
 *
 * Hiltの利点:
 * - 依存関係の自動注入
 * - ライフサイクルに応じた適切なスコープ管理
 * - テスト時のモック注入の簡易化
 * - コンパイル時の型安全性
 *
 * @HiltAndroidApp:
 * - Hiltによる依存関係注入を有効化
 * - アプリケーション全体でHiltコンポーネントを利用可能にする
 * - 他のAndroidコンポーネント（Activity、Fragment等）でのHilt使用を許可
 */
@HiltAndroidApp
class MyApplication : Application()
