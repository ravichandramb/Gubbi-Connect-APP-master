package com.mygubbi.imaginestclientconnect.helpers;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipUtils {

    public static class scanZipFiles extends AsyncTask<Void, Void, List<String>> {

        private File zipFile;
        private ZipUtilsListener listener;

        public scanZipFiles(File zipFile, ZipUtilsListener listener) {
            this.zipFile = zipFile;
            this.listener = listener;
        }

        /**
         * Runs on the UI thread before {@link #doInBackground}.
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.unZippingStarted();
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param voids The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> fileNamesList = new ArrayList<>();
            ZipFile zipFile;
            try {
                zipFile = new ZipFile(this.zipFile);
                Enumeration fileList;

                for (fileList = zipFile.entries(); fileList.hasMoreElements(); ) {
                    ZipEntry entry = (ZipEntry) fileList.nextElement();
                    fileNamesList.add(entry.getName());
                }

                zipFile.close();
            } catch (IOException e) {
                e.printStackTrace();
                return fileNamesList;
            }
            return fileNamesList;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param resultList The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(List<String> resultList) {
            super.onPostExecute(resultList);
            listener.zipFileContents(resultList);
        }
    }

    public static class unZip extends AsyncTask<Void, Void, Boolean> {

        private File zipFile, targetDirectory;
        private ZipUtilsListener listener;

        public unZip(File zipFile, File targetDirectory, ZipUtilsListener listener) {
            this.zipFile = zipFile;
            this.targetDirectory = targetDirectory;
            this.listener = listener;
        }

        /**
         * Runs on the UI thread before {@link #doInBackground}.
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.unZippingStarted();
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param voids The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Boolean doInBackground(Void... voids) {
            try (ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)))) {
                ZipEntry zipEntry;
                int count;
                byte[] buffer = new byte[8192];
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    File file = new File(targetDirectory, zipEntry.getName());
                    File dir = zipEntry.isDirectory() ? file : file.getParentFile();
                    if (!dir.isDirectory() && !dir.mkdirs())
                        throw new FileNotFoundException("Failed to ensure directory: " +
                                dir.getAbsolutePath());
                    if (zipEntry.isDirectory())
                        continue;
                    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                        while ((count = zipInputStream.read(buffer)) != -1)
                            fileOutputStream.write(buffer, 0, count);
                    }
                    // if time should be restored as well
                /*long time = zipEntry.getTime();
                if (time > 0) {
                    boolean result = file.setLastModified(time);
                    Log.d(TAG, "unZip: " + result);
                }*/
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param result The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                listener.unZipSuccess(targetDirectory);
            } else {
                listener.unZipFailed();
            }
        }
    }

    public interface ZipUtilsListener {

        void unZippingStarted();

        void zipFileContents(List<String> fileNamesList);

        void unZipSuccess(File zipTempFile);

        void unZipFailed();
    }

}