package com.dampcake.robotest.integracao;

import com.dampcake.robotest.BuildConfig;
import com.dampcake.robotest.UsuarioActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 27)
public class Integracao_UsuarioActivityTest {
    @Test
    public void testeCarregaListaDeUsuarios() {
        UsuarioActivity usuarioActivity = Robolectric.buildActivity(UsuarioActivity.class).create().visible().get();
        usuarioActivity.carregarLista();
    }
}
