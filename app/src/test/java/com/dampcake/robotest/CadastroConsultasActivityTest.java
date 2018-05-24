package com.dampcake.robotest;

import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.fakes.RoboMenuItem;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by pesso on 23/05/2018.
 */

@RunWith(RobolectricTestRunner.class)
public class CadastroConsultasActivityTest {

    @Test
    public void testCreate() {
        CadastroConsultasActivity activity = Robolectric.buildActivity(CadastroConsultasActivity.class).create().get();
        boolean result;
        result = activity != null;
        assertTrue(result);
    }

    @Test
    public void testeInicializaComponentes() {
        CadastroConsultasActivity activity = Robolectric.buildActivity(CadastroConsultasActivity.class).create().get();
        assertTrue(activity.txtCodigo != null);
        assertTrue(activity.button != null);
    }

    @Test
    public void selecionarItemMenuOpcoes() {
        CadastroConsultasActivity activity = Robolectric.buildActivity(CadastroConsultasActivity.class).create().visible().get();
        MenuItem menuItem = new RoboMenuItem(R.id.action_settings);
        assertTrue(activity.onOptionsItemSelected(menuItem));
    }


    @Test
    public void clickNovaConsultaVazia() {
        CadastroConsultasActivity activity = Robolectric.buildActivity(CadastroConsultasActivity.class).create().visible().get();
        activity.findViewById(R.id.btnCadastroCodigo).performClick();
        assertFalse(activity.isFinishing());
    }

    @Test
    public void clickNovaConsultaNaoVazia() {
        CadastroConsultasActivity activity = Robolectric.buildActivity(CadastroConsultasActivity.class).create().visible().get();
        EditText et = activity.findViewById(R.id.txtCodigo);
        et.setText("8");
        activity.findViewById(R.id.btnCadastroCodigo).performClick();
        assertFalse(activity.isFinishing());
    }

    @Test
    public void formarObjetoConsulta() throws JSONException {
        CadastroConsultasActivity activity = Robolectric.buildActivity(CadastroConsultasActivity.class).create().visible().get();
        JSONObject json = new JSONObject();
        json.put("cod_consulta", "1");
        json.put("paciente", "Carlos");
        json.put("procedimento", "1");
        json.put("unidade_solicitante", "1");
        json.put("local_atendimento", "1");
        json.put("situacao", "1");

        Assert.assertNotNull(activity.formarObjetoConsulta(json, true));

    }

    @Test
    public void exibirAlertDialog(){
        CadastroConsultasActivity activity = Robolectric.buildActivity(CadastroConsultasActivity.class).create().visible().get();
        activity.exibirAlertDialog("teste", "teste");
        Assert.assertNotNull(activity.alerta);
    }
}
