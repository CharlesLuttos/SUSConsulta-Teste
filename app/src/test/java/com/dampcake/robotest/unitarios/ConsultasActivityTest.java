package com.dampcake.robotest.unitarios;

import android.content.Intent;
import android.view.MenuItem;

import com.dampcake.robotest.CadastroConsultasActivity;
import com.dampcake.robotest.ConsultasActivity;
import com.dampcake.robotest.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowApplication;

import model.Usuario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class ConsultasActivityTest {

    @Test
    public void testCreate() {
        ConsultasActivity activity = Robolectric.buildActivity(ConsultasActivity.class).create().get();
        boolean result;
        result = activity != null;
        assertTrue(result);
    }

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
        assertNotNull(activity.listViewConsultas);
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

    @Test
    public void testeCliqueListaConsultas() {
        ConsultasActivity activity = Robolectric.buildActivity(ConsultasActivity.class).create().visible().get();
        activity.listViewConsultas.performItemClick(activity.getCurrentFocus(),0,0);
    }

    @Test
    public void testeCarregaLista() {
        Intent intent = new Intent();
        intent.putExtra("usuario", new Usuario(1,"nome"));
        ConsultasActivity activity = Robolectric.buildActivity(ConsultasActivity.class, intent).create().resume().visible().get();
    }

    @Test
    public void testInicializaBancoCadastroUsuario() {
        ConsultasActivity activity = Robolectric.buildActivity(ConsultasActivity.class).create().get();
        assertTrue(activity.getConsultaDao() != null);
    }

}
