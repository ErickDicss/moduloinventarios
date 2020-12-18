package com.example.pruebainventariostres;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeFormatException;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static android.R.layout.simple_spinner_item;
import static com.example.pruebainventariostres.R.color.colorPrimary;

public class MainActivity extends AppCompatActivity {
    public LinearLayout linearButtonRegistrarInventario, linearRegistrarProducto, linearCompetenciaInventario, LinearButtonEnviar;
    public Button buttonRegistrarProductos, buttonImagenProductoInventariosTres, buttonImagenProductoCompetenciaInventariosTres, buttonEnviarInventariosInventariosTres;
    public ImageView imagenProductoGoldenInventariosTres, imagenProductoCompetenciaInventariosTres;
    public ToggleButton toogleButtonPrecioOfertaGoldenInventariosTres, toogleButtonPrecioOfertaCompetenciaInventariosTres;
    public Spinner spinnerProductoGoldenInventariosTres;
    public EditText editTextCantidadGoldenInventariosTres, editTextPrecioGoldenInventariosTres,
            editTextPrecioGoldenOfertaInventariosTres, editTextPrecioCompetenciaInventariosTres, editTextPrecioCompetenciaOfertaInventariosTres;
    public TextView textViewProductosCompetenciaInventariosTres;
    public String serverResponse;
    public  String sValorStringSpinner;
    public String sValorStringSpinnerCompetencia;
    public  ArrayList aProductosGolden = new ArrayList<>();
    public  ArrayList aProductosCompetencia = new ArrayList();
    public  int Ibandera2;
    public String photophattemp;
    public  String photophattemp2;
    public ProgressDialog progressDialog;
    private static final String CARPETA_PRINCIAL= "misImagenesAPPGolden/";
    private static final String CARPETA_IMAGEN = "imagenes";
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIAL + CARPETA_IMAGEN;
    public File photoFile = null;
    public  int intBandera = 0;
    private static final int REQUEST_CAMERA = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            verifyPermission();
            setContentView(R.layout.activity_main);
            File myfile = new File(Environment.getExternalStorageDirectory(),DIRECTORIO_IMAGEN);
            boolean isCreada = myfile.exists();
            if(isCreada == false){
                isCreada=myfile.mkdirs();
            }

            linearButtonRegistrarInventario = (LinearLayout) findViewById(R.id.linearButtonRegistrarInventario);
            linearRegistrarProducto = (LinearLayout) findViewById(R.id.linearRegistrarProducto);
            linearCompetenciaInventario = (LinearLayout) findViewById(R.id.linearCompetenciaInventario);
            LinearButtonEnviar = (LinearLayout) findViewById(R.id.LinearButtonEnviar);
            buttonRegistrarProductos = (Button) findViewById(R.id.buttonRegistrarProductos);
            buttonImagenProductoInventariosTres = (Button) findViewById(R.id.buttonImagenProductoInventariosTres);
            buttonImagenProductoCompetenciaInventariosTres = (Button) findViewById(R.id.buttonImagenProductoCompetenciaInventariosTres);
            buttonEnviarInventariosInventariosTres = (Button) findViewById(R.id.buttonEnviarInventariosInventariosTres);
            imagenProductoGoldenInventariosTres = (ImageView) findViewById(R.id.imagenProductoGoldenInventariosTres);
            imagenProductoCompetenciaInventariosTres = (ImageView) findViewById(R.id.imagenProductoCompetenciaInventariosTres);
            toogleButtonPrecioOfertaGoldenInventariosTres = (ToggleButton) findViewById(R.id.toogleButtonPrecioOfertaGoldenInventariosTres);
            toogleButtonPrecioOfertaCompetenciaInventariosTres = (ToggleButton) findViewById(R.id.toogleButtonPrecioOfertaCompetenciaInventariosTres);
            spinnerProductoGoldenInventariosTres = (Spinner) findViewById(R.id.spinnerProductoGoldenInventariosTres);
            editTextCantidadGoldenInventariosTres = (EditText) findViewById(R.id.editTextCantidadGoldenInventariosTres);
            editTextPrecioGoldenInventariosTres = (EditText) findViewById(R.id.editTextPrecioGoldenInventariosTres);
            editTextPrecioGoldenOfertaInventariosTres = (EditText) findViewById(R.id.editTextPrecioGoldenOfertaInventariosTres);
            editTextPrecioCompetenciaInventariosTres = (EditText) findViewById(R.id.editTextPrecioCompetenciaInventariosTres);
            editTextPrecioCompetenciaOfertaInventariosTres = (EditText) findViewById(R.id.editTextPrecioCompetenciaOfertaInventariosTres);
            textViewProductosCompetenciaInventariosTres = (TextView) findViewById(R.id.textViewProductosCompetenciaInventariosTres);
            new dameprouctosgolden().execute();

