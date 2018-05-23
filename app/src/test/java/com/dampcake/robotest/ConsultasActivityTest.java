package com.dampcake.robotest;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowApplication;

import adapters.ConsultaAdapter;
import model.Consulta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class ConsultasActivityTest {

    @Test
    public void abrirCadastroConsultaActivity(){
        ConsultasActivity activity = Robolectric.buildActivity(ConsultasActivity.class).create().visible().get();
        activity.findViewById(R.id.fab).performClick();
        Intent expectedIntent = new Intent(activity, CadastroConsultasActivity.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }

    @Test
    public void inicializarComponentes(){
        ConsultasActivity activity = Robolectric.buildActivity(ConsultasActivity.class).create().visible().get();
        assertNotNull(activity.listView);
        assertNotNull(activity.fab);
    }

    @Test
    public void selecionarItemMenuOpcoes() {
        ConsultasActivity activity = Robolectric.buildActivity(ConsultasActivity.class).create().visible().get();
        MenuItem menuItem = new RoboMenuItem(R.id.action_settings);
        assertTrue(activity.onOptionsItemSelected(menuItem));
    }

    @Test
    public void clickCadastroConsulta(){
        ConsultasActivity activity = Robolectric.buildActivity(ConsultasActivity.class).create().visible().get();
        activity.findViewById(R.id.fab).performClick();
        assertFalse(activity.isFinishing());
    }

   /* @Test
    public void onResume(){
        ConsultasActivity activity = Robolectric.buildActivity(ConsultasActivity.class).create().visible().get();
        activity.carregarLista();
        ListView listview = activity.findViewById(R.id.lista_consulta);
        assertNotNull(listview);
        assertNotNull(listview.getEmptyView());
        assertNotNull(activity.listaConsultas);
        assertNotNull(listview.getAdapter());
    }*/


    @Test
    public void testCreate() {
        ConsultasActivity activity = Robolectric.buildActivity(ConsultasActivity.class).create().get();
        boolean result;
        result = activity != null;
        assertTrue(result);
    }

    @Test
    public void testInicializaBancoCadastroUsuario() {
        ConsultasActivity activity = Robolectric.buildActivity(ConsultasActivity.class).create().get();
        activity.inicializarBanco();
        assertTrue(activity.getConsultaDao() != null);
    }
}
