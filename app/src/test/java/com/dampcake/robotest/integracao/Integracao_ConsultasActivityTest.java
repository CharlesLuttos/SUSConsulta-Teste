package com.dampcake.robotest.integracao;

import com.dampcake.robotest.ConsultasActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class Integracao_ConsultasActivityTest {
    @Test
    public void selecionarConsulta() {
        ConsultasActivity consultasActivity = Robolectric.buildActivity(ConsultasActivity.class).create().visible().get();
        consultasActivity.listViewConsultas.performClick();
    }

    @Test
    public void atualizaConsulta() {
        ConsultasActivity consultasActivity = Robolectric.buildActivity(ConsultasActivity.class).create().visible().get();
        consultasActivity.getConsulta(222757399);
    }
/*
    @Test
    public void testeRecebeObjeto() {
        Usuario carlos = new Usuario(1,"Carlos");
        UsuarioActivity usuarioActivity = Robolectric.buildActivity(UsuarioActivity.class).create().visible().get();
    *//*    usuarioActivity.usuario = carlos;
        usuarioActivity.listaUsuarios = new ArrayList<>();
        usuarioActivity.listaUsuarios.add(carlos);
        usuarioActivity.usuarioAdapter = new UsuarioAdapter(usuarioActivity.getApplication(),usuarioActivity.listaUsuarios);
        usuarioActivity.listViewUsuarios.performItemClick(usuarioActivity.getCurrentFocus(),0,0);
        Intent expectedIntent = new Intent(usuarioActivity, ConsultasActivity.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
        assertNotNull(ShadowApplication.getInstance().getNextStartedActivity().getSerializableExtra("usuario"));
        //ConsultasActivity consultasActivity = Robolectric.buildActivity(ConsultasActivity.class).create().visible().get();
    *//*}*/
}
