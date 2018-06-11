package com.dampcake.robotest;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import DAO.DAO;
import DAO.UsuarioDAO;
import adapters.UsuarioAdapter;
import model.Usuario;


/**********************
 * Activity principal *
 *********************/
public class UsuarioActivity extends AppCompatActivity {
    ArrayList<Usuario> listaUsuarios;
    UsuarioAdapter usuarioAdapter;
    public ListView listViewUsuarios;
    Usuario usuario;
    UsuarioDAO usuarioDAO;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_usuario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inicializaBanco();
        inicializaComponentes();
        definirToolbarIcon();
        listViewUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
                Usuario usuario = (Usuario) listViewUsuarios.getItemAtPosition(position);
                Intent formActivity = new Intent(UsuarioActivity.this, ConsultasActivity.class);
                formActivity.putExtra("usuario", usuario);
                startActivity(formActivity);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCadastroUsuario = new Intent(UsuarioActivity.this, CadastroUsuarioActivity.class);
                startActivity(telaCadastroUsuario);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem apagar = menu.add(R.string.apagar);
        apagar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
                usuario = (Usuario) listViewUsuarios.getItemAtPosition(info.position);
                usuarioDAO.apagar(usuario);
                carregarLista();
                exibirToast(getString(R.string.usuario_excluido));
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
     * Inicializa banco criando conexao e tabelas
     */
    private void inicializaBanco(){
        DAO.getHelper(getBaseContext());
        usuarioDAO  = new UsuarioDAO(getBaseContext());
    }

    /**
     * Instancia os componentes
     */
    private void inicializaComponentes() {
        listViewUsuarios = findViewById(R.id.lista_usuario);
        fab = findViewById(R.id.fab);
    }

    /**
     * Define adapter e carrega lista com dados do banco
     */
    public void carregarLista() {
        listViewUsuarios.setEmptyView(findViewById(android.R.id.empty));
        listaUsuarios = usuarioDAO.listar();
        usuarioAdapter = new UsuarioAdapter(this, listaUsuarios);
        listViewUsuarios.setAdapter(usuarioAdapter);
        // Registra para o menu de contexto (exibido ao manter o toque sobre um item da lista)
        registerForContextMenu(listViewUsuarios);
    }

    /**
     * Exibe toast
     * @param mensagem mensagem a ser exibida
     */
    public void exibirToast(String mensagem) {
        Toast.makeText(UsuarioActivity.this, mensagem, Toast.LENGTH_SHORT).show();
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
