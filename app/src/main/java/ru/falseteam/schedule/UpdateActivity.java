package ru.falseteam.schedule;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import javax.net.ssl.SSLSocket;

import ru.falseteam.schedule.data.StaticData;
import ru.falseteam.vframe.socket.VFKeystore;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private Button actUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);
        actUpdate = (Button) findViewById(R.id.actUpdate);
        actUpdate.setOnClickListener(this);
        Button actRemind = (Button) findViewById(R.id.actRemind);
        actRemind.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.currentVersion)).setText(StaticData.getClientVersion());
        ((TextView) findViewById(R.id.newVersion)).setText(getIntent().getStringExtra("version"));
        setFinishOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actUpdate:
                new LoadTask().execute();
                break;
            case R.id.actRemind:
                finish();
                break;
        }
    }

    private class LoadTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setMax(100);
                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.VISIBLE);
                    actUpdate.setEnabled(false);
                }
            });
        }

        @Override
        protected void onProgressUpdate(final Integer... values) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(values[0]);
                }
            });
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        private void deleteRecurse(File file) {
            if (file.isDirectory()) for (File f : file.listFiles()) deleteRecurse(f);
            file.delete();
        }

        @SuppressWarnings({"ResultOfMethodCallIgnored", "SpellCheckingInspection"})
        @Override
        protected String doInBackground(Void... nothing) {
            String path;
            try {
                deleteRecurse(getCacheDir());
                File file = File.createTempFile("schedule", "apk", getCacheDir());
                path = file.getAbsolutePath();
                if (file.exists()) file.delete();
                if (!file.createNewFile()) throw new Exception("File not created " + path);
                file.setReadable(true, false);
                VFKeystore k = new VFKeystore(getApplicationContext().getResources().openRawResource(R.raw.keystore), StaticData.getPublicPass());
                SSLSocket socket = (SSLSocket) k.getSSLContext().getSocketFactory().createSocket(StaticData.getHostname(), StaticData.getPortUpdate());
                if (socket == null) throw new Exception("SSLSocket is null");
                InputStream sin = new BufferedInputStream(socket.getInputStream());
                OutputStream fout = new FileOutputStream(file);

                byte[] length2receive = ByteBuffer.allocate(8).array();
                sin.read(length2receive);
                ByteBuffer bb = ByteBuffer.allocate(8);
                bb.put(length2receive);
                bb.flip();
                long fileLength = bb.getLong();

                byte[] buff = new byte[1024];
                long total = 0;
                int count;
                while ((count = sin.read(buff)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    fout.write(buff, 0, count);
                }
                fout.flush();
                fout.close();
                sin.close();
            } catch (Exception ignore) {
                ignore.printStackTrace();
                return null;
            }
            return path;
        }

        @Override
        protected void onPostExecute(String path) {
            if (path == null) return;
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
