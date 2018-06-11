package com.dampcake.robotest;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.ArrayList;

import DAO.ConsultaDAO;
import adapters.ConsultaAdapter;
import model.Consulta;
import model.Usuario;

public class ConsultasActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeLayout;
    public ListView listViewConsultas;
    ArrayList<Consulta> listaConsultas;
    ConsultaAdapter consultaAdapter;
    private ConsultaDAO consultaDAO;
    Usuario usuario;
    public FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_consulta);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resgatarObjetos();
        inicializarBanco();
        inicializarComponentes();
        definirSwipeToPush();
        definirToolbarIcon();

        listViewConsultas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
                Consulta consulta = (Consulta) listViewConsultas.getItemAtPosition(position);
                Intent activityDetalhesConsulta = new Intent(ConsultasActivity.this, DetalhesConsultasActivity.class);
                activityDetalhesConsulta.putExtra("consulta", consulta);
                startActivity(activityDetalhesConsulta);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCadastroIntent = new Intent(ConsultasActivity.this, CadastroConsultasActivity.class);
                telaCadastroIntent.putExtra("usuario", usuario);
                startActivity(telaCadastroIntent);
            }
        });
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem buscarMapa = menu.add(R.string.menu_suspenso_maps);
        MenuItem criarAlerta = menu.add(R.string.menu_suspenso_alerta);
        MenuItem apagar = menu.add(R.string.menu_suspenso_apagar);
        apagar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
                Consulta consulta = (Consulta) listViewConsultas.getItemAtPosition(info.position);
                ConsultaDAO dao = new ConsultaDAO(getApplication());
                dao.apagar(consulta);
                carregarLista();
                Toast.makeText(ConsultasActivity.this, R.string.toast_excluir_consulta, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        buscarMapa.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        criarAlerta.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarLista();
    }

    /**
     * Resgata objetos de uma intent
     */
    private void resgatarObjetos() {
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
    }

    /**
     * Inicializa banco criando conexao e tabelas
     */
    private void inicializarBanco() {
        setConsultaDao(new ConsultaDAO(getBaseContext()));
    }

    /**
     * Instancia os componentes
     */
    private void inicializarComponentes() {
        listViewConsultas = findViewById(R.id.lista_consulta);
        fab = findViewById(R.id.fab);
    }

    /**
     * Define adapter e carrega lista com dados do banco
     */
    public void carregarLista() {
        listViewConsultas = findViewById(R.id.lista_consulta);
        listViewConsultas.setEmptyView(findViewById(android.R.id.empty));
        listaConsultas = consultaDAO.listar(usuario); // Necessario informar usuario para saber quais consultas listar
        consultaAdapter = new ConsultaAdapter(this, listaConsultas);
        listViewConsultas.setAdapter(consultaAdapter);

        // Registra para o menu de contexto (exibido ao manter o toque sobre um item da lista)
        registerForContextMenu(listViewConsultas);
    }

    /**
     * Evento para a açāo de deslizar o dedo para baixo na tela do Android
     */
    private void definirSwipeToPush() {
        swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Codigo funcional
                for (Consulta c : listaConsultas) {
                    getConsulta(c.getCodigoConsulta());
                }
                carregarLista();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 4000);
                exibirToast(getString(R.string.toast_atualizado));
            }
        });
        swipeLayout.setColorSchemeColors( // Muda a cor da animacao (1 segundo para cada cor)
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
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

    /**
     * Obtem uma consulta atualizada da API
     * @param codigo codigo da consulta
     */
    public void getConsulta(Integer codigo){
        AndroidNetworking.get("http://172.20.10.5:8000/autoconsulta/{codConsulta}")
                .addPathParameter("codConsulta", codigo.toString())
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(Consulta.class, new ParsedRequestListener<Consulta>() {
                    @Override
                    public void onResponse(Consulta user) {
                        for(Consulta c : listaConsultas){
                            if(c.getSituacao().equals(user.getSituacao())){
                                c.setPaciente(user.getPaciente());
                                c.setUnidadeSolicitante(user.getUnidadeSolicitante());
                                c.setLocal(user.getLocal());
                                c.setProcedimento(user.getProcedimento());
                                consultaDAO.atualizar(c);
                            }
                        }
                        consultaAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Error: ", anError.getMessage());
                    }
                });
    }

    /**
     * Exibe toast
     * @param mensagem mensagem a ser exibida
     */
    public void exibirToast(String mensagem) {
        Toast.makeText(ConsultasActivity.this, mensagem, Toast.LENGTH_SHORT).show();
    }

    public ConsultaDAO getConsultaDao() {
        return consultaDAO;
    }

    public void setConsultaDao(ConsultaDAO consultaDao) {
        this.consultaDAO = consultaDao;
    }
}
