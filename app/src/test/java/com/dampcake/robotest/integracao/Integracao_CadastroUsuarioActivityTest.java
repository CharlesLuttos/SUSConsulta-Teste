package com.dampcake.robotest.integracao;

import android.widget.EditText;

import com.dampcake.robotest.CadastroUsuarioActivity;
import com.dampcake.robotest.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class Integracao_CadastroUsuarioActivityTest {
    @Test
    public void clickNovoUsuarioValido(){
        CadastroUsuarioActivity
        activity = Robolectric.buildActivity(CadastroUsuarioActivity.class).create().visible().get();
        EditText txt = activity.findViewById(R.id.txtNomeUsuario);
        txt.setText("Carlos");
        DAO.DAO.getHelper(activity.getBaseContext());
        activity.findViewById(R.id.btnCadastroUsuario).performClick();
        assertTrue(activity.isFinishing());
    }
}
