package com.techdeity.noteapp.api

import com.techdeity.noteapp.models.NoteRequest
import com.techdeity.noteapp.models.NoteResponse
import retrofit2.Response
import retrofit2.http.*

interface NotesApi {

    @GET("/note")
    suspend fun  getNote(): Response<List<NoteResponse>>

    @POST("/note")
    suspend fun createNote(@Body noteRequest: NoteRequest):Response<NoteResponse>

    @PUT("/note/{noteId}")
    suspend fun updateNote(@Path("noteId") noteId:String,@Body noteRequest: NoteRequest) :Response<NoteResponse>

    @DELETE("/note/{noteId}")
    suspend fun deleteNote(noteId:String):Response<NoteResponse>



}
