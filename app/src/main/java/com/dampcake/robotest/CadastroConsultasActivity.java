package com.dampcake.robotest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import DAO.ConsultaDAO;
import model.Consulta;
import model.Usuario;


public class CadastroConsultasActivity extends AppCompatActivity {
    EditText txtCodigo;
    Button button;
    Consulta consulta;
    int codigoConsulta;
    ConsultaDAO consultaDAO;
    JSONObject jsonObject;
    ProgressDialog pd;
    AlertDialog.Builder alerta;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cadastro_consulta);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resgatarObjetos();
        inicializarObjetos();
        inicializarComponentes();
        definirToolbarIcon();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCodigo.getText().toString().isEmpty()) {
                    Toast.makeText(CadastroConsultasActivity.this, R.string.toast_consulta_vazio, Toast.LENGTH_SHORT).show();
                } else if (txtCodigo.getText() != null && txtCodigo.getText().length() > 0) {
                    codigoConsulta = Integer.parseInt(txtCodigo.getText().toString());
                    new ObterDadosJson().execute("http://192.168.0.2/autoconsulta/" + codigoConsulta);
                } else {
                    exibirAlertDialog("Dados", "Digite o código da solicitaçao");
                }
            }
        });
    }

    /**
     * Resgata objetos de uma intent
     */
    public void resgatarObjetos() {
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
    }

    /**
     * Instancia demais objetos
     */
    public void inicializarObjetos() {
        alerta = new AlertDialog.Builder(this);
        consultaDAO = new ConsultaDAO(getBaseContext());
    }

    /**
     * Instancia os componentes
     */
    public void inicializarComponentes() {
        txtCodigo = findViewById(R.id.txtCodigo);
        button = findViewById(R.id.btnCadastroCodigo);
    }

    /**
     * Classe para obter os dados da API em Json
     */
    @SuppressLint("StaticFieldLeak")
    private class ObterDadosJson extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CadastroConsultasActivity.this);
            pd.setMessage("Aguarde");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    String lineBreak = line + "/n";
                    buffer.append(lineBreak);
                    Log.d("Response: ", "> " + line);
                }

                String jsonString = buffer.toString();

                try {
                    jsonObject = new JSONObject(jsonString);
                    Log.d("Paciente: ", jsonObject.get("paciente").toString());
                    Log.d("Procedimento: ", jsonObject.get("procedimento").toString());
                    Log.d("Unidade solicitante: ", jsonObject.get("unidade_solicitante").toString());
                    Log.d("Local atendimento: ", jsonObject.get("local_atendimento").toString());
                    Log.d("Situacao: ", jsonObject.get("situacao").toString());
                } catch (Throwable T) {
                    T.printStackTrace();
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                exibirAlertDialog("Falha na API", "API de serviços inválida");
            } catch (IOException e) {
                e.printStackTrace();
                exibirToast("Falha na conexão com a API de serviços", Toast.LENGTH_SHORT);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException IOEx) {
                    IOEx.printStackTrace();
                }

                try {
                    if (jsonObject != null) {
                        formarObjetoConsulta(jsonObject, false);
                        finish();
                    } else {
                        exibirToast("Consulta não cadastrada", Toast.LENGTH_LONG);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    /**
     * Forma um objeto Consulta a partir de um JSON
     * @param jsonObject objeto JSON com os dados da consulta
     * @param test Flag de testes
     * @return Objeto de consulta com os dados do JSON
     * @throws JSONException Ao nao conseguir montar o objeto
     */
    public Consulta formarObjetoConsulta(JSONObject jsonObject, boolean test) throws JSONException {
        consulta = null;
        boolean invalido = true;

        if(jsonObject.has("cod_consulta") &&
           jsonObject.has("paciente") &&
           jsonObject.has("procedimento") &&
           jsonObject.has("unidade_solicitante") &&
           jsonObject.has("local_atendimento") &&
           jsonObject.has("situacao")) {
            invalido = false;
            consulta = new Consulta();
            consulta.setCodigoConsulta(jsonObject.getInt("cod_consulta"));
            consulta.setPaciente(jsonObject.get("paciente").toString());
            consulta.setProcedimento(jsonObject.get("procedimento").toString());
            consulta.setUnidadeSolicitante(jsonObject.get("unidade_solicitante").toString());
            consulta.setLocal(jsonObject.get("local_atendimento").toString());
            consulta.setSituacao(Integer.parseInt(jsonObject.get("situacao").toString()));
        } else {
            invalido = true;
        }
        consulta.setUsuario(usuario);
        if(!test && !invalido) {
            inserirConsulta(consulta);
        }
        return consulta;
    }


    private void inserirConsulta(Consulta c) {
        try {
            consultaDAO.inserir(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exibirToast(String mensagem, int duracao) {
        Toast.makeText(this, mensagem, duracao).show();
    }

    public void exibirAlertDialog(String titulo, String mensagem) {
        alerta.setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Define icone da toolbar
     */
    private void definirToolbarIcon() {
        try {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
                actionBar.setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.ic_toolbar);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}
