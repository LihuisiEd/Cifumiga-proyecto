package com.cifumiga.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.android.synthetic.main.activity_pdf.*

class PdfActivity : AppCompatActivity() {

    lateinit var pdfView: PDFView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        pdfView = findViewById<PDFView>(R.id.idPDFView)
        pdfView.fromAsset("brochure.pdf").load()
    }
}