            buttonRegistrarProductos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonRegistrarProductos.setEnabled(false);
                    crearVista();
                }
            });
        }catch (Exception e){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toastperson, (ViewGroup) findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.textToas);
            text.setText("Fallo el oncreate");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

    }

    public void crearVista(){
        try {
        linearButtonRegistrarInventario.setVisibility(View.GONE);
        linearRegistrarProducto.setVisibility(View.VISIBLE);
        linearCompetenciaInventario.setVisibility(View.GONE);
        LinearButtonEnviar.setVisibility(View.VISIBLE);

        imagenProductoGoldenInventariosTres.setImageDrawable(getResources().getDrawable(R.drawable.prueba));
        imagenProductoCompetenciaInventariosTres.setImageDrawable(getResources().getDrawable(R.drawable.prueba));

        ArrayList aProductosGolden2 = new ArrayList<>();
        ArrayList aProductosCompetencia2 = new ArrayList<>();
        aProductosGolden2 = new ArrayList<>(((gurusystem) getApplication()).getaProductosGolden());
        aProductosCompetencia2 = new ArrayList<>(((gurusystem) getApplication()).getaProductosCompetencia());
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, simple_spinner_item, aProductosGolden);
        spinnerProductoGoldenInventariosTres.setAdapter(arrayAdapter);
        final ArrayList finalAProductosGolden = aProductosGolden2;
        final ArrayList finalAProductosCompetencia = aProductosCompetencia2;
        spinnerProductoGoldenInventariosTres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sValorStringSpinner = String.valueOf(finalAProductosGolden.get(position));
                sValorStringSpinnerCompetencia = String.valueOf(finalAProductosCompetencia.get(position));
                if (sValorStringSpinnerCompetencia.equals("") || sValorStringSpinnerCompetencia.equals(" ")) {
                    linearCompetenciaInventario.setVisibility(View.GONE);
                    Ibandera2 = 0;
                } else {
                    Ibandera2 = 1;
                    linearCompetenciaInventario.setVisibility(View.VISIBLE);
                    textViewProductosCompetenciaInventariosTres.setText(String.valueOf(finalAProductosCompetencia.get(position)));
                    editTextPrecioCompetenciaOfertaInventariosTres.setVisibility(View.GONE);
                    toogleButtonPrecioOfertaCompetenciaInventariosTres.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked == true) {
                                editTextPrecioCompetenciaOfertaInventariosTres.setVisibility(View.VISIBLE);
                            } else {
                                editTextPrecioCompetenciaOfertaInventariosTres.setVisibility(View.GONE);
                            }
                        }
                    });

                    buttonImagenProductoCompetenciaInventariosTres.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mostrarDialogoOpciones(2);

                        }
                    });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonImagenProductoInventariosTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonImagenProductoInventariosTres.setEnabled(false);

                mostrarDialogoOpciones(1);

            }
        });

        toogleButtonPrecioOfertaGoldenInventariosTres.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    editTextPrecioGoldenOfertaInventariosTres.setVisibility(View.VISIBLE);
                } else {
                    editTextPrecioGoldenOfertaInventariosTres.setVisibility(View.GONE);
                }
            }
        });

        buttonEnviarInventariosInventariosTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEnviarInventariosInventariosTres.setEnabled(false);
                subirDatos();
            }
        });



    }catch (Exception e){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toastperson, (ViewGroup) findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.textToas);
            text.setText("Fallo al crear la vista");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();

        }
    }

    public void subirDatos(){
        try{
            String []  idProductoGolden =  sValorStringSpinner.split("-");;
            if (sValorStringSpinner == "Seleccione Producto Golden") {
                //  Toast.makeText(getApplicationContext(), "No has Seleccionado Ningún Producto", Toast.LENGTH_SHORT).show();
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toastperson, (ViewGroup) findViewById(R.id.custom_toast_container));
                TextView text = (TextView) layout.findViewById(R.id.textToas);
                text.setText("No has Seleccionado Ningún Producto.");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
                buttonEnviarInventariosInventariosTres.setEnabled(true);
            }else{
                if (!editTextCantidadGoldenInventariosTres.getText().toString().trim().equals("") && !editTextPrecioGoldenInventariosTres.getText().toString().trim().equals("")) {
                    if(Ibandera2 ==1){
                        if (photophattemp == null || photophattemp2 == null) {

                            if(photophattemp == null && photophattemp2 == null){
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toastperson, (ViewGroup) findViewById(R.id.custom_toast_container));
                                TextView text = (TextView) layout.findViewById(R.id.textToas);
                                text.setText("No Has Tomado Ninguna FotografÍa.");
                                Toast toast = new Toast(getApplicationContext());
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                                buttonEnviarInventariosInventariosTres.setEnabled(true);

                            }else{
                                if(photophattemp == null){
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.toastperson, (ViewGroup) findViewById(R.id.custom_toast_container));
                                    TextView text = (TextView) layout.findViewById(R.id.textToas);
                                    text.setText("No Has Tomado La FotografÍa Del Producto Golden.");
                                    Toast toast = new Toast(getApplicationContext());
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(layout);
                                    toast.show();
                                    buttonEnviarInventariosInventariosTres.setEnabled(true);
                                }else{
                                    if(photophattemp2 == null){
                                        LayoutInflater inflater = getLayoutInflater();
                                        View layout = inflater.inflate(R.layout.toastperson, (ViewGroup) findViewById(R.id.custom_toast_container));
                                        TextView text = (TextView) layout.findViewById(R.id.textToas);
                                        text.setText("No Has Tomado La FotografÍa De La Competencia.");
                                        Toast toast = new Toast(getApplicationContext());
                                        toast.setDuration(Toast.LENGTH_LONG);
                                        toast.setView(layout);
                                        toast.show();
                                        buttonEnviarInventariosInventariosTres.setEnabled(true);
                                    }
                                }
                            }
                        }else{
                            procesoSubir();
                        }

                    }else{
                        if (photophattemp == null && (sValorStringSpinnerCompetencia.equals("")  || sValorStringSpinnerCompetencia.equals(" "))) {
                            buttonEnviarInventariosInventariosTres.setEnabled(true);


                            if(photophattemp == null  ){
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toastperson, (ViewGroup) findViewById(R.id.custom_toast_container));
                                TextView text = (TextView) layout.findViewById(R.id.textToas);
                                text.setText("No Has Tomado La FotografÍa Del Producto Golden.");
                                Toast toast = new Toast(getApplicationContext());
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                                buttonEnviarInventariosInventariosTres.setEnabled(true);
                            }

                        } else {
                            procesoSubir();
                        }
                    }
                }else{
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toastperson, (ViewGroup) findViewById(R.id.custom_toast_container));
                    TextView text = (TextView) layout.findViewById(R.id.textToas);
                    text.setText("Debes Completar Todos Los Campos");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                    buttonEnviarInventariosInventariosTres.setEnabled(true);

                }
            }


        }catch (Exception e){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toastperson, (ViewGroup) findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.textToas);
            text.setText("Fallo al tomar foto, regresa al menu anterior y vuelva a intentarlo.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();

        }

    }

    public String reducirimagen(){
        String ok = "";
        try{
            ok = "ok";
            if(Ibandera2==0){
                MediaScannerConnection.scanFile(this, new String[]{photophattemp}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
                Bitmap bitmap = BitmapFactory.decodeFile(photophattemp);
                ExifInterface exif = new ExifInterface(photophattemp);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                Matrix matrix1 = new Matrix();
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix1.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix1.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix1.postRotate(270);
                        break;
                    default:
                        break;
                }

                Bitmap bitmaprotado   = Bitmap.createBitmap(bitmap, 0,0,bitmap.getWidth(), bitmap.getHeight(), matrix1,true);
                bitmaprotado = Bitmap.createScaledBitmap(bitmaprotado, 480, 640, false);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HH-mm-ss").format(new Date());
                String imageFileName = "My_image_"+timeStamp+"_";
                File filesDir = this.getFilesDir();
                File imageFile = new File(filesDir, imageFileName + ".jpg");
                OutputStream os;
                try {
                    os = new FileOutputStream(imageFile);
                    bitmaprotado.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }
                photophattemp = imageFile.toString();

            }else {


                MediaScannerConnection.scanFile(this, new String[]{photophattemp}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
                Bitmap bitmap = BitmapFactory.decodeFile(photophattemp);
                ExifInterface exif = new ExifInterface(photophattemp);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                Matrix matrix1 = new Matrix();
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix1.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix1.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix1.postRotate(270);
                        break;
                    default:
                        break;
                }

                Bitmap bitmaprotado = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix1, true);
                bitmaprotado = Bitmap.createScaledBitmap(bitmaprotado, 480, 640, false);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HH-mm-ss").format(new Date());
                String imageFileName = "My_image_" + timeStamp + "_";
                File filesDir = this.getFilesDir();
                File imageFile = new File(filesDir, imageFileName + ".jpg");
                OutputStream os;
                try {
                    os = new FileOutputStream(imageFile);
                    bitmaprotado.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }
                photophattemp = imageFile.toString();


                MediaScannerConnection.scanFile(this, new String[]{photophattemp2}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {

                            }
                        });

                Bitmap bitmap2 = BitmapFactory.decodeFile(photophattemp2);
                ExifInterface exif2 = new ExifInterface(photophattemp2);
                int orientation2 = exif2.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                Matrix matrix2 = new Matrix();
                switch (orientation2) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix2.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix2.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix2.postRotate(270);
                        break;
                    default:
                        break;
                }
                Bitmap bitmaprotado2   = Bitmap.createBitmap(bitmap2, 0,0,bitmap2.getWidth(), bitmap2.getHeight(), matrix2,true);

                bitmaprotado2 = Bitmap.createScaledBitmap(bitmaprotado2, 480, 640, false);
                String timeStamp2 = new SimpleDateFormat("yyyyMMdd_HH-mm-ss").format(new Date());
                String imageFileName2 = "My_image_"+timeStamp2+"_";
                File filesDir2 = this.getFilesDir();
                File imageFile2 = new File(filesDir2, imageFileName2 + ".jpg");
                OutputStream os2;
                try {
                    os2 = new FileOutputStream(imageFile2);
                    bitmaprotado2.compress(Bitmap.CompressFormat.JPEG, 100, os2);
                    os2.flush();
                    os2.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }
                photophattemp2 = imageFile2.toString();


            }

        }catch (Exception e){
            ok = "fallo";

        }
        return  ok;
    }

    public void procesoSubir(){
        try {

            String idUsuario = "126";
            if (Ibandera2 == 0) {
                if (!idUsuario.equals("")) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Subiendo...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    if (reducirimagen().equals("ok")) {
                        try {
                            new MultipartUploadRequest(this, UUID.randomUUID().toString(),  "https://guruapps.com.mx/Goldenfoods/ws/upload_inventarios2.php")
                                    .addFileToUpload(photophattemp, "ImagePhotoGolden")
                                    .addParameter("catidadGolden", editTextCantidadGoldenInventariosTres.getText().toString().trim())
                                    .addParameter("precioGolden", editTextPrecioGoldenInventariosTres.getText().toString().trim())
                                    .addParameter("precioOfertaGolden", editTextPrecioGoldenOfertaInventariosTres.getText().toString().trim())
                                    .addParameter("idProductoGolden", "1")
                                    .addParameter("id_usuarios_golden", "126")
                                    .addParameter("id_tienda_golden", "1")
                                    .setMaxRetries(2)
                                    .setUtf8Charset()
                                    .setUsesFixedLengthStreamingMode(true)
                                    .setDelegate(new UploadStatusDelegate() {
                                        @Override
                                        public void onProgress(Context context, UploadInfo uploadInfo) {
                                            String sServerResponse = uploadInfo.toString();

                                        }

                                        @Override
                                        public void onError(Context context, UploadInfo uploadInfo, Exception exception) {
                                            String sServerResponse = exception.toString();
                                            progressDialog.hide();
                                            LayoutInflater inflater = getLayoutInflater();
                                            View layout = inflater.inflate(R.layout.toastperson2, (ViewGroup) findViewById(R.id.custom_toast_container2));
                                            TextView text = (TextView) layout.findViewById(R.id.textToas2);
                                            text.setText("Falla de red, verifique que su red no este limitada.");
                                            Toast toast = new Toast(getApplicationContext());
                                            toast.setDuration(Toast.LENGTH_SHORT);
                                            toast.setView(layout);
                                            toast.show();
                                            buttonEnviarInventariosInventariosTres.setEnabled(true);

                                        }

                                        @Override
                                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                            String sServerResponse = serverResponse.toString();
                                            String scontext = context.toString();
                                            String suploadInfo = uploadInfo.toString();
                                            progressDialog.hide();
                                            String respues = serverResponse.getBodyAsString();
                                            try {
                                                JSONObject obj = new JSONObject(respues);
                                                Object obj2 = obj.get("estatus");
                                                if (obj2.equals(1)) {

                                                    LayoutInflater inflater = getLayoutInflater();
                                                    View layout = inflater.inflate(R.layout.toastperson3, (ViewGroup) findViewById(R.id.custom_toast_container3));
                                                    TextView text = (TextView) layout.findViewById(R.id.textToas3);

                                                    text.setText("Se Ha Registrado Correctamente.");
                                                    Toast toast = new Toast(getApplicationContext());
                                                    toast.setDuration(Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                                    toast.setView(layout);
                                                    toast.show();
                                                    //modificarnumero();
                                                    // buscarcarpeta();
                                                    buscarcarpeta2();


                                                    borrarDatos();
                                                    buttonEnviarInventariosInventariosTres.setEnabled(true);


                                                } else {
                                                    LayoutInflater inflater = getLayoutInflater();
                                                    View layout = inflater.inflate(R.layout.toastperson2, (ViewGroup) findViewById(R.id.custom_toast_container2));
                                                    TextView text = (TextView) layout.findViewById(R.id.textToas2);
                                                    text.setText("Ocurrió un error,  intente de nuevo o verifique que la información sea correcta. ");
                                                    Toast toast = new Toast(getApplicationContext());
                                                    toast.setDuration(Toast.LENGTH_SHORT);
                                                    toast.setView(layout);
                                                    toast.show();
                                                    buttonEnviarInventariosInventariosTres.setEnabled(true);
                                                }
                                            } catch (Throwable t) {
                                                progressDialog.hide();
                                                LayoutInflater inflater = getLayoutInflater();
                                                View layout = inflater.inflate(R.layout.toastperson2, (ViewGroup) findViewById(R.id.custom_toast_container2));
                                                TextView text = (TextView) layout.findViewById(R.id.textToas2);
                                                text.setText("Falla de red, verifique que su red no este limitada.");
                                                Toast toast = new Toast(getApplicationContext());
                                                toast.setDuration(Toast.LENGTH_SHORT);
                                                toast.setView(layout);
                                                toast.show();

                                                buttonEnviarInventariosInventariosTres.setEnabled(true);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                                            progressDialog.hide();
                                            buttonEnviarInventariosInventariosTres.setEnabled(true);
                                            //borrarDatos();
                                        }
                                    })
                                    .startUpload();
                        } catch (MalformedURLException e) {
                            progressDialog.hide();
                            e.printStackTrace();

                        } catch (FileNotFoundException e) {
                            progressDialog.hide();
                            e.printStackTrace();
                        }
                    }

                }
            } else {
                if (!idUsuario.equals("")) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Subiendo...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    if (reducirimagen().equals("ok")) {
                        try {
                            new MultipartUploadRequest(this, UUID.randomUUID().toString(),  "https://guruapps.com.mx/Goldenfoods/ws/upload_inventarios.php")
                                    .addFileToUpload(photophattemp, "ImagePhotoGolden")
                                    .addFileToUpload(photophattemp2, "imagePhotoCompetencia")
                                    .addParameter("catidadGolden", editTextCantidadGoldenInventariosTres.getText().toString().trim())
                                    .addParameter("precioGolden", editTextPrecioGoldenInventariosTres.getText().toString().trim())
                                    .addParameter("precioCompetencia", editTextPrecioCompetenciaInventariosTres.getText().toString().trim())
                                    .addParameter("precioOfertaGolden", editTextPrecioGoldenOfertaInventariosTres.getText().toString().trim())
                                    .addParameter("precioOfertaCompetencia", editTextPrecioCompetenciaOfertaInventariosTres.getText().toString().trim())
                                    .addParameter("idProductoGolden", "1")
                                    .addParameter("id_usuarios_golden", idUsuario)
                                    .addParameter("id_tienda_golden", "1")
                                    .setMaxRetries(2)
                                    .setUtf8Charset()
                                    .setUsesFixedLengthStreamingMode(true)
                                    .setDelegate(new UploadStatusDelegate() {
                                        @Override
                                        public void onProgress(Context context, UploadInfo uploadInfo) {
                                            String sServerResponse = uploadInfo.toString();

                                        }

                                        @Override
                                        public void onError(Context context, UploadInfo uploadInfo, Exception exception) {
                                            String sServerResponse = exception.toString();
                                            progressDialog.hide();
                                            LayoutInflater inflater = getLayoutInflater();
                                            View layout = inflater.inflate(R.layout.toastperson2, (ViewGroup) findViewById(R.id.custom_toast_container2));
                                            TextView text = (TextView) layout.findViewById(R.id.textToas2);
                                            text.setText("Falla de red, verifique que su red no este limitada.");
                                            Toast toast = new Toast(getApplicationContext());
                                            toast.setDuration(Toast.LENGTH_SHORT);
                                            toast.setView(layout);
                                            toast.show();
                                            buttonEnviarInventariosInventariosTres.setEnabled(true);

                                        }

                                        @Override
                                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                            String sServerResponse = serverResponse.toString();
                                            String scontext = context.toString();
                                            String suploadInfo = uploadInfo.toString();
                                            progressDialog.hide();
                                            String respues = serverResponse.getBodyAsString();
                                            try {
                                                JSONObject obj = new JSONObject(respues);
                                                Object obj2 = obj.get("estatus");
                                                if (obj2.equals(1)) {

                                                    LayoutInflater inflater = getLayoutInflater();
                                                    View layout = inflater.inflate(R.layout.toastperson3, (ViewGroup) findViewById(R.id.custom_toast_container3));
                                                    TextView text = (TextView) layout.findViewById(R.id.textToas3);

                                                    text.setText("Se Ha Registrado Correctamente.");
                                                    Toast toast = new Toast(getApplicationContext());
                                                    toast.setDuration(Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                                    toast.setView(layout);
                                                    toast.show();

                                                    buscarcarpeta2();


                                                    borrarDatos();
                                                    buttonEnviarInventariosInventariosTres.setEnabled(true);


                                                } else {
                                                    LayoutInflater inflater = getLayoutInflater();
                                                    View layout = inflater.inflate(R.layout.toastperson2, (ViewGroup) findViewById(R.id.custom_toast_container2));
                                                    TextView text = (TextView) layout.findViewById(R.id.textToas2);
                                                    text.setText("Ocurrió un error,  intente de nuevo o verifique que la información sea correcta. ");
                                                    Toast toast = new Toast(getApplicationContext());
                                                    toast.setDuration(Toast.LENGTH_SHORT);
                                                    toast.setView(layout);
                                                    toast.show();
                                                    buttonEnviarInventariosInventariosTres.setEnabled(true);
                                                }
                                            } catch (Throwable t) {
                                                progressDialog.hide();
                                                LayoutInflater inflater = getLayoutInflater();
                                                View layout = inflater.inflate(R.layout.toastperson2, (ViewGroup) findViewById(R.id.custom_toast_container2));
                                                TextView text = (TextView) layout.findViewById(R.id.textToas2);
                                                text.setText("Falla de red, verifique que su red no este limitada.");
                                                Toast toast = new Toast(getApplicationContext());
                                                toast.setDuration(Toast.LENGTH_SHORT);
                                                toast.setView(layout);
                                                toast.show();

                                                buttonEnviarInventariosInventariosTres.setEnabled(true);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                                            progressDialog.hide();
                                            buttonEnviarInventariosInventariosTres.setEnabled(true);
                                            //borrarDatos();
                                        }
                                    })
                                    .startUpload();
                        } catch (MalformedURLException e) {
                            progressDialog.hide();
                            e.printStackTrace();

                        } catch (FileNotFoundException e) {
                            progressDialog.hide();
                            e.printStackTrace();
                        }
                    }

                }

            }

        }catch (Exception e){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toastperson, (ViewGroup) findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.textToas);
            text.setText("Fallo al tomar foto, regresa al menu anterior y vuelva a intentarlo.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();

        }
    }

    public void borrarDatos(){
        try{
            if(Ibandera2==0){

                editTextCantidadGoldenInventariosTres.setText("");
                editTextPrecioGoldenInventariosTres.setText("");
               imagenProductoGoldenInventariosTres.setImageDrawable(getResources().getDrawable(R.drawable.prueba));

                //spinnerProductoGoldenInventariosTres.setSelection(0);
                linearCompetenciaInventario.setVisibility(View.GONE);
                photophattemp = null;
                toogleButtonPrecioOfertaGoldenInventariosTres.setChecked(false);



            }else{
                editTextCantidadGoldenInventariosTres.setText("");
                editTextPrecioGoldenInventariosTres.setText("");
                editTextPrecioCompetenciaInventariosTres.setText("");
                imagenProductoGoldenInventariosTres.setImageDrawable(getResources().getDrawable(R.drawable.prueba));
                imagenProductoCompetenciaInventariosTres.setImageDrawable(getResources().getDrawable(R.drawable.prueba));
                //spinnerProductoGoldenInventariosTres.setSelection(0);
                editTextPrecioCompetenciaOfertaInventariosTres.setText("");
                editTextPrecioGoldenOfertaInventariosTres.setText("");
                toogleButtonPrecioOfertaCompetenciaInventariosTres.setChecked(false);
                toogleButtonPrecioOfertaGoldenInventariosTres.setChecked(false);
                photophattemp = null;
                photophattemp2 = null;

            }

        }catch (Exception e){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toastperson, (ViewGroup) findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.textToas);
            text.setText("Fallo al tomar foto, regresa al menu anterior y vuelva a intentarlo.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

    }

    public void buscarcarpeta2(){
        try {
            File archivo = getExternalFilesDir(DIRECTORIO_IMAGEN);
            // File myfile = new File(Environment.getExternalStorageDirectory(),DIRECTORIO_IMAGEN);
            if (archivo.exists()) {
                String[] files = archivo.list();
                if (files.length > 0) {
                    for (String archivo2 : files) {
                        // File   f = new File("/storage/emulated/0/misImagenesAPPGolden/imagenes/" + File.separator + archivo2);
                        File f = new File(archivo + File.separator + archivo2);
                        f.delete();

                    }
                } else {
                    //Toast.makeText(getApplicationContext(), "la carpeta esta vacia", Toast.LENGTH_LONG).show();
                }
            }
        }catch (Exception e){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toastperson, (ViewGroup) findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.textToas);
            text.setText("Fallo al tomar foto, regresa al menu anterior y vuelva a intentarlo.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
    }



    public void mostrarDialogoOpciones(final int bandera){
        try {
            final CharSequence[] opciones = {"Tomar Foto", "Cancelar"};
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Elige una Opción");
            builder.setCancelable(false);
            builder.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (opciones[which].equals("Tomar Foto")) {
                        tomarFoto(bandera);
                    } else {
                        dialog.dismiss();
                        switch (bandera) {
                            case 1:
                                buttonImagenProductoInventariosTres.setEnabled(true);
                                break;
                            case 2:
                                buttonImagenProductoCompetenciaInventariosTres.setEnabled(true);
                                break;
                        }
                    }
                }
            });
            builder.show();
        }catch (Exception e){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toastperson, (ViewGroup) findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.textToas);
            text.setText("ERROR."+e);
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

    }

    private void tomarFoto(int bandera) {
        try{
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();

                }
                if (photoFile != null) {
                    intBandera = bandera;
                    Uri photoURI = FileProvider.getUriForFile(this, "com.example.pruebainventariostres", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                }
            }
        }catch (Exception e){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toastperson2, (ViewGroup) findViewById(R.id.custom_toast_container2));
            TextView text = (TextView) layout.findViewById(R.id.textToas2);
            text.setText("Fallo la camara intentalo de nuevo.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
            buttonImagenProductoInventariosTres.setEnabled(true);
            buttonImagenProductoCompetenciaInventariosTres.setEnabled(true);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        try{
            if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK)
            {

                switch (intBandera){
                    case 1:
                        photophattemp = photoFile.getAbsolutePath();
                        buttonImagenProductoInventariosTres.setEnabled(true);
                        buttonImagenProductoCompetenciaInventariosTres.setEnabled(true);
                        setPic(imagenProductoGoldenInventariosTres, photophattemp);

                        break;
                    case 2:
                        photophattemp2 = photoFile.getAbsolutePath();
                        buttonImagenProductoInventariosTres.setEnabled(true);
                        buttonImagenProductoCompetenciaInventariosTres.setEnabled(true);
                        setPic(imagenProductoCompetenciaInventariosTres, photophattemp2);

                        break;
                }
            }else{

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toastperson2, (ViewGroup) findViewById(R.id.custom_toast_container2));
                TextView text = (TextView) layout.findViewById(R.id.textToas2);
                text.setText("Toma Una Nueva Fotografía.");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

                buttonImagenProductoInventariosTres.setEnabled(true);
                buttonImagenProductoCompetenciaInventariosTres.setEnabled(true);

            }

        }catch (Exception e){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toastperson2, (ViewGroup) findViewById(R.id.custom_toast_container2));
            TextView text = (TextView) layout.findViewById(R.id.textToas2);
            text.setText("Toma Una Nueva Fotografía.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();

            buttonImagenProductoInventariosTres.setEnabled(true);
            buttonImagenProductoCompetenciaInventariosTres.setEnabled(true);
        }

    }

    private void setPic(ImageView imageView, String phathtemporal) {
        try{
            MediaScannerConnection.scanFile(this, new String[]{phathtemporal}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
            Bitmap bitmap = BitmapFactory.decodeFile(phathtemporal);
            ExifInterface exif = new ExifInterface(phathtemporal);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Matrix matrix1 = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix1.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix1.postRotate(180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix1.postRotate(270);
                    break;
                default:
                    break;
            }

            Bitmap bitmaprotado   = Bitmap.createBitmap(bitmap, 0,0,bitmap.getWidth(), bitmap.getHeight(), matrix1,true);
            imageView.setImageBitmap(bitmaprotado);

        }catch (Exception e){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toastperson2, (ViewGroup) findViewById(R.id.custom_toast_container2));
            TextView text = (TextView) layout.findViewById(R.id.textToas2);
            text.setText("Fallo al rotar");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();

        }
    }

    private File createImageFile() throws IOException{

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH-mm-ss").format(new Date());
        String imageFileName = "My_image_"+timeStamp+"_";
        //File storedir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storedir = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN);
        File photo = File.createTempFile(imageFileName,"jpg", storedir);

        return photo ;
    }

    private void verifyPermission() {
        int permsRequestCode = 100;
        String[] perms = {android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int accessFinePermission = ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.RECORD_AUDIO);
        int accessCoarsePermission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        if (accessFinePermission == PackageManager.PERMISSION_GRANTED  && cameraPermission == PackageManager.PERMISSION_GRANTED  && accessCoarsePermission == PackageManager.PERMISSION_GRANTED) {
            //se realiza metodo si es necesario...
            ActivityCompat.requestPermissions(this, perms, permsRequestCode);
        } else {
            ActivityCompat.requestPermissions(this, perms, permsRequestCode);
        }
    }



    public class dameprouctosgolden extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            //String [] formatotienda = tienda.split("-");
            String URL2 ="https://guruapps.com.mx/Goldenfoods/ws/ws_productos.php?";
            String parametros = "formatotienda=ABARROTES RAMIREZ";
            try{
                URL http = new URL(URL2);
                HttpURLConnection urlConnection = (HttpURLConnection) http.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setReadTimeout(5000);
                urlConnection.setConnectTimeout(5000);
                DataOutputStream stream = new DataOutputStream(urlConnection.getOutputStream());
                stream.writeBytes(parametros);
                stream.flush();
                stream.close();

                serverResponse = urlConnection.getResponseMessage();
                InputStream is = urlConnection.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                serverResponse = total.toString();
            }  catch (TimeFormatException to) {
                serverResponse = String.format("%s execin2 ", to);

            }catch (Exception e){
                serverResponse = String.format("%s exein2", e);
            }

            return serverResponse;
        }
        @Override
        protected void onPostExecute(String response){
            super.onPostExecute(response);


            try {
                JSONArray jsonArray = null;
                jsonArray = new JSONArray(response);

                if(jsonArray.isNull(0)){
                    Toast.makeText(getApplicationContext(), "No existen productos para esta tienda.", Toast.LENGTH_SHORT).show();


                }else{
                    aProductosGolden = new ArrayList<>();
                    aProductosGolden.add("Seleccione Producto Golden");
                    aProductosCompetencia = new ArrayList<>();
                    aProductosCompetencia.add("Seleccione Producto Golden");
                    for ( int x=0; x<jsonArray.length(); x++){
                        JSONObject jsonObject = null;
                        jsonObject = jsonArray.getJSONObject(x);
                        String nombreCompuestoProducto;
                        String idProductoGolden = jsonObject.optString("id_productos");
                        String nombreProductoGolden = jsonObject.optString("descripcion");
                        String nombreProductoCompetencia = jsonObject.optString("competencia_directa");
                        nombreCompuestoProducto = idProductoGolden+"-"+nombreProductoGolden;
                        aProductosGolden.add(nombreCompuestoProducto);
                        aProductosCompetencia.add(nombreProductoCompetencia);
                    }
                    ((gurusystem) getApplication()).setaProductosGolden(aProductosGolden);
                    ((gurusystem) getApplication()).setaProductosCompetencia(aProductosCompetencia);


                }
            } catch (JSONException e) {
                try{
                    JSONObject respuesta = new JSONObject(response);
                    Object respuestaincorrecta = respuesta.get("estatus");
                    String MensajeError = respuesta.getString("mensaje");
                    if(respuestaincorrecta.equals(0)){
                        Toast.makeText(getApplication(), " "+MensajeError, Toast.LENGTH_SHORT).show();


                    }else{
                        Toast.makeText(getApplication(), " "+MensajeError, Toast.LENGTH_SHORT).show();

                    }

                }catch (Exception ee){
                    ee.printStackTrace();
                    Toast.makeText(getApplication(), "Falla de red, verifique que no se encuentre conectado a una red, restringida.", Toast.LENGTH_LONG).show();

                }

                //   Toast.makeText(getActivity(), "Error al cargar los productos", Toast.LENGTH_LONG).show();
            }
        }
    }


}
