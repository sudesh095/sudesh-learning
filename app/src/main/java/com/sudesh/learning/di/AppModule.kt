package com.sudesh.learning.di

import android.content.Context
import androidx.room.Room
import com.sudesh.core.Constants
import com.sudesh.data.api.NewsApi
import com.sudesh.data.datasource.LocalDataSource
import com.sudesh.data.datasource.RemoteDataSource
import com.sudesh.data.db.AppDatabase
import com.sudesh.data.db.ArticleDao
import com.sudesh.data.db.MIGRATION_1_2
import com.sudesh.data.repository.NewsRepositoryImpl
import com.sudesh.domain.repository.NewsRepository
import com.sudesh.domain.usecase.GetTopHeadlinesUseCase
import com.sudesh.domain.usecase.SearchNewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @AppContext
    fun provideAppContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    @ApiUrl
    fun provideRetrofitUrl(): String = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().apply {
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
        }.build()

    @Provides
    @Singleton
    fun provideNewsApi(@ApiUrl baseUrl:String,client: OkHttpClient): NewsApi =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(NewsApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@AppContext context: Context): AppDatabase {
        return   Room.databaseBuilder(context, AppDatabase::class.java, Constants.NEWS_DB)
            .addMigrations(MIGRATION_1_2)
            .build()
    }


    @Provides
    @Singleton
    fun provideDao(db: AppDatabase) = db.articleDao()

    @Provides
    @Singleton
    fun provideNewsDataSource(api: NewsApi): RemoteDataSource =
        RemoteDataSource(api)


    @Provides
    @Singleton
    fun provideLocalDataSource(dao: ArticleDao): LocalDataSource =
        LocalDataSource(dao)

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource): NewsRepository =
        NewsRepositoryImpl(remoteDataSource = remoteDataSource, localDataSource = localDataSource)

    @Provides
    @Singleton
    fun provideGetTopHeadlinesUseCase(repository: NewsRepository) =
        GetTopHeadlinesUseCase(repository)

    @Provides
    @Singleton
    fun provideSearchNewsUseCase(repository: NewsRepository) =
        SearchNewsUseCase(repository)
}