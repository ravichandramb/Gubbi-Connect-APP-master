package com.mygubbi.imaginestclientconnect.clientHandover;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectApplication;
import com.mygubbi.imaginestclientconnect.models.ClientHandover;
import com.mygubbi.imaginestclientconnect.models.ClientProfile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@SuppressLint("LogNotTimber")
public class CreatePDFTask extends AsyncTask<Void, String, Boolean> {

    private static final String TAG = "CreatePDFTask";

    private ArrayList<ClientHandover> clientHandovers;
    private String signatureImagePath, remarks;
    private Bitmap logoBitmap, checkBitmap;
    private CreatePDFListener listener;

    CreatePDFTask(ArrayList<ClientHandover> clientHandovers, String signatureImagePath,
                  String remarks, Bitmap logoBitmap, Bitmap checkBitmap, CreatePDFListener listener) {
        this.clientHandovers = clientHandovers;
        this.signatureImagePath = signatureImagePath;
        this.remarks = remarks;
        this.logoBitmap = logoBitmap;
        this.checkBitmap = checkBitmap;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onCreatePDFStarted("Creating PDF file, please wait...");
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            File docsFolder = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "GubbiConnect" + File.separator + "Reports");
            if (!docsFolder.exists()) {
                boolean result = docsFolder.mkdirs();
                Log.i(TAG, "Created a new directory for PDF " + result);
            }

            File pdfFile = new File(docsFolder.getAbsolutePath(), "Completion_Report.pdf");
            OutputStream output = new FileOutputStream(pdfFile);

            Document document = new Document();
            PdfWriter.getInstance(document, output);

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
            headerFont.setStyle(Font.BOLD);
            Font subHeaderFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            subHeaderFont.setStyle(Font.BOLD);
            Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

            document.open();

            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("GubbiConnect");
            document.addCreator("GubbiConnect");

            PdfPTable mainTable = new PdfPTable(4);
            mainTable.setWidths(new int[]{1, 1, 1, 1});
            mainTable.setWidthPercentage(90);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            checkBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image imageCheck = Image.getInstance(stream.toByteArray());

            PdfPCell cell = new PdfPCell(new Phrase("Installation Completion Report", headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setFixedHeight(20);
            cell.setColspan(4);

            mainTable.addCell(cell);

            ClientProfile profile = ClientConnectApplication.getInstance().getClientProfile();

            String text = "Name and Address: ";

            if (profile != null) {
                text += profile.getCustomerName().trim();
            }

            cell = new PdfPCell(new Phrase(text, subHeaderFont));
            cell.setVerticalAlignment(Element.ALIGN_TOP | Element.ALIGN_JUSTIFIED);
            cell.setFixedHeight(50);
            cell.setColspan(3);

            mainTable.addCell(cell);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            logoBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            Image logoImage = Image.getInstance(outputStream.toByteArray());

            cell = new PdfPCell(logoImage, false);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setFixedHeight(50);
            cell.setColspan(1);
            cell.setPaddingBottom(10);
            cell.setPaddingTop(10);

            mainTable.addCell(cell);

            cell = new PdfPCell(new Phrase("Scope of Work", subHeaderFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(20);
            cell.setColspan(4);

            mainTable.addCell(cell);

            for (ClientHandover clientHandover : clientHandovers) {
                if (clientHandover.isHeader()) {
                    cell = new PdfPCell(new Phrase(clientHandover.getItem(), subHeaderFont));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setFixedHeight(20);
                    cell.setColspan(4);

                    mainTable.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase(clientHandover.getItem(), valueFont));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setFixedHeight(20);
                    cell.setColspan(3);

                    mainTable.addCell(cell);

                    cell = new PdfPCell(imageCheck, false);
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setFixedHeight(15);
                    cell.setColspan(1);

                    mainTable.addCell(cell);
                }
            }

            if (!TextUtils.isEmpty(remarks)) {
                cell = new PdfPCell(new Phrase("Remarks", subHeaderFont));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setFixedHeight(20);
                cell.setColspan(1);

                mainTable.addCell(cell);

                cell = new PdfPCell(new Phrase(remarks, valueFont));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setFixedHeight(20);
                cell.setColspan(3);

                mainTable.addCell(cell);
            }

            cell = new PdfPCell(new Phrase("", subHeaderFont));
            cell.setBorder(Rectangle.BOX);
            cell.setFixedHeight(10);
            cell.setColspan(4);

            mainTable.addCell(cell);

            cell = new PdfPCell(new Phrase("Supervisor Name: Hemanth", subHeaderFont));
            cell.setBorder(Rectangle.BOX);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(20);
            cell.setColspan(2);

            mainTable.addCell(cell);

            cell = new PdfPCell(new Phrase("Client Name: Hemanth", subHeaderFont));
            cell.setBorder(Rectangle.BOX);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRightIndent(10);
            cell.setFixedHeight(20);
            cell.setColspan(2);

            mainTable.addCell(cell);

            File file = new File(signatureImagePath);
            Bitmap signatureBitmap = BitmapFactory.decodeStream(new FileInputStream(file));

            ByteArrayOutputStream signatureStream = new ByteArrayOutputStream();
            signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, signatureStream);
            Image signatureImage = Image.getInstance(signatureStream.toByteArray());

            cell = new PdfPCell(signatureImage, false);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRightIndent(20);
            cell.setPaddingBottom(10);
            cell.setPaddingTop(10);
            cell.setFixedHeight(50);
            cell.setColspan(4);

            mainTable.addCell(cell);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            cell = new PdfPCell(new Phrase("Date : " + simpleDateFormat.format(new Date()), subHeaderFont));
            cell.setBorder(Rectangle.BOX);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(20);
            cell.setColspan(2);

            mainTable.addCell(cell);

            cell = new PdfPCell(new Phrase("Date & Signature: " + simpleDateFormat.format(new Date()), subHeaderFont));
            cell.setBorder(Rectangle.BOX);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRightIndent(10);
            cell.setFixedHeight(20);
            cell.setColspan(2);

            mainTable.addCell(cell);

            document.add(mainTable);

            document.close();
            return true;
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        super.onPostExecute(isSuccess);
        listener.onPDFCreated(isSuccess);
    }

    public interface CreatePDFListener {

        void onCreatePDFStarted(String message);

        void onPDFCreated(boolean isSuccess);
    }
}