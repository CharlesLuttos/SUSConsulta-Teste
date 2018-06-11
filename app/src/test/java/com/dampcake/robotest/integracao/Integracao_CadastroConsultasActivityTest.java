package com.dampcake.robotest.integracao;

import android.content.Intent;
import android.widget.EditText;

import com.dampcake.robotest.CadastroConsultasActivity;
import com.dampcake.robotest.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import model.Usuario;

import static org.junit.Assert.assertTrue;

/**
 * Created by pesso on 23/05/2018.
 */

@RunWith(RobolectricTestRunner.class)
public class Integracao_CadastroConsultasActivityTest {

    @Test
    public void clickNovaConsultaNaoVazia() {
        Intent intent = new Intent();
        intent.putExtra("usuario", new Usuario(1,"as"));
        CadastroConsultasActivity activity = Robolectric.buildActivity(CadastroConsultasActivity.class, intent).create().visible().get();
        EditText et = activity.findViewById(R.id.txtCodigo);
        et.setText("222757399");
        activity.findViewById(R.id.btnCadastroCodigo).performClick();
        assertTrue(activity.isFinishing());
    }
}
