package com.example.pestbusters.localserver

import fi.iki.elonen.NanoHTTPD
import java.io.File
import java.io.FileInputStream

// Extend NanoHTTPD for custom HTTP server
class LocalServer(port: Int, private val assetPath: String) : NanoHTTPD(port) {

    override fun serve(session: IHTTPSession?): Response {
        val file = File(assetPath)

        return if (file.exists()) {
            // Serve the file as an HTTP response
            newFixedLengthResponse(
                Response.Status.OK,
                "text/html",
                FileInputStream(file),
                file.length()
            )
        } else {
            // Handle file not found
            newFixedLengthResponse(
                Response.Status.NOT_FOUND,
                "text/plain",
                "File not found"
            )
        }
    }
}
