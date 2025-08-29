package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.dao.StringDao
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.repository.StringRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Hilt用データベースモジュール
 *
 * Hiltによる依存関係注入のためのモジュールクラスです。
 * データベース、DAO、リポジトリなどのインスタンスの作成と提供を行います。
 *
 * @InstallIn(SingletonComponent::class):
 * - アプリケーション全体で単一のインスタンスを共有
 * - アプリの起動から終了まで同じインスタンスを使用
 * - メモリ効率とデータ整合性を確保
 *
 * モジュールが提供するもの:
 * - AppDatabase: Roomデータベースのインスタンス
 * - StringDao: データアクセスオブジェクト
 * - StringRepository: リポジトリパターンの実装
 * - ApplicationScope: アプリケーションスコープのCoroutineScope
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * アプリケーションスコープのCoroutineScopeを提供
     *
     * アプリケーション全体で使用される長期間実行されるコルーチンのスコープです。
     * SupervisorJobを使用することで、子コルーチンの例外が
     * 他の子コルーチンに影響しないようにしています。
     *
     * @ApplicationScopeアノテーション:
     * - 他のCoroutineScopeと区別するための修飾子
     * - 依存性注入時の曖昧さを解消
     *
     * @return アプリケーションスコープのCoroutineScope
     */
    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    /**
     * Roomデータベースのインスタンスを提供
     *
     * AppDatabaseのインスタンスを作成し、アプリケーション全体で共有します。
     * データベースの初期化とコールバック設定も含まれています。
     *
     * 設定内容:
     * - データベース名: "app_database"
     * - 初期データ投入: "Android"文字列
     * - エクスポートスキーマ: false（開発用設定）
     *
     * @param context アプリケーションコンテキスト
     * @param applicationScope データベース初期化用のCoroutineScope
     * @return AppDatabaseのシングルトンインスタンス
     */
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        @ApplicationScope applicationScope: CoroutineScope
    ): AppDatabase {
        return AppDatabase.getDatabase(context, applicationScope)
    }

    /**
     * StringDaoのインスタンスを提供
     *
     * AppDatabaseからStringDaoを取得して提供します。
     * DAOは常にデータベースから取得される必要があります。
     *
     * @param database AppDatabaseのインスタンス
     * @return StringDaoのインスタンス
     */
    @Provides
    fun provideStringDao(database: AppDatabase): StringDao {
        return database.stringDao()
    }

    /**
     * StringRepositoryのインスタンスを提供
     *
     * StringDaoを受け取り、StringRepositoryのインスタンスを作成します。
     * リポジトリパターンにより、ViewModelからのデータアクセスを抽象化します。
     *
     * @param stringDao StringDaoのインスタンス
     * @return StringRepositoryのインスタンス
     */
    @Provides
    @Singleton
    fun provideStringRepository(stringDao: StringDao): StringRepository {
        return StringRepository(stringDao)
    }
}

/**
 * アプリケーションスコープのCoroutineScopeを識別するための修飾子
 *
 * Hiltで複数の同じ型の依存関係がある場合に、
 * どちらを注入するかを明確にするための@Qualifierアノテーションです。
 *
 * 使用例:
 * ```kotlin
 * @Inject constructor(
 *     @ApplicationScope private val scope: CoroutineScope
 * )
 * ```
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope