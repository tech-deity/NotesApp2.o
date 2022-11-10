package com.techdeity.noteapp.di

import com.techdeity.noteapp.api.AuthInterceptor
import com.techdeity.noteapp.api.NotesApi
import com.techdeity.noteapp.api.UserAPI
import com.techdeity.noteapp.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofitBuilder():Retrofit.Builder{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)

    }

@Singleton
@Provides
    fun providesOKHttpClient(authInterceptor: AuthInterceptor) :OkHttpClient{
        return  OkHttpClient.Builder().addInterceptor(authInterceptor).build()

    }


    @Singleton
    @Provides
    fun provideUserAPI(retrofitBuilder: Retrofit.Builder):UserAPI{
        return  retrofitBuilder.build().create(UserAPI::class.java)
    }



@Singleton
@Provides
    fun providesNoteAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): NotesApi {

        return retrofitBuilder
            .client(okHttpClient)
            .build().create(NotesApi::class.java)
    }
}